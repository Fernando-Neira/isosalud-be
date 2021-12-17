package cl.isosalud.service.service.person;

import cl.isosalud.service.dto.PersonDto;
import cl.isosalud.service.entity.PersonEntity;

public interface PersonService {
    PersonEntity save(PersonDto personDto);
}
