package cl.isosalud.service.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @OneToOne
    @JoinColumn(name = "person_id")
    private PersonEntity personEntity;

    @OneToOne
    @JoinColumn(name = "role_id")
    private RoleEntity roleEntity;

    @Column(name = "date_created")
    private LocalDateTime dateCreated = LocalDateTime.now();

    @OneToOne
    @JoinColumn(name = "preferred_contact_mean_id")
    private ContactMeanEntity preferredContactMeanEntity;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    @Column(name = "profile_img_uri")
    private String profileImgUri;

    @OneToOne
    @JoinColumn(name = "user_state_id")
    private UserStateEntity userStateEntity;

    @PrePersist
    void preInsert() {
        final LocalDateTime now = LocalDateTime.now();

        if (this.dateCreated == null) {
            this.dateCreated = now;
        }
    }

}