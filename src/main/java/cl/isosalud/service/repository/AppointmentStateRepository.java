package cl.isosalud.service.repository;

import cl.isosalud.service.entity.AppointmentStateEntity;
import cl.isosalud.service.entity.PersonEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface AppointmentStateRepository extends PagingAndSortingRepository<AppointmentStateEntity, Integer> {

    Optional<AppointmentStateEntity> findByNameIgnoreCase(String name);

}