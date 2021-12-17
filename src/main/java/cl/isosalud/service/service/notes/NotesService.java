package cl.isosalud.service.service.notes;

import cl.isosalud.service.dto.GenericResponseDto;
import cl.isosalud.service.dto.NameDescriptionObj;
import cl.isosalud.service.dto.NoteDto;
import cl.isosalud.service.dto.ProductDto;

import java.util.List;

public interface NotesService {

    NoteDto getById(int noteId);
    NoteDto create(NoteDto noteId);
    List<NoteDto> getAll();
    NoteDto update(Integer id, NoteDto noteDto);
    List<NameDescriptionObj> getAllNoteTypes();
    List<NoteDto> getNotesByPatientId(Integer patientId);
    GenericResponseDto delete(Integer id);
}
