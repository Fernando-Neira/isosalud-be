package cl.isosalud.service.repository;

import cl.isosalud.service.entity.ProductsUsageEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ProductsUsageRepository extends PagingAndSortingRepository<ProductsUsageEntity, Integer> {
}