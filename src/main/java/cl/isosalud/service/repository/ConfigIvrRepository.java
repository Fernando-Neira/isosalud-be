package cl.isosalud.service.repository;

import cl.isosalud.service.entity.ConfigIvr;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface ConfigIvrRepository extends PagingAndSortingRepository<ConfigIvr, Integer> {

    Optional<ConfigIvr> findByKey(String key);

}