package cl.isosalud.service.service.treatment;

import cl.isosalud.service.dto.*;
import cl.isosalud.service.entity.*;
import cl.isosalud.service.exception.GenericException;
import cl.isosalud.service.mapping.Mapper;
import cl.isosalud.service.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TreatmentServiceImpl implements TreatmentService {

    private final ClinicalProcessRepository clinicalProcessRepository;
    private final AppointmentRepository appointmentRepository;
    private final TreatmentRepository treatmentRepository;
    private final TreatmentStateRepository treatmentStateRepository;
    private final TreatmentSpecializationRepository treatmentSpecializationRepository;
    private final UserRepository userRepository;
    private final PaymentStatusRepository paymentStatusRepository;
    private final RelTreatmentClinicalProcessRepository relTreatmentClinicalProcessRepository;
    private final Mapper mapper;


    @Override
    public TreatmentDto getById(int treatmentId) {
        return treatmentRepository.findById(treatmentId)
                .map(t -> {
                    TreatmentDto treatmentDto = map(t);
                    List<RelTreatmentClinicalProcessEntity> relTreatmentClinicalProcessEntities = relTreatmentClinicalProcessRepository.findAllByTreatment(t);
                    treatmentDto.setProcesses(relTreatmentClinicalProcessEntities.stream().map(this::map).toList());

                    List<AppointmentEntity> appointmentEntities = appointmentRepository.findAllByTreatmentEntity(t);
                    treatmentDto.setAppointments(appointmentEntities.stream().map(a -> mapper.map(a, AppointmentDto.class)).toList());

                    return treatmentDto;
                })
                .orElseThrow(() -> new GenericException(HttpStatus.NOT_FOUND, String.format("TreatmentId %s not found", treatmentId), String.format("TreatmentId %s not found", treatmentId)));
    }

    @Override
    public List<TreatmentDto> getByPatientId(Integer patientId) {
        UserEntity patientUserEntity = userRepository.findById(patientId)
                .orElseThrow(() -> new GenericException(HttpStatus.NOT_FOUND, String.format("PatientId %s not found", patientId), String.format("PatientId rut %s not found", patientId)));

        return treatmentRepository.findAllByPatientUser(patientUserEntity)
                .stream()
                .map(t -> {
                    TreatmentDto treatmentDto = map(t);
                    List<RelTreatmentClinicalProcessEntity> relTreatmentClinicalProcessEntities = relTreatmentClinicalProcessRepository.findAllByTreatment(t);
                    treatmentDto.setProcesses(relTreatmentClinicalProcessEntities.stream().map(this::map).toList());

                    List<AppointmentEntity> appointmentEntities = appointmentRepository.findAllByTreatmentEntity(t);
                    treatmentDto.setAppointments(appointmentEntities.stream().map(a -> mapper.map(a, AppointmentDto.class)).toList());

                    if (treatmentDto.getAppointments() != null && !treatmentDto.getAppointments().isEmpty()) {
                        AppointmentDto lastMeeting = Collections.max(treatmentDto.getAppointments(), Comparator.comparing(AppointmentDto::getStartDate));
                        treatmentDto.setLastMeeting(lastMeeting != null ? lastMeeting.getStartDate() : null);

                        AppointmentDto firstMeeting = Collections.min(treatmentDto.getAppointments(), Comparator.comparing(AppointmentDto::getStartDate));
                        treatmentDto.setStartDate(firstMeeting != null ? firstMeeting.getStartDate() : null);
                    }

                    return treatmentDto;
                })
                .toList();
    }

    @Override
    public TreatmentDto create(TreatmentDto payload) {
        TreatmentStateEntity treatmentStateEntity = treatmentStateRepository.findByName("En proceso")
                .orElseThrow(() -> new GenericException(HttpStatus.NOT_FOUND, String.format("TreatmentState %s not found", "En proceso"), String.format("TreatmentState %s not found", "En proceso")));

        UserEntity patientUserEntity = userRepository.findById(payload.getPatient().getId())
                .orElseThrow(() -> new GenericException(HttpStatus.NOT_FOUND, String.format("PatientId %s not found", payload.getPatient().getId()), String.format("PatientId %s not found", payload.getPatient().getId())));

        UserEntity medicUserEntity = userRepository.findById(payload.getMedic().getId())
                .orElseThrow(() -> new GenericException(HttpStatus.NOT_FOUND, String.format("MedicId %s not found", payload.getMedic().getId()), String.format("MedicId %s not found", payload.getMedic().getId())));

        TreatmentSpecializationEntity treatmentSpecializationEntity = treatmentSpecializationRepository.findById(payload.getSpecialization().getId())
                .orElseThrow(() -> new GenericException(HttpStatus.NOT_FOUND, String.format("TreatmenSpecializationId %s not found", payload.getSpecialization().getId()), String.format("TreatmenSpecializationId %s not found", payload.getSpecialization().getId())));

        if (payload.getProcesses() == null && payload.getProcesses().isEmpty()) {
            throw new GenericException(HttpStatus.NOT_FOUND, "Processes dont received", "You need send processes");
        }

        TreatmentEntity treatmentEntity = TreatmentEntity.builder()
                .patientUser(patientUserEntity)
                .medicUser(medicUserEntity)
                .comment(payload.getComment())
                .treatmentSpecializationEntity(treatmentSpecializationEntity)
                .treatmentState(treatmentStateEntity)
                .typeOdontograma(payload.getTypeOdontograma())
                .build();

        treatmentEntity = treatmentRepository.save(treatmentEntity);
        final TreatmentEntity finalTreatmentEntity = treatmentEntity;

        PaymentStatusEntity paymentStatus = paymentStatusRepository.findByName("Pendiente")
                .orElseThrow(() -> new GenericException(HttpStatus.NOT_FOUND, String.format("PaymentStatus %s not found", "Pendiente"), String.format("PaymentStatus %s not found", "Pendiente")));


        List<RelTreatmentClinicalProcessEntity> relTreatmentClinicalProcessEntities = payload.getProcesses().stream()
                .map(p -> {
                    ClinicalProcessEntity clinicalProcess = clinicalProcessRepository.findById(p.getId())
                            .orElseThrow(() -> new GenericException(HttpStatus.NOT_FOUND, String.format("ProcessClinicId %s not found", p.getId()), String.format("ProcessClinicId %s not found", p.getId())));

                    return RelTreatmentClinicalProcessEntity.builder()
                            .treatment(finalTreatmentEntity)
                            .clinicalProcess(clinicalProcess)
                            .price(p.getPrice())
                            .paymentStatus(paymentStatus)
                            .dentalPieces(p.getPieces())
                            .build();
                })
                .toList();

        TreatmentDto treatmentDto = map(treatmentEntity);

        relTreatmentClinicalProcessEntities = relTreatmentClinicalProcessRepository.saveAll(relTreatmentClinicalProcessEntities);

        treatmentDto.setProcesses(relTreatmentClinicalProcessEntities.stream().map(this::map).toList());

        return treatmentDto;
    }

    private ClinicalProcessDto map(RelTreatmentClinicalProcessEntity target) {
        ClinicalProcessEntity clinicalProcess = target.getClinicalProcess();

        return ClinicalProcessDto.builder()
                .id(target.getId())
                .name(clinicalProcess.getName())
                .description(clinicalProcess.getDescription())
                .price(target.getPrice())
                .paymentStatus(target.getPaymentStatus().getName())
                .pieces(target.getDentalPieces())
                .build();
    }

    @Override
    public List<TreatmentDto> getAll() {
        return treatmentRepository.findAll()
                .stream()
                .map(t -> {
                    TreatmentDto treatmentDto = map(t);
                    List<RelTreatmentClinicalProcessEntity> relTreatmentClinicalProcessEntities = relTreatmentClinicalProcessRepository.findAllByTreatment(t);
                    treatmentDto.setProcesses(relTreatmentClinicalProcessEntities.stream().map(this::map).toList());

                    List<AppointmentEntity> appointmentEntities = appointmentRepository.findAllByTreatmentEntity(t);
                    treatmentDto.setAppointments(appointmentEntities.stream().map(a -> mapper.map(a, AppointmentDto.class)).toList());

                    return treatmentDto;
                })
                .toList();
    }

    @Override
    public TreatmentDto changeState(Integer treatmentId, String statusName) {
        TreatmentEntity treatmentEntity = treatmentRepository.findById(treatmentId)
                .orElseThrow(() -> new GenericException(HttpStatus.NOT_FOUND, String.format("TreatmentId %s not found", treatmentId), String.format("TreatmentId %s not found", treatmentId)));

        TreatmentStateEntity treatmentStateEntity = treatmentStateRepository.findByName(statusName)
                .orElseThrow(() -> new GenericException(HttpStatus.NOT_FOUND, String.format("TreatmentState %s not found", statusName), String.format("TreatmentState %s not found", statusName)));

        treatmentEntity.setTreatmentState(treatmentStateEntity);

        TreatmentDto treatmentDto = map(treatmentRepository.save(treatmentEntity));
        List<RelTreatmentClinicalProcessEntity> relTreatmentClinicalProcessEntities = relTreatmentClinicalProcessRepository.findAllByTreatment(treatmentEntity);
        treatmentDto.setProcesses(relTreatmentClinicalProcessEntities.stream().map(this::map).toList());

        List<AppointmentEntity> appointmentEntities = appointmentRepository.findAllByTreatmentEntity(treatmentEntity);
        treatmentDto.setAppointments(appointmentEntities.stream().map(a -> mapper.map(a, AppointmentDto.class)).toList());

        return treatmentDto;

    }

    @Override
    public List<ClinicalProcessTypesDto> getAllClinicalProcess() {
        return clinicalProcessRepository.findAll()
                .stream()
                .collect(Collectors.groupingBy(clinicalProcessEntity -> clinicalProcessEntity.getClinicalProcessType().getName()))
                .entrySet()
                .stream()
                .map(stringListEntry -> {
                    String type = stringListEntry.getKey();

                    return ClinicalProcessTypesDto.builder()
                            .name(type)
                            .processes(stringListEntry.getValue()
                                    .stream()
                                    .map(this::map)
                                    .peek(c -> c.setType(null))
                                    .toList())
                            .build();
                })
                .toList();
    }

    @Override
    public List<TreatmentSpecializationDto> getAllSpecializations() {
        return treatmentSpecializationRepository.findAll().stream().map(this::map).toList();
    }

    private ClinicalProcessDto map(ClinicalProcessEntity target) {
        return ClinicalProcessDto.builder()
                .id(target.getId())
                .name(target.getName())
                .description(target.getDescription())
                .type(NameDescriptionObj.builder()
                        .id(target.getId())
                        .name(target.getName())
                        .description(target.getDescription())
                        .build())
                .price(target.getPrice())
                .build();
    }

    private TreatmentDto map(TreatmentEntity target) {
        return TreatmentDto.builder()
                .id(target.getId())
                .medic(mapper.map(target.getMedicUser(), UserDto.class))
                .patient(mapper.map(target.getPatientUser(), UserDto.class))
                .comment(target.getComment())
                .specialization(map(target.getTreatmentSpecializationEntity()))
                .state(map(target.getTreatmentState()))
                .dateCreated(target.getDateCreated())
                .typeOdontograma(target.getTypeOdontograma())
                .build();
    }

    private TreatmentSpecializationDto map(TreatmentSpecializationEntity target) {
        return TreatmentSpecializationDto.builder()
                .id(target.getId())
                .name(target.getName())
                .description(target.getDescription())
                .build();
    }

    private TreatmentStateDto map(TreatmentStateEntity target) {
        return TreatmentStateDto.builder()
                .id(target.getId())
                .name(target.getName())
                .description(target.getDescription())
                .build();
    }

}
