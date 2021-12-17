package cl.isosalud.service.service.dentist;

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
public class DentistServiceImpl implements DentistService {

    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private final PersonRepository personRepository;
    private final RoleRepository roleRepository;
    private final ContactMeanRepository contactMeanRepository;
    private final PasswordEncoder passwordEncoder;
    private final PersonServiceImpl personService;
    private final Mapper mapper;

    private RoleEntity dentistRoleEntity;

    @PostConstruct
    private void init() {
        dentistRoleEntity = roleRepository.findByName("ROLE_DENTIST")
                .orElseThrow(() -> new GenericException(HttpStatus.NOT_FOUND, "Role ROLE_DENTIST not found", "Role ROLE_DENTIST not found"));
    }

    @Override
    public UserDto getDentistById(int id) {
        log.info("Getting dentist {}", id);

        UserEntity userEntity = userRepository.findByIdAndRoleEntity(id, dentistRoleEntity)
                .orElseThrow(() -> new GenericException(HttpStatus.NOT_FOUND, String.format("User %s not found", id), String.format("User %s not found", id)));

        UserDto userDto = mapper.map(userEntity, UserDto.class);

        appointmentRepository
                .findLastAppointment(userDto.getId())
                .ifPresent(appointmentEntity -> userDto.setNextMeeting(appointmentEntity.getDateStart()));

        return userDto;
    }

    @Override
    public List<UserDto> getDentist() {
        log.info("Getting all dentist");

        return userRepository.findAll()
                .stream()
                .filter(u -> u.getRoleEntity().getName().equals("ROLE_DENTIST") || u.getRoleEntity().getName().equals("ROLE_ADMIN")) // Para filtrar que solo salgan dentistas
                .map(userEntity -> mapper.map(userEntity, UserDto.class))
                .peek(userDto -> {
                    appointmentRepository.findLastAppointment(userDto.getId())
                            .ifPresent(appointmentEntity -> userDto.setNextMeeting(appointmentEntity.getDateStart()));
                })
                .toList();
    }

}
