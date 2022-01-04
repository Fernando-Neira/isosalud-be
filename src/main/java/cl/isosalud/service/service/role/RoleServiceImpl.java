package cl.isosalud.service.service.role;

import cl.isosalud.service.dto.RoleDto;
import cl.isosalud.service.entity.RoleEntity;
import cl.isosalud.service.exception.GenericException;
import cl.isosalud.service.mapping.Mapper;
import cl.isosalud.service.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final Mapper mapper;

    @Override
    public RoleDto save(RoleEntity roleEntity) {
        return mapper.map(roleRepository.save(roleEntity), RoleDto.class);
    }

    @Override
    public RoleDto getById(int id) {
        return mapper.map(roleRepository.findById(id).orElseThrow(() -> new GenericException(HttpStatus.NOT_FOUND, String.format("Role id %s not found", id), String.format("Role id %s not found", id))), RoleDto.class);
    }

    @Override
    public List<RoleDto> getAll() {
        return roleRepository.findAll()
                .stream()
                .map(r -> mapper.map(r, RoleDto.class))
                .toList();
    }

}
