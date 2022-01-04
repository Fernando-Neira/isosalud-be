package cl.isosalud.service.service.notifications.sms;

import cl.isosalud.service.dto.ConfigurationDto;
import cl.isosalud.service.entity.ConfigIvr;
import cl.isosalud.service.enums.MessagesEnum;
import cl.isosalud.service.repository.ConfigIvrRepository;
import cl.isosalud.service.service.configuration.ConfigurationServiceImpl;
import cl.isosalud.service.util.MessageParamsResolver;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class SmsServiceImpl implements SmsService {

    private final ConfigurationServiceImpl configurationServiceImpl;
    private final ConfigIvrRepository configIvrRepository;
    private boolean enableService;
    private String numberFrom;

    @PostConstruct
    public void init() {
        final String ACCOUNT_SID = configurationServiceImpl.getKey("TWILIO_ACCOUNT_SID")
                .map(ConfigurationDto::getValue)
                .orElseThrow(() -> new RuntimeException(String.format("Key %s dont retrieve from database.", "TWILIO_ACCOUNT_SID")));

        final String AUTH_TOKEN = configurationServiceImpl.getKey("TWILIO_AUTH_TOKEN")
                .map(ConfigurationDto::getValue)
                .orElseThrow(() -> new RuntimeException(String.format("Key %s dont retrieve from database.", "TWILIO_AUTH_TOKEN")));

        numberFrom = configurationServiceImpl.getKey("TWILIO_NUMBER_FROM")
                .map(ConfigurationDto::getValue)
                .orElseThrow(() -> new RuntimeException(String.format("Key %s dont retrieve from database.", "TWILIO_NUMBER_FROM")));

        enableService = configurationServiceImpl.getKey("ENABLE_SMS_SERVICE")
                .map(ConfigurationDto::getValue)
                .map(Boolean::parseBoolean)
                .orElse(true);

        log.info("Service initialize successfully");
        log.info(enableService ? "Service enabled" : "Service disabled");

        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }

    @Scheduled(fixedRate = 300000)
    public void updateEnableServiceValue() {
        enableService = configurationServiceImpl.getKey("ENABLE_SMS_SERVICE")
                .map(ConfigurationDto::getValue)
                .map(Boolean::parseBoolean)
                .orElse(true);
    }

    @Override
    public boolean send(MessagesEnum messageEnum, String numberTo, Map<String, Object> params) {
        if (!enableService) {
            log.info("Service disabled from configuration.");
            return false;
        }

        String messagePlaceholder = configIvrRepository.findByKey("MESSAGE_" + messageEnum.name())
                .map(ConfigIvr::getValue)
                .orElse(messageEnum.getDefaultMsg());

        String message = MessageParamsResolver.resolve(messagePlaceholder, params);

        return sendSMS(numberTo, message);
    }

    public boolean sendSMS(String numberTo, String message) {
        log.info("Sending SMS {} - {}", numberTo, message);

        numberTo = numberTo.startsWith("+56") ? numberTo : "+56".concat(numberTo);

        try {
            Message messageObj = Message.creator(new PhoneNumber(numberTo), new PhoneNumber(numberFrom), message).create();

            log.info("Sms send successful => {}", messageObj.getSid());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
