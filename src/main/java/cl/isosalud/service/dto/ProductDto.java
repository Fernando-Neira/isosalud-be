package cl.isosalud.service.dto;

import lombok.*;

import java.math.BigDecimal;
import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class ProductDto {

    private int id;
    private NameDescriptionObj productType;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer quantity;

}