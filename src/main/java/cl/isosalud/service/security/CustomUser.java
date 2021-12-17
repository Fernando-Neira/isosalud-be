package cl.isosalud.service.security;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
public class CustomUser extends User {

    private final String firstName;
    private final String lastName;
    private final String status;

    public CustomUser(String username,
                      String password,
                      Collection<? extends GrantedAuthority> authorities,
                      String firstName,
                      String lastName,
                      String status) {

        super(username, password, true, true,
                true, true, authorities);

        this.firstName = firstName;
        this.lastName = lastName;
        this.status = status;
    }

}