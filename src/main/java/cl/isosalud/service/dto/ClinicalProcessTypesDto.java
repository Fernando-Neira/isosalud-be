package cl.isosalud.service.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class ClinicalProcessTypesDto {

    private String name;
    private List<ClinicalProcessDto> processes;

}