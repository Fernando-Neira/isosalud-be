package cl.isosalud.service.controller;

import cl.isosalud.service.dto.LocationDto;
import cl.isosalud.service.dto.ResponseListWrapper;
import cl.isosalud.service.service.location.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/location", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class LocationsController {

    private final LocationService locationService;

    @GetMapping(path = "")
    public ResponseListWrapper<List<LocationDto>> getLocations() {
        return new ResponseListWrapper<>(locationService.getAll());
    }

    @GetMapping(path = "/search")
    public LocationDto getLocationById(@RequestParam int id) {
        return locationService.getById(id);
    }


}
