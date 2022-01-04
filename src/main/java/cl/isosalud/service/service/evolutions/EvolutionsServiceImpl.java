package cl.isosalud.service.service.evolutions;

import cl.isosalud.service.dto.EvolutionDto;
import cl.isosalud.service.dto.GenericResponseDto;
import cl.isosalud.service.dto.UserDto;
import cl.isosalud.service.entity.EvolutionEntity;
import cl.isosalud.service.entity.UserEntity;
import cl.isosalud.service.exception.GenericException;
import cl.isosalud.service.mapping.Mapper;
import cl.isosalud.service.repository.EvolutionRepository;
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
public class EvolutionsServiceImpl implements EvolutionsService {

    private final EvolutionRepository evolutionRepository;
    private final NoteTypeRepository noteTypeRepository;
    private final UserRepository userRepository;
    private final NoteStatusEntityRepository noteStatusEntityRepository;
    private final Mapper mapper;

    @Override
    public EvolutionDto getById(int evolutionId) {
        return evolutionRepository.findById(evolutionId)
                .map(this::map)
                .orElseThrow(() -> new GenericException(HttpStatus.NOT_FOUND, String.format("Evolution id %s not found", evolutionId), String.format("Evolution id %s not found", evolutionId)));
    }

    @Override
    public List<EvolutionDto> getEvolutionByPatientId(Integer patientId) {
        UserEntity userEntity = userRepository.findById(patientId)
                .orElseThrow(() -> new GenericException(HttpStatus.NOT_FOUND, String.format("Username %s not found", UserUtils.getUsernameLogged()), String.format("Username %s not found", UserUtils.getUsernameLogged())));

        return evolutionRepository.findAllByPatientUser(userEntity)
                .stream()
                .map(this::map)
                .toList();
    }

    @Override
    public GenericResponseDto delete(Integer evolutionId) {
        EvolutionEntity noteEntity = evolutionRepository.findById(evolutionId)
                .orElseThrow(() -> new GenericException(HttpStatus.NOT_FOUND, String.format("Evolution id %s not found", evolutionId), String.format("Evolution id %s not found", evolutionId)));

        try {
            evolutionRepository.delete(noteEntity);
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
    public List<EvolutionDto> getAll() {
        return evolutionRepository.findAll()
                .stream()
                .map(this::map)
                .toList();
    }

    @Override
    public EvolutionDto create(EvolutionDto payload) {
        UserEntity userEntity = userRepository.findByUsernameIgnoreCase(UserUtils.getUsernameLogged())
                .orElseThrow(() -> new GenericException(HttpStatus.NOT_FOUND, String.format("Username %s not found", UserUtils.getUsernameLogged()), String.format("Username %s not found", UserUtils.getUsernameLogged())));

        UserEntity patientUser = userRepository.findById(payload.getPatient().getId())
                .orElseThrow(() -> new GenericException(HttpStatus.NOT_FOUND, String.format("PatientId %s not found", payload.getPatient().getId()), String.format("PatientId %s not found", payload.getPatient().getId())));


        EvolutionEntity evolutionEntity = EvolutionEntity.builder()
//                .id()
                .authorUser(userEntity)
                .patientUser(patientUser)
                .comment(payload.getComment())
                .dateCreated(LocalDateTime.now())
                .build();

        evolutionEntity = evolutionRepository.save(evolutionEntity);

        return map(evolutionEntity);
    }

    private EvolutionDto map(EvolutionEntity target) {
        return EvolutionDto.builder()
                .id(target.getId())
                .dateCreated(target.getDateCreated())
                .medic(mapper.map(target.getAuthorUser(), UserDto.class))
                .patient(mapper.map(target.getPatientUser(), UserDto.class))
                .comment(target.getComment())
                .build();
    }

}
