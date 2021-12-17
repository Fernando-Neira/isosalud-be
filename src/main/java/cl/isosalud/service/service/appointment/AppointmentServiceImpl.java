package cl.isosalud.service.service.appointment;

import cl.isosalud.service.dto.*;
import cl.isosalud.service.entity.*;
import cl.isosalud.service.enums.MessagesEnum;
import cl.isosalud.service.exception.GenericException;
import cl.isosalud.service.mapping.Mapper;
import cl.isosalud.service.repository.*;
import cl.isosalud.service.service.notifications.NotificationService;
import cl.isosalud.service.service.notifications.call.CallService;
import cl.isosalud.service.service.notifications.email.EmailService;
import cl.isosalud.service.service.notifications.sms.SmsService;
import cl.isosalud.service.util.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final AppointmentStateRepository appointmentStateRepository;
    private final AppointmentTypeRepository appointmentTypeRepository;
    private final BoxRepository boxRepository;
    private final UserRepository userRepository;
    private final PersonRepository personRepository;
    private final TreatmentRepository treatmentRepository;
    private final NotificationService notificationService;
    private final Mapper mapper;

    @Override
    public List<AppointmentDto> getOwn() {
        String usernameLoggedIn = UserUtils.getUsernameLogged();
        UserEntity userEntity = userRepository
                .findByUsernameIgnoreCase(usernameLoggedIn)
                .orElseThrow(() -> new GenericException(HttpStatus.NOT_FOUND, String.format("User %s not found", usernameLoggedIn), String.format("User %s not found", usernameLoggedIn)));

        return appointmentRepository.findAllByMedicUserEntity(userEntity)
                .stream()
                .map(appointment -> mapper.map(appointment, AppointmentDto.class))
                .toList();
    }

    @Override
    public AppointmentDto getById(int appointmentId) {
        return mapper.map(appointmentRepository.findById(appointmentId), AppointmentDto.class);
    }

    @Override
    public List<AppointmentDto> getByRutPatient(String patientRut) {
        PersonEntity patientPersonEntity = personRepository.findByRutIgnoreCase(patientRut)
                .orElseThrow(() -> new GenericException(HttpStatus.NOT_FOUND, String.format("Patient rut %s not found", patientRut), String.format("Patient rut %s not found", patientRut)));

        UserEntity userEntity = userRepository.findByPersonEntity(patientPersonEntity)
                .orElseThrow(() -> new GenericException(HttpStatus.NOT_FOUND, String.format("Patient rut %s not found", patientRut), String.format("Patient rut %s not found", patientRut)));

        return appointmentRepository.findAllByPatientUserEntity(userEntity)
                .stream()
                .map(appointment -> mapper.map(appointment, AppointmentDto.class))
                .toList();
    }

    @Override
    public AppointmentDto create(AppointmentDto payload) {
        AppointmentEntity appointmentEntity = mapper.map(payload, AppointmentEntity.class);

        UserEntity patientUserEntity = userRepository.findById(payload.getPatient().getId())
                .orElseThrow(() -> new GenericException(HttpStatus.NOT_FOUND, String.format("Patient id %s not found", payload.getPatient().getId()), String.format("Patient id %s not found", payload.getPatient().getId())));

        appointmentEntity.setPatientUserEntity(patientUserEntity);

        UserEntity medicUserEntity;

        if (payload.getMedic() != null && payload.getMedic().getId() != null) {
            medicUserEntity = userRepository.findById(payload.getMedic().getId())
                    .orElseThrow(() -> new GenericException(HttpStatus.NOT_FOUND, String.format("Medic id %s not found", payload.getMedic().getId()), String.format("Medic id %s not found", payload.getMedic().getId())));
        } else {
            medicUserEntity = userRepository.findByUsernameIgnoreCase(UserUtils.getUsernameLogged())
                    .orElseThrow(() -> new GenericException(HttpStatus.NOT_FOUND, String.format("Medic id %s not found", payload.getMedic().getId()), String.format("Medic id %s not found", payload.getMedic().getId())));
        }


        appointmentEntity.setMedicUserEntity(medicUserEntity);

        BoxEntity boxEntity = boxRepository.findById(payload.getBox().getId())
                .orElseThrow(() -> new GenericException(HttpStatus.NOT_FOUND, String.format("Box %s not found", payload.getBox().getId()), String.format("Box %s not found", payload.getBox().getId())));

        appointmentEntity.setBox(boxEntity);

        AppointmentStateEntity appointmentState = appointmentStateRepository.findByNameIgnoreCase("Agendada")
                .orElseThrow(() -> new GenericException(HttpStatus.NOT_FOUND, String.format("AppointmentState %s not found", "Agendada"), String.format("AppointmentState id %s not found", "Agendada")));

        appointmentEntity.setAppointmentState(appointmentState);

        AppointmentTypeEntity appointmentType = appointmentTypeRepository.findById(payload.getType().getId())
                .orElseThrow(() -> new GenericException(HttpStatus.NOT_FOUND, String.format("AppointmentType %s not found", payload.getType().getName()), String.format("AppointmentType id %s not found", payload.getType().getName())));

        appointmentEntity.setAppointmentType(appointmentType);

        try {
            if (payload.getTreatment() != null && payload.getTreatment().getId() != null) {
                TreatmentEntity treatmentEntity = treatmentRepository.findById(payload.getTreatment().getId())
                        .orElseThrow(() -> new GenericException(HttpStatus.NOT_FOUND, String.format("TreatmentId %s not found", payload.getTreatment().getId()), String.format("TreatmentId %s not found", payload.getTreatment().getId())));

                appointmentEntity.setTreatmentEntity(treatmentEntity);
            }

            appointmentEntity = appointmentRepository.save(appointmentEntity);

            AppointmentDto newAppointmnet = mapper.map(appointmentEntity, AppointmentDto.class);

            try {
                sendNotificationAppointmentCreated(newAppointmnet);
            }catch (Exception e) {
                e.printStackTrace();
                log.error("Error al enviar notificacion...");
            }

            return newAppointmnet;
        } catch (Exception e) {
            e.printStackTrace();
            throw new GenericException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al crear cita", "Error al crear cita");
        }
    }

    private Map<String, Object> getParamsMessage(AppointmentDto appointment) {
        Map<String, Object> params = new HashMap<>();

        params.put("<paciente_rut>", appointment.getPatient().getPersonInfo().getRut());
        params.put("<paciente_nombre>", appointment.getPatient().getPersonInfo().getFirstName());
        params.put("<paciente_apellido>", appointment.getPatient().getPersonInfo().getLastName());
        params.put("<paciente_direccion>", appointment.getPatient().getPersonInfo().getAddressInfo().getStreet());
        params.put("<cita_dia_nombre>", appointment.getStartDate().format(DateTimeFormatter.ofPattern("EEEE")));
        params.put("<cita_dia_numero>", appointment.getStartDate().format(DateTimeFormatter.ofPattern("dd")));
        params.put("<cita_mes_nombre>", appointment.getStartDate().getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault()));
        params.put("<cita_mes_numero>", appointment.getStartDate().format(DateTimeFormatter.ofPattern("MM")));
        params.put("<cita_anno>", appointment.getStartDate().format(DateTimeFormatter.ofPattern("yyyy")));
        params.put("<cita_hora_inicio>", appointment.getStartDate().format(DateTimeFormatter.ofPattern("HH:mm a")));
        params.put("<cita_hora_fin>", appointment.getEndDate().format(DateTimeFormatter.ofPattern("HH \\'con\\' mm")));
        params.put("<cita_tipo>", appointment.getType().getName());
        params.put("<medico_nombre>", appointment.getMedic().getPersonInfo().getFirstName());
        params.put("<medico_apellido>", appointment.getMedic().getPersonInfo().getLastName());

        return params;
    }

    private void sendNotificationAppointmentCreated(AppointmentDto appointment) {
        notificationService.send(
                appointment.getPatient().getPreferredContactMeanName(),
                MessagesEnum.APPOINTMENT_CREATED,
                appointment.getPatient().getPersonInfo(),
                getParamsMessage(appointment)
        );
    }

    @Override
    public AppointmentDto edit(int appointmentId, AppointmentDto payload) {
        AppointmentEntity appointmentEntity = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new GenericException(HttpStatus.NOT_FOUND, String.format("Appointment id %s not found", payload.getPatient().getId()), String.format("Appointment id %s not found", payload.getPatient().getId())));

        AppointmentStateEntity appointmentState = appointmentStateRepository.findByNameIgnoreCase("Agendada")
                .orElseThrow(() -> new GenericException(HttpStatus.NOT_FOUND, String.format("AppointmentState %s not found", "Agendada"), String.format("AppointmentState id %s not found", "Agendada")));

        appointmentEntity.setAppointmentState(appointmentState);

        // Set new titulo
        appointmentEntity.setTitle(payload.getTitle());

        // Set new startDate
        appointmentEntity.setDateStart(payload.getStartDate());

        // Set new endDate
        appointmentEntity.setDateEnd(payload.getEndDate());

        // Set new Box
        BoxEntity boxEntity = boxRepository.findById(payload.getBox().getId())
                .orElseThrow(() -> new GenericException(HttpStatus.NOT_FOUND, String.format("Box %s not found", payload.getBox().getId()), String.format("Box %s not found", payload.getBox().getId())));

        appointmentEntity.setBox(boxEntity);

        // Set new Type
        AppointmentTypeEntity appointmentType = appointmentTypeRepository.findById(payload.getType().getId())
                .orElseThrow(() -> new GenericException(HttpStatus.NOT_FOUND, String.format("AppointmentType %s not found", payload.getType().getName()), String.format("AppointmentType id %s not found", payload.getType().getName())));

        appointmentEntity.setAppointmentType(appointmentType);

        // Set new Medic
        UserEntity medicUserEntity;

        if (payload.getMedic() != null && payload.getMedic().getId() != null) {
            medicUserEntity = userRepository.findById(payload.getMedic().getId())
                    .orElseThrow(() -> new GenericException(HttpStatus.NOT_FOUND, String.format("Medic id %s not found", payload.getMedic().getId()), String.format("Medic id %s not found", payload.getMedic().getId())));
        } else {
            medicUserEntity = userRepository.findByUsernameIgnoreCase(UserUtils.getUsernameLogged())
                    .orElseThrow(() -> new GenericException(HttpStatus.NOT_FOUND, String.format("Medic id %s not found", payload.getMedic().getId()), String.format("Medic id %s not found", payload.getMedic().getId())));
        }

        appointmentEntity.setMedicUserEntity(medicUserEntity);

        // Set new Comment

        appointmentEntity.setComment(payload.getComment());

        // Set new Patient

        UserEntity patientUserEntity = userRepository.findById(payload.getPatient().getId())
                .orElseThrow(() -> new GenericException(HttpStatus.NOT_FOUND, String.format("Patient id %s not found", payload.getPatient().getId()), String.format("Patient id %s not found", payload.getPatient().getId())));

        appointmentEntity.setPatientUserEntity(patientUserEntity);


        try {
            if (payload.getTreatment() != null && payload.getTreatment().getId() != null) {
                TreatmentEntity treatmentEntity = treatmentRepository.findById(payload.getTreatment().getId())
                        .orElseThrow(() -> new GenericException(HttpStatus.NOT_FOUND, String.format("TreatmentId %s not found", payload.getTreatment().getId()), String.format("TreatmentId %s not found", payload.getTreatment().getId())));

                appointmentEntity.setTreatmentEntity(treatmentEntity);
            }

            appointmentEntity = appointmentRepository.save(appointmentEntity);

            AppointmentDto newAppointmnet = mapper.map(appointmentEntity, AppointmentDto.class);

            sendNotificationAppointmentEdited(newAppointmnet);

            return newAppointmnet;
        } catch (Exception e) {
            throw new GenericException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al crear cita", "Error al crear cita");
        }
    }

    private void sendNotificationAppointmentEdited(AppointmentDto appointment) {
        notificationService.send(
                appointment.getPatient().getPreferredContactMeanName(),
                MessagesEnum.APPOINTMENT_CREATED,
                appointment.getPatient().getPersonInfo(),
                getParamsMessage(appointment)
        );
    }

    @Override
    public List<AppointmentDto> getAll() {
        return appointmentRepository.findAll()
                .stream()
                .map(appointment -> mapper.map(appointment, AppointmentDto.class))
                .toList();
    }

    @Override
    public AppointmentBoxAvaiblesDto getBoxesAvailables(LocalDateTime startDate, LocalDateTime endDate) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
        List<AppointmentEntity> appointmentEntities = appointmentRepository.findAllAppointmentsInHour(startDate, endDate);

        return AppointmentBoxAvaiblesDto.builder()
                .boxesUnavailable(
                        appointmentEntities.stream().map(a -> {
                            BoxEntity boxEntity = a.getBox();

                            return NameDescriptionObj.builder()
                                    .id(boxEntity.getId())
                                    .name(boxEntity.getName())
                                    .description(boxEntity.getDescription())
                                    .build();
                        }).distinct().toList())
                .build();
    }

    @Override
    public AppointmentDto cancel(AppointmentDto appointment) {
        AppointmentEntity appointmentEntity = appointmentRepository.findById(appointment.getId())
                .orElseThrow(() -> new GenericException(HttpStatus.NOT_FOUND, String.format("Appointment id %s not found", appointment.getId()), String.format("Appointment id %s not found", appointment.getId())));

        AppointmentStateEntity appointmentState = appointmentStateRepository.findByNameIgnoreCase("Cancelada")
                .orElseThrow(() -> new GenericException(HttpStatus.NOT_FOUND, String.format("AppointmentState %s not found", "Cancelada"), String.format("AppointmentState %s not found", "Cancelada")));

        appointmentEntity.setAppointmentState(appointmentState);
        return mapper.map(appointmentRepository.save(appointmentEntity), AppointmentDto.class);
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
