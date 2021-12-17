package cl.isosalud.service.repository;

import cl.isosalud.service.entity.GenderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GenderRepository extends JpaRepository<GenderEntity, Integer> {

    Optional<GenderEntity> findByName(String name);

}