package cl.isosalud.service.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

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