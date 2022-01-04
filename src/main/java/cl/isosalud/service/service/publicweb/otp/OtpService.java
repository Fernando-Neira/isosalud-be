package cl.isosalud.service.service.publicweb.otp;

import java.util.Optional;

public interface OtpService {
    int generateOtp(String key);

    Optional<Integer> getOtp(String key);

    boolean invalidateOtp(String key);
}
