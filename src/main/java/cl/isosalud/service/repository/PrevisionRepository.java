package cl.isosalud.service.repository;

import cl.isosalud.service.entity.BoxEntity;
import cl.isosalud.service.entity.GenderEntity;
import cl.isosalud.service.entity.PrevisionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PrevisionRepository extends JpaRepository<PrevisionEntity, Integer> {

    List<PrevisionEntity> findAll();
    Optional<PrevisionEntity> findByName(String name);

}