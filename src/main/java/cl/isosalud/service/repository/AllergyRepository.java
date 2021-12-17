package cl.isosalud.service.repository;

import cl.isosalud.service.entity.AllergyEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface AllergyRepository extends PagingAndSortingRepository<AllergyEntity, Integer> {
}