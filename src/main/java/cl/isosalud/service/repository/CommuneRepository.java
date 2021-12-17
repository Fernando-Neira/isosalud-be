package cl.isosalud.service.repository;

import cl.isosalud.service.entity.CommuneEntity;
import cl.isosalud.service.entity.RegionEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface CommuneRepository extends PagingAndSortingRepository<CommuneEntity, Integer> {

    List<CommuneEntity> findAll();
    List<CommuneEntity> findAllByRegion(RegionEntity regionEntity);
    Optional<CommuneEntity> findByName(String name);

}