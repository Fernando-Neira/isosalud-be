package cl.isosalud.service.controller;

import cl.isosalud.service.dto.DocumentCollectionDto;
import cl.isosalud.service.dto.DocumentDto;
import cl.isosalud.service.dto.GenericResponseDto;
import cl.isosalud.service.dto.ResponseListWrapper;
import cl.isosalud.service.service.document.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(value = "/files", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class FilesController {

    private final DocumentService documentService;

    @PreAuthorize("hasAnyRole('ROLE_DENTIST', 'ROLE_ADMIN')")
    @GetMapping(path = "")
    public ResponseListWrapper<List<DocumentDto>> getAllFiles() {
        return new ResponseListWrapper<>(documentService.getAll());
    }

    @PreAuthorize("hasAnyRole('ROLE_DENTIST', 'ROLE_ADMIN')")
    @GetMapping(path = "/search")
    public DocumentDto getProduct(@RequestParam Integer id) {
        return documentService.getById(id);
    }

    @PreAuthorize("hasAnyRole('ROLE_DENTIST', 'ROLE_ADMIN')")
    @GetMapping(path = "/search-by-patient")
    public ResponseListWrapper<List<DocumentCollectionDto>> getFilesPatientId(@RequestParam Integer id) {
        return new ResponseListWrapper<>(documentService.getByPatientId(id));
    }

    @PreAuthorize("hasAnyRole('ROLE_DENTIST', 'ROLE_ADMIN')")
    @PostMapping(path = "/upload")
    public ResponseListWrapper<List<DocumentCollectionDto>> uploadFile(@RequestParam MultipartFile[] file, @RequestParam Integer patientId, @RequestParam String collectionName) {
        return new ResponseListWrapper<>(documentService.upload(file, patientId, collectionName));
    }

    @PreAuthorize("hasAnyRole('ROLE_DENTIST', 'ROLE_ADMIN')")
    @PostMapping(path = "/delete")
    public GenericResponseDto deleteFile(@RequestParam Integer fileId) {
        return documentService.delete(fileId);
    }

}
