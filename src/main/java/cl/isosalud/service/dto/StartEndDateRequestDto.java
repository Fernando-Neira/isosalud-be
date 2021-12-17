package cl.isosalud.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class StartEndDateRequestDto {

    @JsonFormat(pattern="yyyy-MM-dd HH:mm")
    private LocalDateTime startDate;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm")
    private LocalDateTime endDate;

}