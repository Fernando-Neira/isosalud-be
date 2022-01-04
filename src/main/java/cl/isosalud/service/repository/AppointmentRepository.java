package cl.isosalud.service.repository;

import cl.isosalud.service.entity.AppointmentEntity;
import cl.isosalud.service.entity.TreatmentEntity;
import cl.isosalud.service.entity.UserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AppointmentRepository extends PagingAndSortingRepository<AppointmentEntity, Integer> {

    List<AppointmentEntity> findAll();

    List<AppointmentEntity> findAllByMedicUserEntity(UserEntity medicUserEntity);

    List<AppointmentEntity> findAllByPatientUserEntity(UserEntity patientUserEntity);

    List<AppointmentEntity> findAllByTreatmentEntity(TreatmentEntity treatmentEntity);

    @Query(
            nativeQuery = true,
            value = "SELECT * from appointments a WHERE a.patient_user_id = :userId ORDER BY a.date_start desc LIMIT 1"
    )
    Optional<AppointmentEntity> findLastAppointment(@Param("userId") int userId);

    @Query(
            nativeQuery = true,
            value = "SELECT * FROM appointments a WHERE a.date_start\\:\\:date = '2021-12-15' AND a.date_start\\:\\:time BETWEEN :startDate AND :endDate"
    )
    List<AppointmentEntity> findAllAppointmentsInHour(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

}