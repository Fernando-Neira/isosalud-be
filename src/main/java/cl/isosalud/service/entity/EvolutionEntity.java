package cl.isosalud.service.entity;

import lombok.*;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
@Entity
@Table(name = "evolutions")
public class EvolutionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "evolution_id", nullable = false)
    private Integer id;

    @Column(name = "date_created", nullable = false)
    private LocalDateTime dateCreated;

    @ManyToOne
    @JoinColumn(name = "author_user_id")
    private UserEntity authorUser;

    @ManyToOne
    @JoinColumn(name = "patient_user_id")
    private UserEntity patientUser;

    @Column(name = "comment", nullable = false)
    private String comment;
}