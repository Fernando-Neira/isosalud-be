package cl.isosalud.service.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class TreatmentSpecializationDto {

    private int id;
    private String name;
    private String description;

}