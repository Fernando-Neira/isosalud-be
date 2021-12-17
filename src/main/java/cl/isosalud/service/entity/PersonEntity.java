package cl.isosalud.service.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity(name = "persons")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class PersonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "person_id")
    private Integer id;

    @Column(name = "rut", length = 10)
    private String rut;

    @Column(name = "first_name", length = 40)
    private String firstName;

    @Column(name = "last_name", length = 40)
    private String lastName;

    @Column(name = "email", length = 80)
    private String email;

    @Column(name = "phone")
    private Integer phone;

    @Column(name = "cellphone")
    private Integer cellphone;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "date_created")
    private LocalDateTime dateCreated;

    @Column(name = "date_updated")
    private LocalDateTime dateUpdated;

    @OneToOne
    @JoinColumn(name = "gender_id")
    private GenderEntity genderEntity;

    @OneToOne
    @JoinColumn(name = "prevision_id")
    private PrevisionEntity previsionEntity;

    @OneToOne
    @JoinColumn(name = "address_id")
    private AddressEntity addressEntity;

    @PrePersist
    void preInsert() {
        final LocalDateTime now = LocalDateTime.now();

        if (this.dateCreated == null) {
            this.dateCreated = now;
        }

        this.dateUpdated = now;
    }

}