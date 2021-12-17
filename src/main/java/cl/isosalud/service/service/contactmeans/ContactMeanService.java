package cl.isosalud.service.service.contactmeans;

import cl.isosalud.service.dto.ContactMeanDto;
import cl.isosalud.service.dto.RoleDto;
import cl.isosalud.service.entity.ContactMeanEntity;
import cl.isosalud.service.entity.RoleEntity;

import java.util.List;

public interface ContactMeanService {
    ContactMeanDto save(ContactMeanEntity contactMeanEntity);
    ContactMeanDto getById(int id);
    List<ContactMeanDto> getAll();
}
