package cl.isosalud.service.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class CallApiDto {

    private int numberTo;
    private String message;
    private String next_path;

}
