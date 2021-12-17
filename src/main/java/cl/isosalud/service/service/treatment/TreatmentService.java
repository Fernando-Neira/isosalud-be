package cl.isosalud.service.service.treatment;

import cl.isosalud.service.dto.ClinicalProcessDto;
import cl.isosalud.service.dto.ClinicalProcessTypesDto;
import cl.isosalud.service.dto.TreatmentDto;
import cl.isosalud.service.dto.TreatmentSpecializationDto;

import java.util.List;

public interface TreatmentService {

    List<TreatmentDto> getAll();
    TreatmentDto getById(int treatmentId);
    List<TreatmentDto> getByPatientId(Integer patientId);
    TreatmentDto create(TreatmentDto payload);
    TreatmentDto changeState(Integer treatmentId, String statusName);
    List<ClinicalProcessTypesDto> getAllClinicalProcess();
    List<TreatmentSpecializationDto> getAllSpecializations();
}
