package cl.isosalud.service.entity;

import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
@Table(name = "rel_treatment_appointment")
@Entity
public class RelTreatmentAppointmentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rel_treatment_appointment_id", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "appointment_id")
    private AppointmentEntity appointment;

    @ManyToOne
    @JoinColumn(name = "treatment_id")
    private TreatmentEntity treatment;

}