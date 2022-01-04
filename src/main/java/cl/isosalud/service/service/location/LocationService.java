package cl.isosalud.service.service.location;

import cl.isosalud.service.dto.LocationDto;

import java.util.List;

public interface LocationService {

    List<LocationDto> getAll();

    LocationDto getById(int locationId);

}
