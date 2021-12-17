package cl.isosalud.service.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@ToString
public class RutValidationDto {

    private boolean isFormatValid;
    private boolean isDvValid;

}
