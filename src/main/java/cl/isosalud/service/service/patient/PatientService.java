package cl.isosalud.service.service.patient;

import cl.isosalud.service.dto.UserDto;

import java.util.List;

public interface PatientService {
    UserDto save(UserDto userEntity);

    UserDto getPatientById(int id);

    List<UserDto> getPatients();

}
