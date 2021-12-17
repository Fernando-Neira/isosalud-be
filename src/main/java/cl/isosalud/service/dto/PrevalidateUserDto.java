package cl.isosalud.service.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class PrevalidateUserDto {

    private String statusCode;
    private String errorMsg;
    private String username;

}
