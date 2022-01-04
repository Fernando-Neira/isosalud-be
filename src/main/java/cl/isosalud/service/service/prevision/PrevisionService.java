package cl.isosalud.service.service.prevision;

import cl.isosalud.service.dto.NameDescriptionObj;

import java.util.List;

public interface PrevisionService {

    List<NameDescriptionObj> getAll();

    NameDescriptionObj getById(int boxId);

}
