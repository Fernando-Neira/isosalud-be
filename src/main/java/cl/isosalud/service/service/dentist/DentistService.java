package cl.isosalud.service.service.dentist;

import cl.isosalud.service.dto.UserDto;

import java.util.List;

public interface DentistService {
    UserDto getDentistById(int id);

    List<UserDto> getDentist();

}
