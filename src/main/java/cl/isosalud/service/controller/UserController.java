package cl.isosalud.service.controller;

import cl.isosalud.service.dto.NameDescriptionObj;
import cl.isosalud.service.dto.PrevalidateUserDto;
import cl.isosalud.service.dto.ResponseListWrapper;
import cl.isosalud.service.dto.UserDto;
import cl.isosalud.service.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    @PreAuthorize("hasAnyRole('ROLE_DENTIST', 'ROLE_ADMIN')")
    @GetMapping(path = "")
    public ResponseListWrapper<List<UserDto>> getUsers() {
        return new ResponseListWrapper<>(userService.getUsers());
    }

    @PreAuthorize("hasAnyRole('ROLE_DENTIST', 'ROLE_ADMIN')")
    @GetMapping(path = "/search")
    public UserDto getUser(@RequestParam String username) {
        return userService.getUser(username);
    }

    @PreAuthorize("hasAnyRole('ROLE_DENTIST', 'ROLE_ADMIN')")
    @PostMapping(path = "")
    public UserDto createUser(@RequestBody UserDto userDto) {
        return userService.save(userDto);
    }

    @PreAuthorize("hasAnyRole('ROLE_DENTIST', 'ROLE_ADMIN')")
    @PostMapping(path = "/edit")
    public UserDto editUser(@RequestParam int userId, @RequestBody UserDto userDto) {
        return userService.edit(userId, userDto);
    }

    @PreAuthorize("hasAnyRole('ROLE_DENTIST', 'ROLE_ADMIN')")
    @PostMapping(path = "/pre-validate")
    public PrevalidateUserDto validateRutAndGetUsername(@RequestBody UserDto userDto) {
        return userService.prevalidateRut(userDto);
    }

    @PreAuthorize("hasAnyRole('ROLE_DENTIST', 'ROLE_ADMIN')")
    @PostMapping(path = "/change-status")
    public UserDto changeUserStatus(@RequestParam int userId, @RequestParam String status) {
        return userService.changeUserStatus(userId, status);
    }

    @PreAuthorize("permitAll()")
    @GetMapping(path = "/states")
    public ResponseListWrapper<List<NameDescriptionObj>> getUserStates() {
        return new ResponseListWrapper<>(userService.getUserStates());
    }

}
