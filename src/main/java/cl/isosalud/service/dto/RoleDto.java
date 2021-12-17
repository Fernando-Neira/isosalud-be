package cl.isosalud.service.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class RoleDto {

    private Integer id;
    private String name;
    private String description;
    private String spanishName;

}