package cl.isosalud.service.service.document;

import cl.isosalud.service.dto.DocumentCollectionDto;
import cl.isosalud.service.dto.DocumentDto;
import cl.isosalud.service.dto.GenericResponseDto;
import cl.isosalud.service.dto.UserDto;
import cl.isosalud.service.entity.CommuneEntity;
import cl.isosalud.service.entity.DocumentEntity;
import cl.isosalud.service.entity.UserEntity;
import cl.isosalud.service.exception.GenericException;
import cl.isosalud.service.mapping.Mapper;
import cl.isosalud.service.repository.DocumentRepository;
import cl.isosalud.service.repository.UserRepository;
import cl.isosalud.service.util.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;
    private final UserRepository userRepository;
    private final Mapper mapper;
    private Path docStorageLocation;

    @Value("${documents.upload-directory}")
    private String uploadDirectory;

    @PostConstruct
    public void init() {
        try {
            docStorageLocation = Paths.get(uploadDirectory).toAbsolutePath().normalize();
            Files.createDirectories(docStorageLocation);
        } catch (IOException e) {
            throw new RuntimeException("Error al cargar path de uploads.");
        }
    }

    @Override
    public DocumentDto getById(int documentId) {
        DocumentEntity documentEntity = documentRepository.findById(documentId)
                .orElseThrow(() -> new GenericException(HttpStatus.NOT_FOUND, String.format("Document id %s not found", documentId), String.format("Document id %s not found", documentId)));

        return map(documentEntity);
    }

    @Override
    public List<DocumentDto> getAll() {
        return documentRepository.findAll()
                .stream()
                .map(this::map)
                .toList();
    }

    @Override
    public List<DocumentCollectionDto> getByPatientId(Integer patientId) {
        UserEntity patientUser = userRepository.findById(patientId)
                .orElseThrow(() -> new GenericException(HttpStatus.NOT_FOUND, String.format("Patient id %s not found", patientId), String.format("Patient id %s not found", patientId)));


        return documentRepository.findAllByPatientUser(patientUser).stream()
                .map(this::map)
                .collect(Collectors.groupingBy(DocumentDto::getCollectionName))
                .values()
                .stream()
                .map(documentDtos -> {
                    DocumentDto first = documentDtos.stream().findFirst().orElseThrow(() -> new GenericException("Error", List.of("Error")));

                    return DocumentCollectionDto.builder()
                            .collectionName(first.getCollectionName())
                            .date(first.getDate())
                            .medicUser(first.getMedicUser())
                            .documents(documentDtos.stream().peek(doc -> doc.setMedicUser(null)).toList())
                            .build();
                })
                .toList();
    }

    @Override
    public GenericResponseDto delete(Integer fileId) {
        DocumentEntity documentEntity = documentRepository.findById(fileId)
                .orElseThrow(() -> new GenericException(HttpStatus.NOT_FOUND, String.format("File id %s not found", fileId), String.format("File id %s not found", fileId)));

        try {
            documentRepository.delete(documentEntity);
            return GenericResponseDto.builder()
                    .message("OK")
                    .details(List.of(documentEntity.getName()))
                    .build();
        }catch (Exception e) {
            return GenericResponseDto.builder()
                    .message("NOK")
                    .build();
        }
    }

    @Override
    public List<DocumentCollectionDto> upload(MultipartFile[] multipartFiles, Integer patientId, String collectionName) {
        List<DocumentDto> files = new ArrayList<>();

        String uploadHash = patientId.toString().concat(String.valueOf(new Date().getTime()));

        for (MultipartFile multipartFile : multipartFiles) {
            files.add(upload(uploadHash, multipartFile, patientId, collectionName));
        }

        return files
                .stream()
                .collect(Collectors.groupingBy(DocumentDto::getCollectionName))
                .values()
                .stream()
                .map(documentDtos -> {
                    DocumentDto first = documentDtos.stream().findFirst().orElseThrow(() -> new GenericException("Error", List.of("Error")));

                    return DocumentCollectionDto.builder()
                            .collectionName(first.getCollectionName())
                            .date(first.getDate())
                            .medicUser(first.getMedicUser())
                            .documents(documentDtos.stream().peek(doc -> doc.setMedicUser(null)).toList())
                            .build();
                })
                .toList();
    }

    private DocumentDto upload(String uploadHash, MultipartFile multipartFiles, Integer patientId, String collectionName) {
        UserEntity medicUser = userRepository.findByUsernameIgnoreCase(UserUtils.getUsernameLogged())
                .orElseThrow(() -> new GenericException(HttpStatus.NOT_FOUND, String.format("Medic id %s not found", UserUtils.getUsernameLogged()), String.format("Medic id %s not found", UserUtils.getUsernameLogged())));

        UserEntity patientUser = userRepository.findById(patientId)
                .orElseThrow(() -> new GenericException(HttpStatus.NOT_FOUND, String.format("Patient id %s not found", patientId), String.format("Patient id %s not found", patientId)));

        DocumentEntity documentEntity = DocumentEntity.builder()
                .name(multipartFiles.getOriginalFilename())
                .mimeType(multipartFiles.getContentType())
                .size(multipartFiles.getSize())
                .medicUser(medicUser)
                .patientUser(patientUser)
                .uploadHash(uploadHash)
                .collectionName(collectionName)
                .date(LocalDateTime.now())
                .build();

        documentEntity.setHash();

        saveFile(multipartFiles, documentEntity.getHash());

        documentEntity = documentRepository.save(documentEntity);

        return map(documentEntity);
    }

    private void saveFile(MultipartFile file, String hash) {
        try {
            Path targetLocation = docStorageLocation.resolve(hash);
            Files.copy(file.getInputStream(), targetLocation);
        } catch (IOException e) {
            throw new GenericException("Error", List.of("Error al subir archivo."));
        }
    }

    private DocumentDto map(DocumentEntity documentEntity) {
        return DocumentDto.builder()
                .id(documentEntity.getId())
                .name(documentEntity.getName())
                .mimeType(documentEntity.getMimeType())
                .size(documentEntity.getSize())
                .medicUser(mapper.map(documentEntity.getMedicUser(), UserDto.class))
                .patientUser(mapper.map(documentEntity.getPatientUser(), UserDto.class))
                .hash(documentEntity.getHash())
                .uploadHash(documentEntity.getUploadHash())
                .collectionName(documentEntity.getCollectionName())
                .date(documentEntity.getDate())
                .downloadUrl(docStorageLocation.resolve(documentEntity.getHash()).toString().replace("/uploads/", ""))
                .build();
    }

}
