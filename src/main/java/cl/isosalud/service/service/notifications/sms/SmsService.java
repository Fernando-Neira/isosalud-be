package cl.isosalud.service.service.notifications.sms;

import cl.isosalud.service.enums.MessagesEnum;

import java.util.Map;

public interface SmsService {

    boolean send(MessagesEnum messageEnum, String numberTo, Map<String, Object> params);

}
