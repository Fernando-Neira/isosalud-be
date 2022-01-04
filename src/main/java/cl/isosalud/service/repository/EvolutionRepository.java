package cl.isosalud.service.repository;

import cl.isosalud.service.entity.EvolutionEntity;
import cl.isosalud.service.entity.UserEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface EvolutionRepository extends PagingAndSortingRepository<EvolutionEntity, Integer> {

    List<EvolutionEntity> findAll();

    List<EvolutionEntity> findAllByPatientUser(UserEntity userEntity);

}