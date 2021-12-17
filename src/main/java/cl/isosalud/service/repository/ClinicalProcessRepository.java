package cl.isosalud.service.repository;

import cl.isosalud.service.entity.ClinicalProcessEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ClinicalProcessRepository extends PagingAndSortingRepository<ClinicalProcessEntity, Integer> {

    List<ClinicalProcessEntity> findAll();

}