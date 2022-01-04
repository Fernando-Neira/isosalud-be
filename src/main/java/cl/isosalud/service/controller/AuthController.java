package cl.isosalud.service.controller;

import cl.isosalud.service.dto.UserDto;
import cl.isosalud.service.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserService userService;

    @PostMapping(path = "/register")
    public UserDto createUser(@RequestBody UserDto userDto) {
        return userService.save(userDto);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "/me")
    public UserDto getMeInfo() {

        return userService.getUserLogged();
    }

}
