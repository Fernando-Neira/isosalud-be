package cl.isosalud.service.service.appointment.type;

import cl.isosalud.service.dto.NameDescriptionObj;

import java.util.List;

public interface AppointmentTypeService {

    List<NameDescriptionObj> getAll();

    NameDescriptionObj getById(int boxId);

}
