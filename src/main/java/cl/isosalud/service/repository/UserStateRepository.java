package cl.isosalud.service.repository;

import cl.isosalud.service.entity.UserStateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserStateRepository extends JpaRepository<UserStateEntity, Integer> {

    Optional<UserStateEntity> findByName(String name);

}