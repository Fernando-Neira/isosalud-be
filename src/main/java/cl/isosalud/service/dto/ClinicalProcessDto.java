package cl.isosalud.service.dto;

import lombok.*;

import java.math.BigDecimal;

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