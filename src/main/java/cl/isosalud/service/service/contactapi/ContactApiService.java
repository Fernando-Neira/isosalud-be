package cl.isosalud.service.service.contactapi;

import cl.isosalud.service.dto.NameDescriptionObj;

import java.util.List;

public interface ContactApiService {

    List<NameDescriptionObj> getAll();
    NameDescriptionObj getById(int boxId);

}
