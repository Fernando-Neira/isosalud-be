package cl.isosalud.service.repository;

import cl.isosalud.service.entity.NoteStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NoteStatusEntityRepository extends JpaRepository<NoteStatusEntity, Integer> {

    Optional<NoteStatusEntity> findByName(String name);

}