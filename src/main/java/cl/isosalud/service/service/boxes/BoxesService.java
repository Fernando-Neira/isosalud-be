package cl.isosalud.service.service.boxes;

import cl.isosalud.service.dto.NameDescriptionObj;

import java.util.List;

public interface BoxesService {

    List<NameDescriptionObj> getAll();
    NameDescriptionObj getById(int boxId);

}
