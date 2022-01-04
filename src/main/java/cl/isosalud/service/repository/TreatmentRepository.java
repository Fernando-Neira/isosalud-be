package cl.isosalud.service.repository;

import cl.isosalud.service.entity.TreatmentEntity;
import cl.isosalud.service.entity.UserEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface TreatmentRepository extends PagingAndSortingRepository<TreatmentEntity, Integer> {

    List<TreatmentEntity> findAll();

    List<TreatmentEntity> findAllByPatientUser(UserEntity userEntity);

}