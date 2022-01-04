package cl.isosalud.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class TreatmentDto {

    private Integer id;
    private UserDto medic;
    private UserDto patient;
    private String comment;
    private TreatmentSpecializationDto specialization;
    private TreatmentStateDto state;
    private List<ClinicalProcessDto> processes;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateCreated;
    private String typeOdontograma;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime startDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime lastMeeting;
    private List<AppointmentDto> appointments;

}