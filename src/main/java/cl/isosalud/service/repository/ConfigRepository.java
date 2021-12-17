package cl.isosalud.service.repository;

import cl.isosalud.service.entity.ConfigEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConfigRepository extends JpaRepository<ConfigEntity, Integer> {

    Optional<ConfigEntity> findByKeyIgnoreCase(String key);

}