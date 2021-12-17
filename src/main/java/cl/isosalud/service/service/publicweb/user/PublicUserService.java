package cl.isosalud.service.service.publicweb.user;

import cl.isosalud.service.dto.*;

import java.util.List;

public interface PublicUserService {
    SendOtpResponseDto sendOtp(String rut, boolean resend);
    SuccessfulAuthenticationResponseDto doLogin(String rut, int otp);
}
