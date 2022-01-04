package cl.isosalud.service.service.publicweb.user;

import cl.isosalud.service.dto.SendOtpResponseDto;
import cl.isosalud.service.dto.SuccessfulAuthenticationResponseDto;

public interface PublicUserService {
    SendOtpResponseDto sendOtp(String rut, boolean resend);

    SuccessfulAuthenticationResponseDto doLogin(String rut, int otp);
}
