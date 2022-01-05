package cl.isosalud.service.service.publicweb.user;

import cl.isosalud.service.dto.SendOtpResponseDto;
import cl.isosalud.service.dto.SuccessfulAuthenticationResponseDto;
import cl.isosalud.service.entity.PersonEntity;
import cl.isosalud.service.entity.UserEntity;
import cl.isosalud.service.enums.MessagesEnum;
import cl.isosalud.service.exception.GenericException;
import cl.isosalud.service.repository.PersonRepository;
import cl.isosalud.service.repository.UserRepository;
import cl.isosalud.service.service.notifications.NotificationService;
import cl.isosalud.service.service.publicweb.otp.OtpService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PublicUserServiceImpl implements PublicUserService {

    private final NotificationService notificationService;
    private final UserRepository userRepository;
    private final PersonRepository personRepository;
    private final OtpService otpService;
    private final HttpServletRequest request;

    @Override
    public SendOtpResponseDto sendOtp(String rut, boolean resend) {
        Optional<PersonEntity> personEntity = personRepository.findByRutIgnoreCase(rut.replace(".", ""));

        if (personEntity.isEmpty()) {
            return SendOtpResponseDto.builder()
                    .status("NOK")
                    .message("Rut no encontrado, si quieres tratarte con nosotros contactanos!")
                    .build();
        }

        UserEntity userEntity = userRepository.findByPersonEntity(personEntity.get()).get();

        Optional<Integer> otpSaved = otpService.getOtp(rut);

        String contact = switch (userEntity.getPreferredContactMeanEntity().getSpanishName()) {
            case "SMS", "Llamada" -> String.valueOf(userEntity.getPersonEntity().getCellphone()).replaceAll("\\d(?=(?:\\D*\\d){4})", "*");
            case "Correo électronico" -> userEntity.getPersonEntity().getEmail().replaceAll("(^[^@]{3}|(?!^)\\G)[^@]", "$1*");
            default -> throw new IllegalStateException("Unexpected value: " + userEntity.getPreferredContactMeanEntity().getSpanishName());
        };

        if (otpSaved.isPresent() && !resend) {
            return SendOtpResponseDto.builder()
                    .status("OK")
                    .message("Contraseña temporal envíada mediante ".concat(userEntity.getPreferredContactMeanEntity().getSpanishName()).concat(" a ").concat(contact))
                    .build();
        }

        int otp = otpService.generateOtp(rut);

        Map<String, Object> params = new HashMap<>();
        params.put("<paciente_nombre>", personEntity.get().getFirstName());
        params.put("<paciente_apellido>", personEntity.get().getLastName());
        params.put("<otp>", otp);

        notificationService.send(userEntity.getPreferredContactMeanEntity().getName(), MessagesEnum.OTP_SEND, personEntity.get(), params);

        return SendOtpResponseDto.builder()
                .status("OK")
                .message("Contraseña temporal envíada mediante ".concat(userEntity.getPreferredContactMeanEntity().getSpanishName()).concat(" a ").concat(contact))
                .build();
    }

    @Override
    public SuccessfulAuthenticationResponseDto doLogin(String rut, int otp) {
        Optional<Integer> otpSaved = otpService.getOtp(rut);

        if (otpSaved.isEmpty() || otpSaved.get() != otp) {
            throw new GenericException(HttpStatus.UNAUTHORIZED, "Código de acceso invalido", "Código de acceso invalido");
        }

        PersonEntity personEntity = personRepository.findByRutIgnoreCase(rut.replace(".", "")).get();

        UserEntity userEntity = userRepository.findByPersonEntity(personEntity).get();

        Object[] jwtData = getJwt(userEntity);

        return SuccessfulAuthenticationResponseDto.builder()
                .role(((List<String>) jwtData[0]).get(0))
                .status(userEntity.getUserStateEntity().getName())
                .accessToken((String) jwtData[1])
                .refreshToken((String) jwtData[2])
                .build();
    }

    private Object[] getJwt(UserEntity user) {
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());

        List<String> authorities = List.of(new SimpleGrantedAuthority(user.getRoleEntity().getName()).getAuthority());

        int minutes = 60 * 12;

        String accessToken = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + minutes * 60 * 1000))
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles", authorities)
                .sign(algorithm);

        String refreshToken = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 30 * 60 * 1000))
                .withIssuer(request.getRequestURL().toString())
                .sign(algorithm);

        return new Object[]{
                authorities,
                accessToken,
                refreshToken
        };
    }
}
