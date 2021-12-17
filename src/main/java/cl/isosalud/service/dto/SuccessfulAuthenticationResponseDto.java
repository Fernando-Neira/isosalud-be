package cl.isosalud.service.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class SuccessfulAuthenticationResponseDto {

    private String accessToken;
    private String refreshToken;
    private String role;
    private String status;

}
