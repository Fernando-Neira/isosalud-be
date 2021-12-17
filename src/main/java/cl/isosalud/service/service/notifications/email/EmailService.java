package cl.isosalud.service.service.notifications.email;

import cl.isosalud.service.enums.MessagesEnum;

import java.util.Map;

public interface EmailService {

    boolean send(MessagesEnum messageEnum, String numberTo, Map<String, Object> params);

}
