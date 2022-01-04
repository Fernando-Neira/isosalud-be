package cl.isosalud.service.entity;

import lombok.*;

import javax.persistence.*;

@Entity(name = "contact_means")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class ContactMeanEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contact_mean_id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false, length = 30)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "spanish_name")
    private String spanishName;

}