package cl.isosalud.service.dto;

import cl.isosalud.service.entity.ConfigEntity;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class ConfigurationDto {

    private int id;
    private String key;
    private String value;
    private String description;

    public static ConfigurationDto fromConfigEntity(ConfigEntity configEntity) {
        return ConfigurationDto.builder()
                .id(configEntity.getId())
                .key(configEntity.getKey())
                .value(configEntity.getValue())
                .description(configEntity.getDescription())
                .build();
    }

}