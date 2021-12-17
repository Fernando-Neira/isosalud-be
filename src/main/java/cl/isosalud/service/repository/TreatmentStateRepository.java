package cl.isosalud.service.repository;

import cl.isosalud.service.entity.TreatmentStateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TreatmentStateRepository extends JpaRepository<TreatmentStateEntity, Integer> {

    Optional<TreatmentStateEntity> findByName(String name);

}