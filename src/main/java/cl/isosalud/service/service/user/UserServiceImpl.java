package cl.isosalud.service.service.user;

import cl.isosalud.service.dto.*;
import cl.isosalud.service.entity.*;
import cl.isosalud.service.exception.GenericException;
import cl.isosalud.service.mapping.Mapper;
import cl.isosalud.service.repository.*;
import cl.isosalud.service.security.CustomUser;
import cl.isosalud.service.service.person.PersonServiceImpl;
import cl.isosalud.service.util.RutUtils;
import cl.isosalud.service.util.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {

    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private final CommuneRepository communeRepository;
    private final PersonRepository personRepository;
    private final RoleRepository roleRepository;
    private final ContactMeanRepository contactMeanRepository;
    private final GenderRepository genderRepository;
    private final PasswordEncoder passwordEncoder;
    private final PersonServiceImpl personService;
    private final PrevisionRepository previsionRepository;
    private final Mapper mapper;
    private final UserStateRepository userStateRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> userOpt = findUserByUsername(username);

        if (userOpt.isEmpty()) {
            //TODO: Generate Custom Exception
            log.error("User not found {}", username);
            throw new UsernameNotFoundException("User not found");
        }

        UserEntity userEntity = userOpt.get();

        userEntity.setLastLogin(LocalDateTime.now());

        return new CustomUser(
                userEntity.getUsername(),
                userEntity.getPassword(),
                List.of(new SimpleGrantedAuthority(userEntity.getRoleEntity().getName())),
                userEntity.getPersonEntity().getFirstName(),
                userEntity.getPersonEntity().getLastName(),
                userEntity.getUserStateEntity().getName()
        );
    }

    public Optional<UserEntity> findUserByUsername(String username) {
        return userRepository.findByUsernameIgnoreCase(username);
    }

    @Override
    public UserDto save(UserDto payload) {
        log.info("Saving new user {}", payload);

        if (personRepository.findByRutIgnoreCase(payload.getPersonInfo().getRut()).isPresent()) {
            throw new UsernameNotFoundException(String.format("RUT %s, already exist.", payload.getPersonInfo().getRut()));
        }

        String username = getUsername(payload);

        payload.setUsername(username);
        payload.setPassword(username);

        if (findUserByUsername(payload.getUsername()).isPresent()) {
            throw new UsernameNotFoundException(String.format("Username %s, already exist.", payload.getUsername()));
        }

        RoleEntity roleEntity = roleRepository.findByName(payload.getRoleName().contains("ROLE_") ? payload.getRoleName() : "ROLE_".concat(payload.getRoleName()))
                .orElseThrow(() -> new UsernameNotFoundException(String.format("Role %s, doesn't exist.", payload.getRoleName())));

        ContactMeanEntity contactMeanEntity = contactMeanRepository.findByName(payload.getPreferredContactMeanName())
                .orElseThrow(() -> new UsernameNotFoundException(String.format("ContactMean %s, doesn't exist.", payload.getPreferredContactMeanName())));

        UserStateEntity userStateEntity = userStateRepository.findByName(payload.getStatus())
                .orElseThrow(() -> new UsernameNotFoundException(String.format("UserState %s, doesn't exist.", payload.getStatus())));


        PersonEntity personEntity = personService.save(payload.getPersonInfo());
        UserEntity userEntity = mapper.map(payload, UserEntity.class);

        userEntity.setPersonEntity(personEntity);
        userEntity.setRoleEntity(roleEntity);
        userEntity.setPreferredContactMeanEntity(contactMeanEntity);
        userEntity.setPassword(passwordEncoder.encode(payload.getPassword()));
        userEntity.setUserStateEntity(userStateEntity);


        return mapper.map(userRepository.save(userEntity), UserDto.class);
    }

    @Override
    public UserDto edit(int userId, UserDto payload) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new GenericException(HttpStatus.NOT_FOUND, String.format("User id %s not found", userId), String.format("User id %s not found", userId)));

        RoleEntity roleEntity = roleRepository.findByName(payload.getRoleName().contains("ROLE_") ? payload.getRoleName() : "ROLE_".concat(payload.getRoleName()))
                .orElseThrow(() -> new UsernameNotFoundException(String.format("Role %s, doesn't exist.", payload.getRoleName())));

        ContactMeanEntity contactMeanEntity = contactMeanRepository.findByName(payload.getPreferredContactMeanName())
                .orElseThrow(() -> new UsernameNotFoundException(String.format("ContactMean %s, doesn't exist.", payload.getPreferredContactMeanName())));

        UserStateEntity userStateEntity = userStateRepository.findByName(payload.getStatus())
                .orElseThrow(() -> new UsernameNotFoundException(String.format("UserState %s, doesn't exist.", payload.getStatus())));

        userEntity.setRoleEntity(roleEntity);
        userEntity.setPreferredContactMeanEntity(contactMeanEntity);
        userEntity.setUserStateEntity(userStateEntity);

        // Set new address
        userEntity.getPersonEntity().getAddressEntity().setAddress(payload.getPersonInfo().getAddressInfo().getStreet());

        // Set new cellphone
        userEntity.getPersonEntity().setCellphone(payload.getPersonInfo().getCellphone());

        // Set new commune
        CommuneEntity communeEntity = communeRepository.findByName(payload.getPersonInfo().getAddressInfo().getCommune())
                .orElseThrow(() -> new UsernameNotFoundException(String.format("Commune %s, doesn't exist.", payload.getPersonInfo().getAddressInfo().getCommune())));
        userEntity.getPersonEntity().getAddressEntity().setCommuneEntity(communeEntity);

        // Set new DateOfBirth
        userEntity.getPersonEntity().setDateOfBirth(payload.getPersonInfo().getDateOfBirth());

        // Set new email
        userEntity.getPersonEntity().setEmail(payload.getPersonInfo().getEmail());

        // Set new firstName
        userEntity.getPersonEntity().setFirstName(payload.getPersonInfo().getFirstName());

        // Set new lastName
        userEntity.getPersonEntity().setLastName(payload.getPersonInfo().getLastName());

        // Set new gender
        GenderEntity genderEntity = genderRepository.findByName(payload.getPersonInfo().getGender())
                .orElseThrow(() -> new UsernameNotFoundException(String.format("Gender %s, doesn't exist.", payload.getPersonInfo().getGender())));
        userEntity.getPersonEntity().setGenderEntity(genderEntity);

        // Set new phone
        userEntity.getPersonEntity().setPhone(payload.getPersonInfo().getPhone());

        // Set new cellphone
        userEntity.getPersonEntity().setCellphone(payload.getPersonInfo().getCellphone());

        // Set new prevision
        PrevisionEntity previsionEntity = previsionRepository.findByName(payload.getPersonInfo().getPrevision())
                .orElseThrow(() -> new UsernameNotFoundException(String.format("Prevision %s, doesn't exist.", payload.getPersonInfo().getPrevision())));
        userEntity.getPersonEntity().setPrevisionEntity(previsionEntity);

        // Set new rut??
