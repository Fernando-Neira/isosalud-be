package cl.isosalud.service.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class NotificationResponse {

    private ResumeDto patients;
    private ResumeDto appointments;
    private ResumeDto2 appointmentsStates;
    private ResumeDto treatment;
    private int genderMale;
    private int genderFemale;

    @NoArgsConstructor
    @Setter
    @Getter
    public static class ResumeDto {
        private int total;
        private int previousMonth;
        private int currentMonth;

        public ResumeDto(int[] data) {
            this.total = data[0];
            this.previousMonth = data[1];
            this.currentMonth = data[2];
        }
    }

    @NoArgsConstructor
    @Setter
    @Getter
    public static class ResumeDto2 {
        private int cancelados;
        private int realizados;

        public ResumeDto2(int[] data) {
            cancelados = data[0];
            realizados = data[1];
        }
    }
}
