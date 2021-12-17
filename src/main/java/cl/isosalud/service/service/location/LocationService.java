package cl.isosalud.service.service.location;

import cl.isosalud.service.dto.LocationDto;
import cl.isosalud.service.dto.NameDescriptionObj;

import java.util.List;

public interface LocationService {

    List<LocationDto> getAll();
    LocationDto getById(int locationId);

}
