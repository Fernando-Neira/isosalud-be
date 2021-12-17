package cl.isosalud.service.repository;

import cl.isosalud.service.entity.PersonEntity;
import cl.isosalud.service.entity.RoleEntity;
import cl.isosalud.service.entity.UserEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends PagingAndSortingRepository<UserEntity, Integer> {

    List<UserEntity> findAll();

    List<UserEntity> findAllByRoleEntity(RoleEntity roleEntity);

    Optional<UserEntity> findByUsernameIgnoreCase(String username);

    List<UserEntity> findByUsernameContainingIgnoreCase(String username);

    Optional<UserEntity> findByUsernameIgnoreCaseAndRoleEntity(String username, RoleEntity roleEntity);

    Optional<UserEntity> findByIdAndRoleEntity(int id, RoleEntity roleEntity);

    Optional<UserEntity> findByPersonEntity(PersonEntity personEntity);

}