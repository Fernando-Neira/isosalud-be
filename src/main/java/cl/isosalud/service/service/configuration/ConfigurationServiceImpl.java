package cl.isosalud.service.service.configuration;

import cl.isosalud.service.dto.ConfigurationDto;
import cl.isosalud.service.entity.ConfigEntity;
import cl.isosalud.service.repository.ConfigRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ConfigurationServiceImpl implements ConfigurationService {

    private final ConfigRepository configRepository;

    public List<ConfigurationDto> getAll() {
        return configRepository.findAll().stream().map(ConfigurationDto::fromConfigEntity).toList();
    }

    public Optional<ConfigurationDto> getKey(String key) {
        return configRepository.findByKeyIgnoreCase(key).map(ConfigurationDto::fromConfigEntity);
    }

    public ConfigurationDto setKey(ConfigurationDto configuration) {
        return ConfigurationDto.fromConfigEntity(configRepository.save(ConfigEntity.builder()
                .key(configuration.getKey())
                .value(configuration.getValue())
                .description(configuration.getDescription())
                .build()));
    }

}
