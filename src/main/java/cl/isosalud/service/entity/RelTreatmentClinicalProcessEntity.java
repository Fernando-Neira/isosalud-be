package cl.isosalud.service.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
@Table(name = "rel_treatment_clinical_process")
@Entity
public class RelTreatmentClinicalProcessEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rel_treatment_clinicalprocess", nullable = false)
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "treatment_id", nullable = false)
    private TreatmentEntity treatment;

    @ManyToOne(optional = false)
    @JoinColumn(name = "clinical_process_id", nullable = false)
    private ClinicalProcessEntity clinicalProcess;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "payment_status_id")
    private PaymentStatusEntity paymentStatus;

    @Column(name = "dental_pieces")
    private String dentalPieces;

}