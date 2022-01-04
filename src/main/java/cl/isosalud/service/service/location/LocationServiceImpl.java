package cl.isosalud.service.service.location;

import cl.isosalud.service.dto.LocationDto;
import cl.isosalud.service.entity.CommuneEntity;
import cl.isosalud.service.entity.RegionEntity;
import cl.isosalud.service.exception.GenericException;
import cl.isosalud.service.mapping.Mapper;
import cl.isosalud.service.repository.CommuneRepository;
import cl.isosalud.service.repository.RegionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class LocationServiceImpl implements LocationService {

    private final CommuneRepository communeRepository;
    private final RegionRepository regionRepository;
    private final Mapper mapper;

    @Override
    public List<LocationDto> getAll() {
        return communeRepository.findAll()
                .stream()
                .collect(Collectors.groupingBy(CommuneEntity::getRegion))
                .entrySet()
                .stream()
                .map(region -> mapper.map(region, LocationDto.class))
                .toList();
    }

    @Override
    public LocationDto getById(int locationId) {
        RegionEntity regionEntity = regionRepository.findById(locationId)
                .orElseThrow(() -> new GenericException(HttpStatus.NOT_FOUND, String.format("Region id %s not found", locationId), String.format("Region id %s not found", locationId)));

        List<CommuneEntity> communesEntity = communeRepository.findAllByRegion(regionEntity);

        return LocationDto.builder()
                .id(regionEntity.getId())
                .name(regionEntity.getName())
                .abbreviation(regionEntity.getAbbreviation())
                .romanNumber(regionEntity.getRomanNumber())
                .communes(communesEntity.stream().map(c -> LocationDto.ComunneDto.builder().id(c.getId()).name(c.getName()).build()).toList())
                .build();
    }

}
