package cl.isosalud.service.service.role;

import cl.isosalud.service.dto.RoleDto;
import cl.isosalud.service.entity.RoleEntity;

import java.util.List;

public interface RoleService {
    RoleDto save(RoleEntity roleEntity);

    RoleDto getById(int id);

    List<RoleDto> getAll();
}
