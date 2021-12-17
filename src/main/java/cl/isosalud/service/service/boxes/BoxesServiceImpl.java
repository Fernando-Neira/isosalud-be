package cl.isosalud.service.service.boxes;

import cl.isosalud.service.dto.NameDescriptionObj;
import cl.isosalud.service.mapping.Mapper;
import cl.isosalud.service.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class BoxesServiceImpl implements BoxesService {

    private final BoxRepository boxRepository;
    private final Mapper mapper;

    @Override
    public List<NameDescriptionObj> getAll() {
        return boxRepository.findAll()
                .stream()
                .map(box -> mapper.map(box, NameDescriptionObj.class))
                .toList();
    }

    @Override
    public NameDescriptionObj getById(int boxId) {
        return mapper.map(boxRepository.findById(boxId), NameDescriptionObj.class);
    }

}
