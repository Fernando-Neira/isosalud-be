package cl.isosalud.service.repository;

import cl.isosalud.service.entity.AppointmentStateEntity;
import cl.isosalud.service.entity.AppointmentTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppointmentTypeRepository extends JpaRepository<AppointmentTypeEntity, Integer> {

    Optional<AppointmentTypeEntity> findByNameIgnoreCase(String name);

}