package cl.isosalud.service.controller;

import cl.isosalud.service.dto.ResponseListWrapper;
import cl.isosalud.service.dto.UserDto;
import cl.isosalud.service.service.dentist.DentistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/dentist", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class MedicController {

    private final DentistService dentistService;

    @PreAuthorize("hasAnyRole('ROLE_DENTIST', 'ROLE_ADMIN')")
    @GetMapping(path = "")
    public ResponseListWrapper<List<UserDto>> getPatients() {
        return new ResponseListWrapper<>(dentistService.getDentist());
    }

    @PreAuthorize("hasAnyRole('ROLE_DENTIST', 'ROLE_ADMIN')")
    @GetMapping(path = "/search")
    public UserDto getPatient(@RequestParam int id) {
        return dentistService.getDentistById(id);
    }

}
