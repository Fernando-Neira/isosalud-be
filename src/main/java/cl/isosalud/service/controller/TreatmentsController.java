package cl.isosalud.service.controller;

import cl.isosalud.service.dto.*;
import cl.isosalud.service.service.treatment.TreatmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/treatments", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TreatmentsController {

    //TODO: Do this!!!!
    private final TreatmentService treatmentService;

    @PreAuthorize("hasAnyRole('ROLE_DENTIST', 'ROLE_ADMIN')")
    @GetMapping(path = "")
    public ResponseListWrapper<List<TreatmentDto>> getAll() {
        return new ResponseListWrapper<>(treatmentService.getAll());
    }

    @PreAuthorize("hasAnyRole('ROLE_DENTIST', 'ROLE_ADMIN')")
    @GetMapping(path = "/search")
    public TreatmentDto getById(@RequestParam Integer id) {
        return treatmentService.getById(id);
    }

    @PreAuthorize("hasAnyRole('ROLE_DENTIST', 'ROLE_ADMIN', 'ROLE_PATIENT')")
    @GetMapping(path = "/search-by-patient")
    public ResponseListWrapper<List<TreatmentDto>> getByPatientId(@RequestParam Integer id) {
        return new ResponseListWrapper<>(treatmentService.getByPatientId(id));
    }

    @PreAuthorize("hasAnyRole('ROLE_DENTIST', 'ROLE_ADMIN')")
    @PostMapping(path = "")
    public TreatmentDto create(@RequestBody TreatmentDto payload) {
        return treatmentService.create(payload);
    }

    @PreAuthorize("hasAnyRole('ROLE_DENTIST', 'ROLE_ADMIN')")
    @PostMapping(path = "/change-state")
    public TreatmentDto cancel(@RequestParam Integer id, @RequestParam String state) {
        return treatmentService.changeState(id, state);
    }

    @PreAuthorize("hasAnyRole('ROLE_DENTIST', 'ROLE_ADMIN')")
    @GetMapping(path = "/clinic-process")
    public ResponseListWrapper<List<ClinicalProcessTypesDto>> getAllClinicalProcess() {
        return new ResponseListWrapper<>(treatmentService.getAllClinicalProcess());
    }

    @PreAuthorize("hasAnyRole('ROLE_DENTIST', 'ROLE_ADMIN')")
    @GetMapping(path = "/specializations")
    public ResponseListWrapper<List<TreatmentSpecializationDto>> getAllSpecializations() {
        return new ResponseListWrapper<>(treatmentService.getAllSpecializations());
    }

}
