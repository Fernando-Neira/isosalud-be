package cl.isosalud.service.controller;

import cl.isosalud.service.dto.NameDescriptionObj;
import cl.isosalud.service.dto.ResponseListWrapper;
import cl.isosalud.service.dto.RoleDto;
import cl.isosalud.service.service.role.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/roles", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class RolesController {

    private final RoleService roleService;

    @PreAuthorize("hasAnyRole('ROLE_DENTIST', 'ROLE_ADMIN')")
    @GetMapping(path = "")
    public ResponseListWrapper<List<RoleDto>> getAppointments() {
        return new ResponseListWrapper<>(roleService.getAll());
    }

    @PreAuthorize("hasAnyRole('ROLE_DENTIST', 'ROLE_ADMIN')")
    @GetMapping(path = "/search")
    public RoleDto getAppointment(@RequestParam int id) {
        return roleService.getById(id);
    }


}
