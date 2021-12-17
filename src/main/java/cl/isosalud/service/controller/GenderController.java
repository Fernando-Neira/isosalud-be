package cl.isosalud.service.controller;

import cl.isosalud.service.dto.NameDescriptionObj;
import cl.isosalud.service.dto.ResponseListWrapper;
import cl.isosalud.service.service.gender.GenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/gender", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class GenderController {

    private final GenderService genderService;

    @PreAuthorize("hasAnyRole('ROLE_DENTIST', 'ROLE_ADMIN')")
    @GetMapping(path = "")
    public ResponseListWrapper<List<NameDescriptionObj>> getAppointments() {
        return new ResponseListWrapper<>(genderService.getAll());
    }

    @PreAuthorize("hasAnyRole('ROLE_DENTIST', 'ROLE_ADMIN')")
    @GetMapping(path = "/search")
    public NameDescriptionObj getAppointment(@RequestParam int id) {
        return genderService.getById(id);
    }


}
