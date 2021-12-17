package cl.isosalud.service.repository;

import cl.isosalud.service.entity.PersonEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface PersonRepository extends PagingAndSortingRepository<PersonEntity, Integer> {

    Optional<PersonEntity> findByRutIgnoreCase(String rut);

}