package cl.isosalud.service.entity;

import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
@Table(name = "addresses")
@Entity
public class AddressEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "commune_id")
    private CommuneEntity communeEntity;

    @Column(name = "address", nullable = false)
    private String address;

}