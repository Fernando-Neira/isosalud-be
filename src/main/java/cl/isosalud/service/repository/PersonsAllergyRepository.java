package cl.isosalud.service.repository;

import cl.isosalud.service.entity.PersonsAllergyEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PersonsAllergyRepository extends PagingAndSortingRepository<PersonsAllergyEntity, Integer> {
}