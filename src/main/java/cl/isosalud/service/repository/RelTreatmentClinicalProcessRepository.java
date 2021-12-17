package cl.isosalud.service.repository;

import cl.isosalud.service.entity.RelTreatmentClinicalProcessEntity;
import cl.isosalud.service.entity.TreatmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RelTreatmentClinicalProcessRepository extends JpaRepository<RelTreatmentClinicalProcessEntity, Integer> {

    List<RelTreatmentClinicalProcessEntity> findAllByTreatment(TreatmentEntity treatmentEntity);

}