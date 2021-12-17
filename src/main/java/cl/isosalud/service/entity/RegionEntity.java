package cl.isosalud.service.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Table(name = "regions")
@Entity
public class RegionEntity {
    @Id
    @Column(name = "region_id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "roman_number")
    private String romanNumber;

    @Column(name = "abbreviation")
    private String abbreviation;

}