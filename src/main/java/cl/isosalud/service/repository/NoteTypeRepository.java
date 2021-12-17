package cl.isosalud.service.repository;

import cl.isosalud.service.entity.NoteTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NoteTypeRepository extends JpaRepository<NoteTypeEntity, Integer> {

    Optional<NoteTypeEntity> findByName(String name);

}