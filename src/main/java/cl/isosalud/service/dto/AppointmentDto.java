package cl.isosalud.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class AppointmentDto {

    private int id;
    private String title;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime startDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime endDate;
    private NameDescriptionObj box;
    private String comment;
    private UserDto patient;
    private UserDto medic;
    private NameDescriptionObj status;
    private NameDescriptionObj type;
    private TreatmentDto treatment;

}