package cl.isosalud.service.controller;

import cl.isosalud.service.dto.*;
import cl.isosalud.service.service.appointment.AppointmentService;
import cl.isosalud.service.service.appointment.type.AppointmentTypeService;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(value = "/appointment", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final AppointmentTypeService appointmentTypeService;

    @PreAuthorize("hasAnyRole('ROLE_DENTIST', 'ROLE_ADMIN')")
    @GetMapping(path = "/own")
    public ResponseListWrapper<List<AppointmentDto>> getAppointments() {
        return new ResponseListWrapper<>(appointmentService.getOwn());
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping(path = "")
    public ResponseListWrapper<List<AppointmentDto>> getAll() {
        return new ResponseListWrapper<>(appointmentService.getAll());
    }

    @PreAuthorize("hasAnyRole('ROLE_DENTIST', 'ROLE_ADMIN')")
    @GetMapping(path = "/search")
    public Object getAppointment(@RequestParam(required = false) Integer id, @RequestParam(name = "rut-patient", required = false) String patientRut) {
        return id != null ? appointmentService.getById(id) : appointmentService.getByRutPatient(patientRut);
    }

    @PreAuthorize("hasAnyRole('ROLE_DENTIST', 'ROLE_ADMIN')")
    @PostMapping(path = "")
    public AppointmentDto createAppointment(@RequestBody AppointmentDto appointment) {
        return appointmentService.create(appointment);
    }

    @PreAuthorize("hasAnyRole('ROLE_DENTIST', 'ROLE_ADMIN')")
    @PostMapping(path = "/edit")
    public AppointmentDto editAppointment(@RequestParam int appointmentId, @RequestBody AppointmentDto appointment) {
        return appointmentService.edit(appointmentId, appointment);
    }

    @PreAuthorize("hasAnyRole('ROLE_DENTIST', 'ROLE_ADMIN')")
    @PostMapping(path = "/cancel")
    public AppointmentDto cancelAppointment(@RequestBody AppointmentDto appointment) {
        return appointmentService.cancel(appointment);
    }

    @PreAuthorize("isAuthenticated() or isAnonymous()")
    @GetMapping(path = "/types")
    public ResponseListWrapper<List<NameDescriptionObj>> getAppointmentTypes() {
        return new ResponseListWrapper<>(appointmentTypeService.getAll());
    }

    @PreAuthorize("hasAnyRole('ROLE_DENTIST', 'ROLE_ADMIN')")
    @GetMapping(path = "/types/search")
    public NameDescriptionObj getAppointmentType(@RequestParam int id) {
        return appointmentTypeService.getById(id);
    }

    @PreAuthorize("hasAnyRole('ROLE_DENTIST', 'ROLE_ADMIN')")
    @GetMapping(path = "/unavailable")
    public AppointmentBoxAvaiblesDto getBoxesAvaibles(@RequestBody StartEndDateRequestDto payload) {
        return appointmentService.getBoxesAvailables(payload.getStartDate(), payload.getEndDate());
    }

}
