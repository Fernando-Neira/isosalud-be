package cl.isosalud.service.controller;

import cl.isosalud.service.dto.EvolutionDto;
import cl.isosalud.service.dto.GenericResponseDto;
import cl.isosalud.service.dto.ResponseListWrapper;
import cl.isosalud.service.service.evolutions.EvolutionsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/evolutions", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class EvolutionsController {

    //TODO: Do this!!!
    private final EvolutionsService evolutionsService;

    @PreAuthorize("hasAnyRole('ROLE_DENTIST', 'ROLE_ADMIN')")
    @GetMapping(path = "")
    public ResponseListWrapper<List<EvolutionDto>> getAllEvolutions() {
        return new ResponseListWrapper<>(evolutionsService.getAll());
    }

    @PreAuthorize("hasAnyRole('ROLE_DENTIST', 'ROLE_ADMIN')")
    @GetMapping(path = "/search")
    public EvolutionDto getEvolutionById(@RequestParam Integer id) {
        return evolutionsService.getById(id);
    }

    @PreAuthorize("hasAnyRole('ROLE_DENTIST', 'ROLE_ADMIN')")
    @GetMapping(path = "/search-patient")
    public ResponseListWrapper<List<EvolutionDto>> getPatientEvolutionDto(@RequestParam(name = "id") Integer patientId) {
        return new ResponseListWrapper<>(evolutionsService.getEvolutionByPatientId(patientId));
    }

    @PreAuthorize("hasAnyRole('ROLE_DENTIST', 'ROLE_ADMIN')")
    @PostMapping(path = "")
    public EvolutionDto createEvolution(@RequestBody EvolutionDto payload) {
        return evolutionsService.create(payload);
    }

    @PreAuthorize("hasAnyRole('ROLE_DENTIST', 'ROLE_ADMIN')")
    @PostMapping(path = "/delete")
    public GenericResponseDto updateEvolution(@RequestParam Integer id) {
        return evolutionsService.delete(id);
    }

}
