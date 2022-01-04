package cl.isosalud.service.service.document;

import cl.isosalud.service.dto.DocumentCollectionDto;
import cl.isosalud.service.dto.DocumentDto;
import cl.isosalud.service.dto.GenericResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DocumentService {

    DocumentDto getById(int productId);

    List<DocumentCollectionDto> upload(MultipartFile[] multipartFiles, Integer patientId, String collectionName);

    List<DocumentDto> getAll();

    List<DocumentCollectionDto> getByPatientId(Integer id);

    GenericResponseDto delete(Integer fileId);
}
