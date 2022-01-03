package cl.isosalud.service.service.notifications.email;

import cl.isosalud.service.dto.AppointmentDto;
import cl.isosalud.service.dto.ConfigurationDto;
import cl.isosalud.service.entity.ConfigIvr;
import cl.isosalud.service.enums.MessagesEnum;
import cl.isosalud.service.repository.ConfigIvrRepository;
import cl.isosalud.service.service.configuration.ConfigurationServiceImpl;
import cl.isosalud.service.util.MessageParamsResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final ConfigurationServiceImpl configurationServiceImpl;
    private final ConfigIvrRepository configIvrRepository;
    private final JavaMailSender javaMailSender;

    private boolean enableService;

    @PostConstruct
    public void init() {
        enableService = configurationServiceImpl.getKey("ENABLE_EMAIL_SERVICE")
                .map(ConfigurationDto::getValue)
                .map(Boolean::parseBoolean)
                .orElse(true);

        log.info("Service initialize successfully");
        log.info(enableService ? "Service enabled" : "Service disabled");
    }

    @Scheduled(fixedRate = 300000)
    public void updateEnableServiceValue() {
        enableService = configurationServiceImpl.getKey("ENABLE_EMAIL_SERVICE")
                .map(ConfigurationDto::getValue)
                .map(Boolean::parseBoolean)
                .orElse(true);
    }

    @Override
    public boolean send(MessagesEnum messageEnum, String emailTo, Map<String, Object> params) {

        String messagePlaceholder = configIvrRepository.findByKey("MESSAGE_"+messageEnum.name())
                .map(ConfigIvr::getValue)
                .orElse(messageEnum.getDefaultMsg());

        String message = MessageParamsResolver.resolve(messagePlaceholder, params);

        String subjectPlaceholder = configIvrRepository.findByKey("EMAIL_SUBJECT_"+messageEnum.name())
                .map(ConfigIvr::getValue)
                .orElse(messageEnum.getDefaultMsg());

        String subject = MessageParamsResolver.resolve(subjectPlaceholder, params);

        String bodyPlaceholder = configIvrRepository.findByKey("EMAIL_BODY_"+messageEnum.name())
                .map(ConfigIvr::getValue)
                .orElse(messageEnum.getDefaultMsg());

        String body = bodyPlaceholder.replace("<message>", message);

        return sendEmail(emailTo, subject, body);
    }

    private boolean sendEmail(String to, String subject, String body) {
        if (!enableService) {
            log.info("Service disabled from configuration.");
            return false;
        }

        log.info("Sending email {} - {} - {}", subject, to, body);

        MimeMessage msg = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(msg, true);

            helper.setFrom("contacto@isosalud.cl");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);

            javaMailSender.send(msg);

            log.info("Email sended successully");
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }

}
