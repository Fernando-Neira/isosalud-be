package cl.isosalud.service.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
@Table(name = "notes")
@Entity
public class NoteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "note_id", nullable = false)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "author_user_id")
    private UserEntity authorUserEntity;

    @OneToOne
    @JoinColumn(name = "dest_user_id")
    private UserEntity destUserEntity;

    @Column(name = "comment", nullable = false)
    private String comment;

    @Column(name = "date_created", nullable = false)
    private LocalDateTime dateCreated;

    @ManyToOne
    @JoinColumn(name = "note_type_id")
    private NoteTypeEntity noteType;

    @Column(name = "date_reminder")
    private LocalDateTime dateReminder;

    @ManyToOne
    @JoinColumn(name = "note_status_id")
    private NoteStatusEntity noteStatusEntity;

    @PrePersist
    void preInsert() {
        final LocalDateTime now = LocalDateTime.now();

        if (this.dateCreated == null) {
            this.dateCreated = now;
        }
    }

}