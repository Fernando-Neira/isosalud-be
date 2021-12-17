package cl.isosalud.service.controller;

import cl.isosalud.service.dto.GenericResponseDto;
import cl.isosalud.service.dto.NameDescriptionObj;
import cl.isosalud.service.dto.NoteDto;
import cl.isosalud.service.dto.ResponseListWrapper;
import cl.isosalud.service.service.notes.NotesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/notes", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class NotesController {

    //TODO: Do this!!!
    private final NotesService notesService;

    @PreAuthorize("hasAnyRole('ROLE_DENTIST', 'ROLE_ADMIN')")
    @GetMapping(path = "")
    public ResponseListWrapper<List<NoteDto>> getAllNotes() {
        return new ResponseListWrapper<>(notesService.getAll());
    }

    @PreAuthorize("hasAnyRole('ROLE_DENTIST', 'ROLE_ADMIN')")
    @GetMapping(path = "/search")
    public NoteDto getNoteById(@RequestParam Integer id) {
        return notesService.getById(id);
    }

    @PreAuthorize("hasAnyRole('ROLE_DENTIST', 'ROLE_ADMIN')")
    @GetMapping(path = "/search-patient")
    public ResponseListWrapper<List<NoteDto>> getPatientNotes(@RequestParam(name = "id") Integer patientId) {
        return new ResponseListWrapper<>(notesService.getNotesByPatientId(patientId));
    }

    @PreAuthorize("hasAnyRole('ROLE_DENTIST', 'ROLE_ADMIN')")
    @PostMapping(path = "")
    public NoteDto createNote(@RequestBody NoteDto payload) {
        return notesService.create(payload);
    }

    @PreAuthorize("hasAnyRole('ROLE_DENTIST', 'ROLE_ADMIN')")
    @PostMapping(path = "/update")
    public NoteDto updateNote(@RequestParam Integer id, @RequestBody NoteDto payload) {
        return notesService.update(id, payload);
    }

    @PreAuthorize("hasAnyRole('ROLE_DENTIST', 'ROLE_ADMIN')")
    @PostMapping(path = "/delete")
    public GenericResponseDto updateNote(@RequestParam Integer id) {
        return notesService.delete(id);
    }

    @PreAuthorize("hasAnyRole('ROLE_DENTIST', 'ROLE_ADMIN')")
    @GetMapping(path = "/types")
    public ResponseListWrapper<List<NameDescriptionObj>> getAllNoteTypes() {
        return new ResponseListWrapper<>(notesService.getAllNoteTypes());
    }

}
