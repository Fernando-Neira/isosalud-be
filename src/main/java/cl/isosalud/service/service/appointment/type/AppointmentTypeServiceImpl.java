package cl.isosalud.service.service.appointment.type;

import cl.isosalud.service.dto.NameDescriptionObj;
import cl.isosalud.service.mapping.Mapper;
import cl.isosalud.service.repository.AppointmentTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AppointmentTypeServiceImpl implements AppointmentTypeService {

    private final AppointmentTypeRepository appointmentTypeRepository;
    private final Mapper mapper;

    @Override
    public List<NameDescriptionObj> getAll() {
        return appointmentTypeRepository.findAll()
                .stream()
                .map(box -> mapper.map(box, NameDescriptionObj.class))
                .toList();
    }

    @Override
    public NameDescriptionObj getById(int boxId) {
        return mapper.map(appointmentTypeRepository.findById(boxId), NameDescriptionObj.class);
    }

}
