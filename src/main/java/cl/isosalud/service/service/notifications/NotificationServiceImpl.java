package cl.isosalud.service.service.notifications;

import cl.isosalud.service.dto.NotificationResponse;
import cl.isosalud.service.dto.PersonDto;
import cl.isosalud.service.entity.PersonEntity;
import cl.isosalud.service.enums.MessagesEnum;
import cl.isosalud.service.service.notifications.call.CallService;
import cl.isosalud.service.service.notifications.email.EmailService;
import cl.isosalud.service.service.notifications.sms.SmsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService{

    private final CallService callService;
    private final EmailService emailService;
    private final SmsService smsService;

    @Override
    public NotificationResponse send(String notificationType, MessagesEnum messageEnum, PersonDto personDto, Map<String, Object> params) {
        switch (notificationType) {
            case "call_phone" -> callService.send(messageEnum, String.valueOf(personDto.getCellphone()), params);
            case "sms" -> smsService.send(messageEnum, String.valueOf(personDto.getCellphone()), params);
            case "email" -> emailService.send(messageEnum, personDto.getEmail(), params);
            default -> {
                log.info("Without notificationType {}", notificationType);
            }
        }

        return NotificationResponse.builder()

                .build();
    }

    @Override
    public NotificationResponse send(String notificationType, MessagesEnum messageEnum, PersonEntity personEntity, Map<String, Object> params) {
        switch (notificationType) {
            case "call_phone" -> callService.send(messageEnum, String.valueOf(personEntity.getCellphone()), params);
            case "sms" -> smsService.send(messageEnum, String.valueOf(personEntity.getCellphone()), params);
            case "email" -> emailService.send(messageEnum, personEntity.getEmail(), params);
            default -> {
                log.info("Without notificationType {}", notificationType);
            }
        }

        return NotificationResponse.builder()

                .build();
    }

}
