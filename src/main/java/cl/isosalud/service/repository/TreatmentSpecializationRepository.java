package cl.isosalud.service.repository;

import cl.isosalud.service.entity.TreatmentSpecializationEntity;
import cl.isosalud.service.entity.TreatmentStateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TreatmentSpecializationRepository extends JpaRepository<TreatmentSpecializationEntity, Integer> {

    Optional<TreatmentSpecializationEntity> findByName(String name);

}