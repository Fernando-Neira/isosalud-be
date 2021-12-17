package cl.isosalud.service.util;

import lombok.experimental.UtilityClass;
import org.springframework.security.core.context.SecurityContextHolder;

@UtilityClass
public class UserUtils {

    public static String getUsernameLogged() {
        return (String) (SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    }

}
