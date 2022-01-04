package cl.isosalud.service.repository;

import cl.isosalud.service.entity.ContactMeanEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface ContactMeanRepository extends PagingAndSortingRepository<ContactMeanEntity, Integer> {

    Optional<ContactMeanEntity> findByName(String name);

    List<ContactMeanEntity> findAll();

}