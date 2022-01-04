package cl.isosalud.service.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Table(name = "persons_allergies")
@Entity
public class PersonsAllergyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "person_allergy_id", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "person_id")
    private PersonEntity personEntity;

    @ManyToOne
    @JoinColumn(name = "allergy_id")
    private AllergyEntity allergyEntity;

}