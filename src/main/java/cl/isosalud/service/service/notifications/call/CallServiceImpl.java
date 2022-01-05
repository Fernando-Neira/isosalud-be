package cl.isosalud.service.service.notifications.call;

import cl.isosalud.service.context.AppContext;
import cl.isosalud.service.dto.ConfigurationDto;
import cl.isosalud.service.entity.ConfigIvr;
import cl.isosalud.service.enums.MessagesEnum;
import cl.isosalud.service.repository.ConfigIvrRepository;
import cl.isosalud.service.service.configuration.ConfigurationServiceImpl;
import cl.isosalud.service.util.MessageParamsResolver;
import com.twilio.rest.api.v2010.account.Call;
import com.twilio.type.PhoneNumber;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CallServiceImpl implements CallService {

    private final ConfigurationServiceImpl configurationServiceImpl;
    private final ConfigIvrRepository configIvrRepository;

    private boolean enableService;
    private String numberFrom;

    @PostConstruct
    public void init() {
        numberFrom = "+12526808820";
                /*configurationServiceImpl.getKey("TWILIO_NUMBER_FROM_CALL")
                .map(ConfigurationDto::getValue)
                .orElseThrow(() -> new RuntimeException(String.format("Key %s dont retrieve from database.", "TWILIO_NUMBER_FROM")));
*/
        enableService = configurationServiceImpl.getKey("ENABLE_CALL_SERVICE")
                .map(ConfigurationDto::getValue)
                .map(Boolean::parseBoolean)
                .orElse(true);

        log.info("Service initialize successfully");
        log.info(enableService ? "Service enabled" : "Service disabled");
    }

    @Scheduled(fixedRate = 300000)
    public void updateEnableServiceValue() {
        enableService = configurationServiceImpl.getKey("ENABLE_CALL_SERVICE")
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

        return doCall(numberTo, message);
    }

    private boolean doCall(String numberTo, String message) {
        if (!enableService) {
            log.info("Service disabled from configuration.");
            return false;
        }

        log.info("Making call {} - {}", numberTo, message);

        try {

            numberTo = numberTo.startsWith("+56") ? numberTo : "+56".concat(numberTo);

            Call call = Call.creator(
                            new PhoneNumber(numberTo),
                            new PhoneNumber(numberFrom),
                            new URI("http://api.isosalud.cl/api-ivr/do-call-patient"))
                    .create();

            AppContext.putKey(call.getSid(), message);

            log.info("Call send successful => {}", call.getSid());

            return true;
        } catch (URISyntaxException e) {
            e.printStackTrace();

            return false;
        }
    }

}
