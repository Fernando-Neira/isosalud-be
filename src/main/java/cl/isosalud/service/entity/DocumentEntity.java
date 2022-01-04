package cl.isosalud.service.entity;

import cl.isosalud.service.exception.GenericException;
import lombok.*;

import javax.persistence.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
@Entity
@Table(name = "documents")
public class DocumentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "document_id", nullable = false)
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "medic_user_id", nullable = false)
    private UserEntity medicUser;

    @ManyToOne(optional = false)
    @JoinColumn(name = "patient_user_id", nullable = false)
    private UserEntity patientUser;

    @Column(name = "name")
    private String name;

    @Column(name = "mime_type")
    private String mimeType;

    @Column(name = "size")
    private Long size;

    @Column(name = "hash")
    private String hash;

    @Column(name = "upload_hash")
    private String uploadHash;

    @Column(name = "collection_name")
    private String collectionName;

    @Column(name = "date")
    private LocalDateTime date = LocalDateTime.now();

    public void setHash() {
        try {
            String transformedName = this.name + this.mimeType + this.size + new Date().getTime();
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(transformedName.getBytes(StandardCharsets.UTF_8));
            this.hash = new BigInteger(1, messageDigest.digest()).toString(Character.MAX_RADIX);
        } catch (NoSuchAlgorithmException e) {
            throw new GenericException("ERROR", List.of("ERROR"));
        }
    }

    @PrePersist
    void preInsert() {
        final LocalDateTime now = LocalDateTime.now();

        if (this.date == null) {
            this.date = now;
        }
    }

}