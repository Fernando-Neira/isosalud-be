package cl.isosalud.service.service.notifications;

import cl.isosalud.service.dto.NotificationResponse;
import cl.isosalud.service.dto.PersonDto;
import cl.isosalud.service.entity.PersonEntity;
import cl.isosalud.service.enums.MessagesEnum;

import java.util.Map;

public interface NotificationService {

    NotificationResponse send(String notificationType, MessagesEnum messageEnum, PersonDto personDto, Map<String, Object> params);
    NotificationResponse send(String notificationType, MessagesEnum messageEnum, PersonEntity personEntity, Map<String, Object> params);

}
