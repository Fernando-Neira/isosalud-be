package cl.isosalud.service.service.gender;

import cl.isosalud.service.dto.NameDescriptionObj;
import cl.isosalud.service.mapping.Mapper;
import cl.isosalud.service.repository.GenderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class GenderServiceImpl implements GenderService {

    private final GenderRepository genderRepository;
    private final Mapper mapper;

    @Override
    public List<NameDescriptionObj> getAll() {
        return genderRepository.findAll()
                .stream()
                .map(box -> mapper.map(box, NameDescriptionObj.class))
                .toList();
    }

    @Override
    public NameDescriptionObj getById(int boxId) {
        return mapper.map(genderRepository.findById(boxId), NameDescriptionObj.class);
    }

}
