package cl.isosalud.service.service.notes;

import cl.isosalud.service.dto.GenericResponseDto;
import cl.isosalud.service.dto.NameDescriptionObj;
import cl.isosalud.service.dto.NoteDto;
import cl.isosalud.service.dto.UserDto;
import cl.isosalud.service.entity.NoteEntity;
import cl.isosalud.service.entity.NoteStatusEntity;
import cl.isosalud.service.entity.NoteTypeEntity;
import cl.isosalud.service.entity.UserEntity;
import cl.isosalud.service.exception.GenericException;
import cl.isosalud.service.mapping.Mapper;
import cl.isosalud.service.repository.NoteRepository;
import cl.isosalud.service.repository.NoteStatusEntityRepository;
import cl.isosalud.service.repository.NoteTypeRepository;
import cl.isosalud.service.repository.UserRepository;
import cl.isosalud.service.util.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class NotesServiceImpl implements NotesService {

    private final NoteRepository noteRepository;
    private final NoteTypeRepository noteTypeRepository;
    private final UserRepository userRepository;
    private final NoteStatusEntityRepository noteStatusEntityRepository;
    private final Mapper mapper;

    @Override
    public NoteDto getById(int noteId) {
        return noteRepository.findById(noteId)
                .map(p -> NoteDto.builder()
                        .id(p.getId())
                        .author(mapper.map(p.getAuthorUserEntity(), UserDto.class))
                        .destinatario(mapper.map(p.getDestUserEntity(), UserDto.class))
                        .comment(p.getComment())
                        .dateCreated(p.getDateCreated())
                        .noteType(p.getNoteType().getName())
                        .dateReminder(p.getDateReminder())
                        .reminderStatus(p.getNoteStatusEntity() != null ? p.getNoteStatusEntity().getName() : null)
                        .build())
                .orElseThrow(() -> new GenericException(HttpStatus.NOT_FOUND, String.format("Note id %s not found", UserUtils.getUsernameLogged()), String.format("Note id %s not found", UserUtils.getUsernameLogged())));
    }

    @Override
    public List<NoteDto> getNotesByPatientId(Integer patientId) {
        UserEntity userEntity = userRepository.findById(patientId)
                .orElseThrow(() -> new GenericException(HttpStatus.NOT_FOUND, String.format("Username %s not found", UserUtils.getUsernameLogged()), String.format("Username %s not found", UserUtils.getUsernameLogged())));

        return noteRepository.findAllByDestUserEntity(userEntity)
                .stream()
                .map(p -> NoteDto.builder()
                        .id(p.getId())
                        .author(mapper.map(p.getAuthorUserEntity(), UserDto.class))
                        .destinatario(mapper.map(p.getDestUserEntity(), UserDto.class))
                        .comment(p.getComment())
                        .dateCreated(p.getDateCreated())
                        .noteType(p.getNoteType().getName())
                        .dateReminder(p.getDateReminder())
                        .reminderStatus(p.getNoteStatusEntity() != null ? p.getNoteStatusEntity().getName() : null)
                        .build())
                .toList();
    }

    @Override
    public GenericResponseDto delete(Integer id) {
        NoteEntity noteEntity = noteRepository.findById(id)
                .orElseThrow(() -> new GenericException(HttpStatus.NOT_FOUND, String.format("Note id %s not found", id), String.format("Note id %s not found", id)));

        try {
            noteRepository.delete(noteEntity);
            return GenericResponseDto.builder()
                    .message("OK")
                    .build();
        } catch (Exception e) {
            return GenericResponseDto.builder()
                    .message("NOK")
                    .build();
        }
    }

    @Override
    public List<NoteDto> getAll() {
        UserEntity medicUser = userRepository.findByUsernameIgnoreCase(UserUtils.getUsernameLogged())
                .orElseThrow(() -> new GenericException(HttpStatus.NOT_FOUND, String.format("Medic id %s not found", UserUtils.getUsernameLogged()), String.format("Medic id %s not found", UserUtils.getUsernameLogged())));

        return noteRepository.findAllByAuthorUserEntityAndDestUserEntity(medicUser, null)
                .stream()
                .map(p -> NoteDto.builder()
                        .id(p.getId())
                        .author(mapper.map(p.getAuthorUserEntity(), UserDto.class))
                        .destinatario(mapper.map(p.getDestUserEntity(), UserDto.class))
                        .comment(p.getComment())
                        .dateCreated(p.getDateCreated())
                        .noteType(p.getNoteType().getName())
                        .dateReminder(p.getDateReminder())
                        .reminderStatus(p.getNoteStatusEntity() != null ? p.getNoteStatusEntity().getName() : null)
                        .build())
                .toList();
    }

    @Override
    public NoteDto create(NoteDto payload) {
        UserEntity userEntity = userRepository.findByUsernameIgnoreCase(UserUtils.getUsernameLogged())
                .orElseThrow(() -> new GenericException(HttpStatus.NOT_FOUND, String.format("Username %s not found", UserUtils.getUsernameLogged()), String.format("Username %s not found", UserUtils.getUsernameLogged())));

        NoteTypeEntity noteTypeEntity = noteTypeRepository.findByName(payload.getNoteType())
                .orElseThrow(() -> new GenericException(HttpStatus.NOT_FOUND, String.format("Type %s not found", payload.getNoteType()), String.format("Type %s not found", payload.getNoteType())));

        NoteEntity noteEntity = NoteEntity.builder()
//                .id()
                .authorUserEntity(userEntity)
                .comment(payload.getComment())
                .dateCreated(LocalDateTime.now())
                .noteType(noteTypeEntity)
                .dateReminder(payload.getNoteType().equals("Recordatorio") ? payload.getDateCreated() : null)
                .build();

        if (payload.getDestinatario() != null && payload.getDestinatario().getId() != null) {
            UserEntity destUserEntity = userRepository.findById(payload.getDestinatario().getId())
                    .orElseThrow(() -> new GenericException(HttpStatus.NOT_FOUND, String.format("User id %s not found", payload.getDestinatario().getId()), String.format("User id %s not found", payload.getDestinatario().getId())));

            noteEntity.setDestUserEntity(destUserEntity);
        }

        if (payload.getReminderStatus() != null) {
            NoteStatusEntity noteStatusEntity = noteStatusEntityRepository.findByName(payload.getReminderStatus())
                    .orElseThrow(() -> new GenericException(HttpStatus.NOT_FOUND, String.format("ReminderStatus id %s not found", payload.getDestinatario().getId()), String.format("ReminderStatus id %s not found", payload.getDestinatario().getId())));

            noteEntity.setNoteStatusEntity(noteStatusEntity);
        }

        noteEntity = noteRepository.save(noteEntity);

        return NoteDto.builder()
                .id(noteEntity.getId())
                .author(mapper.map(noteEntity.getAuthorUserEntity(), UserDto.class))
                .destinatario(mapper.map(noteEntity.getDestUserEntity(), UserDto.class))
                .comment(noteEntity.getComment())
                .dateCreated(noteEntity.getDateCreated())
                .noteType(noteEntity.getNoteType().getName())
                .dateReminder(noteEntity.getDateReminder())
                .reminderStatus(noteEntity.getNoteStatusEntity() != null ? noteEntity.getNoteStatusEntity().getName() : null)
                .build();
    }

    @Override
    public NoteDto update(Integer id, NoteDto payload) {
        NoteEntity noteEntity = noteRepository.findById(id)
                .orElseThrow(() -> new GenericException(HttpStatus.NOT_FOUND, String.format("Note id %s not found", id), String.format("Note id %s not found", id)));

        NoteTypeEntity noteTypeEntity = noteTypeRepository.findByName(payload.getNoteType())
                .orElseThrow(() -> new GenericException(HttpStatus.NOT_FOUND, String.format("Type %s not found", UserUtils.getUsernameLogged()), String.format("Type %s not found", payload.getNoteType())));

        if (payload.getDateCreated() != null) {
            noteEntity.setDateReminder(payload.getDateCreated());
        }

        if (payload.getDestinatario() != null && payload.getDestinatario().getId() != null) {
            UserEntity destUserEntity = userRepository.findById(payload.getDestinatario().getId())
                    .orElseThrow(() -> new GenericException(HttpStatus.NOT_FOUND, String.format("User id %s not found", payload.getDestinatario().getId()), String.format("User id %s not found", payload.getDestinatario().getId())));

            noteEntity.setDestUserEntity(destUserEntity);
        }

        noteEntity.setComment(payload.getComment());
        noteEntity.setNoteType(noteTypeEntity);
        noteEntity.setDateReminder(payload.getDateReminder());

        return NoteDto.builder()
                .id(noteEntity.getId())
                .author(mapper.map(noteEntity.getAuthorUserEntity(), UserDto.class))
                .destinatario(mapper.map(noteEntity.getDestUserEntity(), UserDto.class))
                .comment(noteEntity.getComment())
                .dateCreated(noteEntity.getDateCreated())
                .noteType(noteEntity.getNoteType().getName())
                .dateReminder(noteEntity.getDateReminder())
                .reminderStatus(noteEntity.getNoteStatusEntity() != null ? noteEntity.getNoteStatusEntity().getName() : null)
                .build();
    }

    @Override
    public List<NameDescriptionObj> getAllNoteTypes() {
        return noteTypeRepository.findAll()
                .stream()
                .map(p -> mapper.map(p, NameDescriptionObj.class))
                .toList();
    }

}
