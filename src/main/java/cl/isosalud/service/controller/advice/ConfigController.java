package cl.isosalud.service.controller.advice;

import cl.isosalud.service.dto.ConfigurationDto;
import cl.isosalud.service.exception.GenericException;
import cl.isosalud.service.service.configuration.ConfigurationService;
import cl.isosalud.service.dto.ResponseListWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/config")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class ConfigController {

    private final ConfigurationService configurationService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(path = "")
    public ResponseListWrapper<List<ConfigurationDto>> getConfigurations() {
        return new ResponseListWrapper<>(configurationService.getAll());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(path = "/search")
    public ConfigurationDto getConfiguration(@RequestParam String key) {
        return configurationService.getKey(key)
                .orElseThrow(() -> new GenericException(HttpStatus.NOT_FOUND, String.format("Key %s not found", key), String.format("Key %s not found", key)));
    }


}
