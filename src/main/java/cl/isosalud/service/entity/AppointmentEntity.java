package cl.isosalud.service.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
@Table(name = "appointments")
@Entity
public class AppointmentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "appointment_id", nullable = false)
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "patient_user_id", nullable = false)
    private UserEntity patientUserEntity;

    @ManyToOne(optional = false)
    @JoinColumn(name = "medic_user_id", nullable = false)
    private UserEntity medicUserEntity;

    @Column(name = "title")
    private String title;

    @Column(name = "comment")
    private String comment;

    @Column(name = "date_start", nullable = false)
    private LocalDateTime dateStart;

    @Column(name = "date_end", nullable = false)
    private LocalDateTime dateEnd;

    @Column(name = "date_created", nullable = false)
    private LocalDateTime dateCreated;

    @ManyToOne
    @JoinColumn(name = "treatment_id")
    private TreatmentEntity treatmentEntity;

    @ManyToOne(optional = false)
    @JoinColumn(name = "box_id", nullable = false)
    private BoxEntity box;

    @ManyToOne(optional = false)
    @JoinColumn(name = "appointment_state_id", nullable = false)
    private AppointmentStateEntity appointmentState;

    @ManyToOne(optional = false)
    @JoinColumn(name = "appointment_type_id", nullable = false)
    private AppointmentTypeEntity appointmentType;

    @PrePersist
    void preInsert() {
        final LocalDateTime now = LocalDateTime.now();

        if (this.dateCreated == null) {
            this.dateCreated = now;
        }
    }

}