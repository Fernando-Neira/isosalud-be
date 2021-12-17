package cl.isosalud.service.service.person;

import cl.isosalud.service.dto.PersonDto;
import cl.isosalud.service.entity.*;
import cl.isosalud.service.mapping.Mapper;
import cl.isosalud.service.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PersonServiceImpl implements PersonService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final PersonRepository personRepository;
    private final RoleRepository roleRepository;
    private final GenderRepository genderRepository;
    private final ContactMeanRepository contactMeanRepository;
    private final PasswordEncoder passwordEncoder;
    private final PrevisionRepository previsionRepository;
    private final CommuneRepository communeRepository;
    private final Mapper mapper;

    @Override
    public PersonEntity save(PersonDto personDto) {
        log.info("Saving new person {}", personDto);

        GenderEntity genderEntity = genderRepository.findByName(personDto.getGender())
                .orElseThrow(() -> new UsernameNotFoundException(String.format("Gender %s, doesn't exist.", personDto.getGender())));

        PrevisionEntity previsionEntity = previsionRepository.findByName(personDto.getPrevision())
                .orElseThrow(() -> new UsernameNotFoundException(String.format("Prevision %s, doesn't exist.", personDto.getPrevision())));

        CommuneEntity communeEntity = communeRepository.findByName(personDto.getAddressInfo().getCommune())
                .orElseThrow(() -> new UsernameNotFoundException(String.format("Commune %s, doesn't exist.", personDto.getAddressInfo().getCommune())));

        AddressEntity addressEntity = AddressEntity.builder()
                .address(personDto.getAddressInfo().getStreet())
                .communeEntity(communeEntity)
                .build();

        addressEntity = addressRepository.save(addressEntity);

        PersonEntity personEntity = mapper.map(personDto, PersonEntity.class);
        personEntity.setGenderEntity(genderEntity);
        personEntity.setPrevisionEntity(previsionEntity);
        personEntity.setAddressEntity(addressEntity);

        return personRepository.save(personEntity);
    }

}
