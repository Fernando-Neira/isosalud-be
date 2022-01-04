package cl.isosalud.service.service.patient;

import cl.isosalud.service.dto.UserDto;
import cl.isosalud.service.entity.ContactMeanEntity;
import cl.isosalud.service.entity.PersonEntity;
import cl.isosalud.service.entity.RoleEntity;
import cl.isosalud.service.entity.UserEntity;
import cl.isosalud.service.exception.GenericException;
import cl.isosalud.service.mapping.Mapper;
import cl.isosalud.service.repository.*;
import cl.isosalud.service.service.person.PersonServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PatientServiceImpl implements PatientService {

    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private final PersonRepository personRepository;
    private final RoleRepository roleRepository;
    private final ContactMeanRepository contactMeanRepository;
    private final PasswordEncoder passwordEncoder;
    private final PersonServiceImpl personService;
    private final Mapper mapper;

    private RoleEntity patientRoleEntity;

    @PostConstruct
    private void init() {
        patientRoleEntity = roleRepository.findByName("ROLE_PATIENT")
                .orElseThrow(() -> new GenericException(HttpStatus.NOT_FOUND, "Role PATIENT not found", "Role PATIENT not found"));
    }

    @Override
    public UserDto save(UserDto userDto) {
        log.info("Saving new user {}", userDto);

        personRepository.findByRutIgnoreCase(userDto.getPersonInfo().getRut()).orElseThrow(() -> new UsernameNotFoundException(String.format("RUT %s, already exist.", userDto.getPersonInfo().getRut())));

        ContactMeanEntity contactMeanOpt = contactMeanRepository.findByName(userDto.getPreferredContactMeanName()).orElseThrow(() -> new UsernameNotFoundException(String.format("ContactMean %s, doesn't exist.", userDto.getPreferredContactMeanName())));

        PersonEntity personEntity = personService.save(userDto.getPersonInfo());
        UserEntity userEntity = mapper.map(userDto, UserEntity.class);

        userEntity.setPersonEntity(personEntity);
        userEntity.setRoleEntity(patientRoleEntity);
        userEntity.setPreferredContactMeanEntity(contactMeanOpt);
        userEntity.setPassword(passwordEncoder.encode(userDto.getPassword()));

        return mapper.map(userRepository.save(userEntity), UserDto.class);
    }


    @Override
    public UserDto getPatientById(int id) {
        log.info("Getting patient {}", id);

        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new GenericException(HttpStatus.NOT_FOUND, String.format("User %s not found", id), String.format("User %s not found", id)));

        UserDto userDto = mapper.map(userEntity, UserDto.class);

        appointmentRepository
                .findLastAppointment(userDto.getId())
                .ifPresent(appointmentEntity -> userDto.setNextMeeting(appointmentEntity.getDateStart()));

        return userDto;
    }

    @Override
    public List<UserDto> getPatients() {
        log.info("Getting all patients");

        return userRepository.findAll()
                .stream()
//                .filter(u -> !u.getRoleEntity().getName().equals("ROLE_ADMIN")) // Para filtrar que no salga el rol admin
                .map(userEntity -> mapper.map(userEntity, UserDto.class))
                .peek(userDto -> {
                    appointmentRepository.findLastAppointment(userDto.getId())
                            .ifPresent(appointmentEntity -> userDto.setNextMeeting(appointmentEntity.getDateStart()));
                })
                .toList();
    }

}
