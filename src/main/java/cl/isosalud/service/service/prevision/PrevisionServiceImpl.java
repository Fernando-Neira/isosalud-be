package cl.isosalud.service.service.prevision;

import cl.isosalud.service.dto.NameDescriptionObj;
import cl.isosalud.service.mapping.Mapper;
import cl.isosalud.service.repository.PrevisionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PrevisionServiceImpl implements PrevisionService {

    private final PrevisionRepository previsionRepository;
    private final Mapper mapper;

    @Override
    public List<NameDescriptionObj> getAll() {
        return previsionRepository.findAll()
                .stream()
                .map(box -> mapper.map(box, NameDescriptionObj.class))
                .toList();
    }

    @Override
    public NameDescriptionObj getById(int boxId) {
        return mapper.map(previsionRepository.findById(boxId), NameDescriptionObj.class);
    }

}
