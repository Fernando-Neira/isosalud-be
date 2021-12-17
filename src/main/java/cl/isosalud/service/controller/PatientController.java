package cl.isosalud.service.controller;

import cl.isosalud.service.dto.ResponseListWrapper;
import cl.isosalud.service.dto.UserDto;
import cl.isosalud.service.service.patient.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/patient", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PatientController {

    private final PatientService patientService;

    @PreAuthorize("hasAnyRole('ROLE_DENTIST', 'ROLE_ADMIN')")
    @GetMapping(path = "")
    public ResponseListWrapper<List<UserDto>> getPatients() {
        return new ResponseListWrapper<>(patientService.getPatients());
    }

    @PreAuthorize("hasAnyRole('ROLE_DENTIST', 'ROLE_ADMIN')")
    @GetMapping(path = "/search")
    public UserDto getPatient(@RequestParam int id) {
        return patientService.getPatientById(id);
    }

    @PreAuthorize("hasAnyRole('ROLE_DENTIST', 'ROLE_ADMIN')")
    @PostMapping(path = "")
    public UserDto createPatient(UserDto userDto) {
        return patientService.save(userDto);
    }

}
