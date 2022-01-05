package cl.isosalud.service.service.user;

import cl.isosalud.service.dto.ChangePasswordDto;
import cl.isosalud.service.dto.NameDescriptionObj;
import cl.isosalud.service.dto.PrevalidateUserDto;
import cl.isosalud.service.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto save(UserDto userEntity);

    boolean addRoleToUser(String username, String roleName);

    UserDto getUser(String username);

    List<UserDto> getUsers();

    UserDto getUserLogged();

    PrevalidateUserDto prevalidateRut(UserDto userDto);

    UserDto changeUserStatus(int userId, String newStatus);

    UserDto edit(int userId, UserDto userDto);

    List<NameDescriptionObj> getUserStates();

    UserDto changePassword(ChangePasswordDto request);
}
