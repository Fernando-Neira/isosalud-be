package cl.isosalud.service.service.gender;

import cl.isosalud.service.dto.NameDescriptionObj;

import java.util.List;

public interface GenderService {

    List<NameDescriptionObj> getAll();

    NameDescriptionObj getById(int boxId);

}
