package cl.isosalud.service.service.notifications.call;

import cl.isosalud.service.dto.AppointmentDto;
import cl.isosalud.service.dto.CallApiDto;
import cl.isosalud.service.dto.SmsMessageDto;
import cl.isosalud.service.enums.MessagesEnum;

import java.util.Map;

public interface CallService {

    boolean send(MessagesEnum messageEnum, String numberTo, Map<String, Object> params);

}
