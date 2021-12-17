package cl.isosalud.service.controller;

import cl.isosalud.service.dto.SendOtpResponseDto;
import cl.isosalud.service.dto.SuccessfulAuthenticationResponseDto;
import cl.isosalud.service.dto.UserDto;
import cl.isosalud.service.service.publicweb.user.PublicUserService;
import cl.isosalud.service.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/public/auth", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthPublicController {

    private final PublicUserService publicUserService;
    private final UserService userService;

    @PostMapping(path = "/login/send-otp")
    public SendOtpResponseDto sendOtp(@RequestParam String rut, @RequestParam(required = false) boolean resend) {
        return publicUserService.sendOtp(rut, resend);
    }

    @PostMapping(path = "/login")
    public SuccessfulAuthenticationResponseDto login(@RequestParam String rut, @RequestParam int otp) {
        return publicUserService.doLogin(rut, otp);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "/me")
    public UserDto getMeInfo() {
        return userService.getUserLogged();
    }

}
