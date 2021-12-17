package cl.isosalud.service.repository;

import cl.isosalud.service.entity.PaymentStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentStatusRepository extends JpaRepository<PaymentStatusEntity, Integer> {

    List<PaymentStatusEntity> findAll();
    Optional<PaymentStatusEntity> findByName(String name);

}