//        userEntity.getPersonEntity().setRut();


        return mapper.map(userRepository.save(userEntity), UserDto.class);
    }

    @Override
    public List<NameDescriptionObj> getUserStates() {
        return userStateRepository.findAll()
                .stream()
                .map(state -> mapper.map(state, NameDescriptionObj.class))
                .toList();
    }

    @Override
    public UserDto changePassword(ChangePasswordDto request) {
        String username = UserUtils.getUsernameLogged();

        UserEntity userEntity = userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new GenericException(HttpStatus.NOT_FOUND, String.format("User %s not found", username), String.format("User %s not found", username)));

        userEntity.setPassword(passwordEncoder.encode(request.getPassword()));

        return mapper.map(userEntity, UserDto.class);
    }

    @Override
    public boolean addRoleToUser(String username, String roleName) {
        log.info("Adding new role {} to user {}", roleName, username);

        Optional<UserEntity> user = userRepository.findByUsernameIgnoreCase(username);

        if (user.isEmpty()) {
            //TODO: Generate Custom Exception
            return false;
        }

        Optional<RoleEntity> role = roleRepository.findByName(roleName);

        if (role.isEmpty()) {
            //TODO: Generate Custom Exception
            return false;
        }

        user.get().setRoleEntity(role.get());

        return true;
    }

    @Override
    public UserDto getUser(String username) {
        log.info("Getting user {}", username);

        UserEntity userEntity = userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new GenericException(HttpStatus.NOT_FOUND, String.format("User %s not found", username), String.format("User %s not found", username)));

        UserDto userDto = mapper.map(userEntity, UserDto.class);

        appointmentRepository
                .findLastAppointment(userDto.getId())
                .ifPresent(appointmentEntity -> userDto.setNextMeeting(appointmentEntity.getDateStart()));


        return userDto;
    }

    @Override
    public List<UserDto> getUsers() {
        log.info("Getting all users");
        return userRepository.findAll()
                .stream()
                .map(userEntity -> mapper.map(userEntity, UserDto.class))
                .peek(userDto -> {
                    appointmentRepository.findLastAppointment(userDto.getId())
                            .ifPresent(appointmentEntity -> userDto.setNextMeeting(appointmentEntity.getDateStart()));
                })
                .toList();
    }

    @Override
    public UserDto getUserLogged() {
        return getUser(UserUtils.getUsernameLogged());
    }

    @Override
    public PrevalidateUserDto prevalidateRut(UserDto userDto) {
        RutValidationDto rutValidationDto = RutUtils.validaRut(userDto.getPersonInfo().getRut());
        if (!rutValidationDto.isDvValid() || !rutValidationDto.isFormatValid()) {
            return PrevalidateUserDto.builder()
                    .statusCode("NOK")
                    .errorMsg(!rutValidationDto.isFormatValid() ? "RUT no valido, debe ser en formato 12345678-9" : "RUT ingresado no es valido")
                    .build();
        }

        if (personRepository.findByRutIgnoreCase(userDto.getPersonInfo().getRut()).isPresent()) {
            return PrevalidateUserDto.builder()
                    .statusCode("NOK")
                    .errorMsg("RUT ya se encuentra registrado")
                    .build();
        }

        String username = getUsername(userDto);

        return PrevalidateUserDto.builder()
                .statusCode("OK")
                .username(username)
                .build();
    }

    private String getUsername(UserDto userDto) {
        String username = String.valueOf(userDto.getPersonInfo().getFirstName().charAt(0)).toLowerCase() + userDto.getPersonInfo().getLastName().split(" ")[0].toLowerCase();

        List<UserEntity> users = userRepository.findByUsernameContainingIgnoreCase(username);

        if (users.isEmpty()) {
            return username;
        }

        List<String> usernamesStr = users.stream().map(UserEntity::getUsername).sorted().toList();
        boolean isCorrect = false;
        int i = 1;

        do {
            String number = String.valueOf(i);
            username = username + (number.length() == 1 ? "0" + number : number);
            if (usernamesStr.contains(username)) {
                i++;
                continue;
            }

            isCorrect = true;
        } while (!isCorrect);

        return username;
    }

    @Override
    public UserDto changeUserStatus(int userId, String newStatus) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new GenericException(HttpStatus.NOT_FOUND, String.format("User id %s not found", userId), String.format("User id %s not found", userId)));

        UserStateEntity userStateEntity = userStateRepository.findByName(newStatus)
                .orElseThrow(() -> new GenericException(HttpStatus.NOT_FOUND, String.format("Status %s not found", newStatus), String.format("Status %s not found", newStatus)));

        userEntity.setUserStateEntity(userStateEntity);

        return mapper.map(userRepository.save(userEntity), UserDto.class);
    }

}
