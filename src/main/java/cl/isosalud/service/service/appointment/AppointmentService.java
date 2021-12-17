package cl.isosalud.service.service.appointment;

import cl.isosalud.service.dto.AppointmentBoxAvaiblesDto;
import cl.isosalud.service.dto.AppointmentDto;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentService {

    List<AppointmentDto> getOwn();
    AppointmentDto getById(int appointmentId);
    AppointmentDto create(AppointmentDto appointment);
    AppointmentDto cancel(AppointmentDto appointment);
    List<AppointmentDto> getByRutPatient(String patientRut);
    AppointmentDto edit(int appointmentId, AppointmentDto appointment);
    List<AppointmentDto> getAll();
    AppointmentBoxAvaiblesDto getBoxesAvailables(LocalDateTime startDate, LocalDateTime endDate);
}
