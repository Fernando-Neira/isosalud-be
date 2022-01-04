package cl.isosalud.service.service.contactmeans;

import cl.isosalud.service.dto.ContactMeanDto;
import cl.isosalud.service.entity.ContactMeanEntity;
import cl.isosalud.service.exception.GenericException;
import cl.isosalud.service.mapping.Mapper;
import cl.isosalud.service.repository.ContactMeanRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ContactMeanServiceImpl implements ContactMeanService {

    private final ContactMeanRepository contactMeanRepository;
    private final Mapper mapper;

    @Override
    public ContactMeanDto save(ContactMeanEntity contactMeanEntity) {
        return mapper.map(contactMeanRepository.save(contactMeanEntity), ContactMeanDto.class);
    }

    @Override
    public ContactMeanDto getById(int id) {
        return mapper.map(contactMeanRepository.findById(id).orElseThrow(() -> new GenericException(HttpStatus.NOT_FOUND, String.format("ContactMean id %s not found", id), String.format("ContactMean id %s not found", id))), ContactMeanDto.class);
    }

    @Override
    public List<ContactMeanDto> getAll() {
        return contactMeanRepository.findAll()
                .stream()
                .map(r -> mapper.map(r, ContactMeanDto.class))
                .toList();
    }

}
