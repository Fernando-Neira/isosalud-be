package cl.isosalud.service.repository;

import cl.isosalud.service.entity.ProductTypeEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ProductTypeRepository extends PagingAndSortingRepository<ProductTypeEntity, Integer> {

    List<ProductTypeEntity> findAll();

}