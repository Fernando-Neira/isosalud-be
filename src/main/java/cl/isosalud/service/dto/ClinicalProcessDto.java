package cl.isosalud.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class ClinicalProcessDto {

    private int id;
    private String name;
    private String description;
    private NameDescriptionObj type;
    private BigDecimal price;
    private String pieces;
    private String paymentStatus;

}