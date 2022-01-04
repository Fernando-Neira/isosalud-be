package cl.isosalud.service.service.evolutions;

import cl.isosalud.service.dto.EvolutionDto;
import cl.isosalud.service.dto.GenericResponseDto;

import java.util.List;

public interface EvolutionsService {

    EvolutionDto getById(int noteId);

    EvolutionDto create(EvolutionDto payload);

    List<EvolutionDto> getAll();

    List<EvolutionDto> getEvolutionByPatientId(Integer patientId);

    GenericResponseDto delete(Integer id);
}
