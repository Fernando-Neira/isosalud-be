package cl.isosalud.service.controller;

import cl.isosalud.service.dto.*;
import cl.isosalud.service.service.appointment.type.AppointmentTypeService;
import cl.isosalud.service.service.publicweb.publics.PublicService;
import cl.isosalud.service.service.publicweb.user.PublicUserService;
import cl.isosalud.service.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/public", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PublicController {

    private final AppointmentTypeService appointmentTypeService;

    private final PublicService publicService;
    private final UserService userService;

    @PostMapping(path = "/contact")
    public GenericResponseDto login(@RequestBody ContactEmail payload) {
        return publicService.sendEmail(payload);
    }

    @PreAuthorize("isAuthenticated() or isAnonymous()")
    @GetMapping(path = "/types")
    public ResponseListWrapper<List<NameDescriptionObj>> getAppointmentTypes() {
        return new ResponseListWrapper<>(appointmentTypeService.getAll());
    }

}
