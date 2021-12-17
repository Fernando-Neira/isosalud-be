package cl.isosalud.service.repository;

import cl.isosalud.service.entity.AddressEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface AddressRepository extends PagingAndSortingRepository<AddressEntity, Integer> {
}