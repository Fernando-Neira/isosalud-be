package cl.isosalud.service.controller;

import cl.isosalud.service.dto.NameDescriptionObj;
import cl.isosalud.service.dto.ResponseListWrapper;
import cl.isosalud.service.service.boxes.BoxesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/boxes", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BoxesController {

    private final BoxesService boxesService;

    @PreAuthorize("hasAnyRole('ROLE_DENTIST', 'ROLE_ADMIN')")
    @GetMapping(path = "")
    public ResponseListWrapper<List<NameDescriptionObj>> getAppointments() {
        return new ResponseListWrapper<>(boxesService.getAll());
    }

    @PreAuthorize("hasAnyRole('ROLE_DENTIST', 'ROLE_ADMIN')")
    @GetMapping(path = "/search")
    public NameDescriptionObj getAppointment(@RequestParam int id) {
        return boxesService.getById(id);
    }


}
