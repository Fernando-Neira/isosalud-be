package cl.isosalud.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class DocumentDto {

    private int id;
    private String name;
    private long size;
    private String mimeType;
    private String hash;
    private String uploadHash;
    private UserDto medicUser;
    private UserDto patientUser;
    private String collectionName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime date;
    private String downloadUrl;

}