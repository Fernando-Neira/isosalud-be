package cl.isosalud.service.repository;

import cl.isosalud.service.entity.BoxEntity;
import cl.isosalud.service.entity.UserEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;


public interface BoxRepository extends PagingAndSortingRepository<BoxEntity, Integer> {

    List<BoxEntity> findAll();

}