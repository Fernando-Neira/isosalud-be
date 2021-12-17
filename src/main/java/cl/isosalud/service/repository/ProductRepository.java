package cl.isosalud.service.repository;

import cl.isosalud.service.entity.BoxEntity;
import cl.isosalud.service.entity.ProductEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ProductRepository extends PagingAndSortingRepository<ProductEntity, Integer> {

    List<ProductEntity> findAll();

}