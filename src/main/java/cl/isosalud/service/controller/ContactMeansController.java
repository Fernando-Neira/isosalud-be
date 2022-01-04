package cl.isosalud.service.controller;

import cl.isosalud.service.dto.ContactMeanDto;
import cl.isosalud.service.dto.ResponseListWrapper;
import cl.isosalud.service.service.contactmeans.ContactMeanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/contact-means", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ContactMeansController {

    private final ContactMeanService contactMeanService;

    @PreAuthorize("hasAnyRole('ROLE_DENTIST', 'ROLE_ADMIN')")
    @GetMapping(path = "")
    public ResponseListWrapper<List<ContactMeanDto>> getAppointments() {
        return new ResponseListWrapper<>(contactMeanService.getAll());
    }

    @PreAuthorize("hasAnyRole('ROLE_DENTIST', 'ROLE_ADMIN')")
    @GetMapping(path = "/search")
    public ContactMeanDto getAppointment(@RequestParam int id) {
        return contactMeanService.getById(id);
    }


}
