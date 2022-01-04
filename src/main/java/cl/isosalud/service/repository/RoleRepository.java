package cl.isosalud.service.repository;

import cl.isosalud.service.entity.RoleEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends PagingAndSortingRepository<RoleEntity, Integer> {

    Optional<RoleEntity> findByName(String name);

    List<RoleEntity> findAll();

}