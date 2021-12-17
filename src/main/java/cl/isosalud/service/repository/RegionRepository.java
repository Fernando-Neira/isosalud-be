package cl.isosalud.service.repository;

import cl.isosalud.service.entity.RegionEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface RegionRepository extends PagingAndSortingRepository<RegionEntity, Integer> {
}