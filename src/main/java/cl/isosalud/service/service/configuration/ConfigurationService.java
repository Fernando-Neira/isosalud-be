package cl.isosalud.service.service.configuration;

import cl.isosalud.service.dto.ConfigurationDto;

import java.util.List;
import java.util.Optional;

public interface ConfigurationService {

    List<ConfigurationDto> getAll();
    Optional<ConfigurationDto> getKey(String key);
    ConfigurationDto setKey(ConfigurationDto configuration);

}
