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
@Table(name = "communes")
@Entity
public class CommuneEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "commune_id", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "region_id")
    private RegionEntity region;

    @Column(name = "name")
    private String name;
}