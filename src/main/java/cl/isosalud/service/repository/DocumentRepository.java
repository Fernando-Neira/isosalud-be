package cl.isosalud.service.repository;

import cl.isosalud.service.entity.DocumentEntity;
import cl.isosalud.service.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentRepository extends JpaRepository<DocumentEntity, Integer> {

    List<DocumentEntity> findAllByPatientUser(UserEntity patientUser);

}