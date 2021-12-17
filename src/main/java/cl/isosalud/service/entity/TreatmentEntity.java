package cl.isosalud.service.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@Table(name = "treatments")
@Entity
public class TreatmentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "treatment_id", nullable = false)
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "patient_user_id", nullable = false)
    private UserEntity patientUser;

    @ManyToOne(optional = false)
    @JoinColumn(name = "medic_user_id", nullable = false)
    private UserEntity medicUser;

    @Column(name = "comment")
    private String comment;

    @ManyToOne
    @JoinColumn(name = "specialization_id")
    private TreatmentSpecializationEntity treatmentSpecializationEntity;

    @ManyToOne
    @JoinColumn(name = "state_id")
    private TreatmentStateEntity treatmentState;

    @Column(name = "date_created")
    private LocalDateTime dateCreated = LocalDateTime.now();

    @Column(name = "type_odontograma")
    private String typeOdontograma;

    @PrePersist
    void preInsert() {
        final LocalDateTime now = LocalDateTime.now();

        if (this.dateCreated == null) {
            this.dateCreated = now;
        }
    }

}