package cl.isosalud.service.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class NameDescriptionObj {

    private Integer id;
    private String name;
    private String description;

}