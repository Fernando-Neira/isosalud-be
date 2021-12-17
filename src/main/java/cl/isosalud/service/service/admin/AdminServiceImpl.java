package cl.isosalud.service.service.admin;

import cl.isosalud.service.dto.AdminResumeDto;
import cl.isosalud.service.dto.NameDescriptionObj;
import cl.isosalud.service.dto.ProductDto;
import cl.isosalud.service.entity.*;
import cl.isosalud.service.exception.GenericException;
import cl.isosalud.service.mapping.Mapper;
import cl.isosalud.service.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.TemporalAmount;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AdminServiceImpl implements AdminService {

    private final AppointmentRepository appointmentRepository;
    private final TreatmentRepository treatmentRepository;
    private final UserRepository userRepository;

    @Override
    public AdminResumeDto getResume() {
        int[] patients = getNewPatientsMonth();
        int[][] appointments = getNewAppointmentsMonth();
        int[] treatments = getNewTreatmentsMonth();

        return AdminResumeDto.builder()
                .patients(new AdminResumeDto.ResumeDto(patients))
                .appointments(new AdminResumeDto.ResumeDto(appointments[0]))
                .treatment(new AdminResumeDto.ResumeDto(treatments))
                .appointmentsStates(new AdminResumeDto.ResumeDto2(appointments[1]))
                .build();
    }

    private int[] getNewPatientsMonth() {
        List<UserEntity> allUsers = userRepository.findAll();

        int thisMonth = Integer.parseInt(String.valueOf(allUsers.stream().filter(u -> u.getDateCreated().getMonth().equals(LocalDate.now().getMonth())).count()));
        int prevMonth = Integer.parseInt(String.valueOf(allUsers.stream().filter(u -> u.getDateCreated().getMonth().equals(LocalDate.now().minus(Period.ofMonths(1)).getMonth())).count()));
        int total = allUsers.size();

        return new int[] {total, prevMonth, thisMonth};
    }

    private int[][] getNewAppointmentsMonth() {
        List<AppointmentEntity> allAppointments = appointmentRepository.findAll();

        int thisMonth = Integer.parseInt(String.valueOf(allAppointments.stream().filter(a -> a.getDateCreated().getMonth().equals(LocalDate.now().getMonth())).count()));
        int prevMonth = Integer.parseInt(String.valueOf(allAppointments.stream().filter(u -> u.getDateCreated().getMonth().equals(LocalDate.now().minus(Period.ofMonths(1)).getMonth())).count()));
        int canceledMonth = Integer.parseInt(String.valueOf(allAppointments.stream().filter(u -> u.getAppointmentState().getName().equals("Cancelada")).count()));
        int realizadasMonth = Integer.parseInt(String.valueOf(allAppointments.stream().filter(u -> u.getAppointmentState().getName().equals("Realizada")).count()));
        int total = allAppointments.size();

        return new int[][] {{total, prevMonth, thisMonth}, {canceledMonth, realizadasMonth}};
    }

    private int[] getNewTreatmentsMonth() {
        List<TreatmentEntity> allTreatments = treatmentRepository.findAll();

        int thisMonth = Integer.parseInt(String.valueOf(allTreatments.stream().filter(a -> a.getDateCreated().getMonth().equals(LocalDate.now().getMonth())).count()));
        int prevMonth = Integer.parseInt(String.valueOf(allTreatments.stream().filter(u -> u.getDateCreated().getMonth().equals(LocalDate.now().minus(Period.ofMonths(1)).getMonth())).count()));
        int total = allTreatments.size();

        return new int[] {total, prevMonth, thisMonth};
    }

}
