package cl.isosalud.service.controller;

import cl.isosalud.service.context.AppContext;
import cl.isosalud.service.service.contactapi.ContactApiService;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Call;
import com.twilio.twiml.VoiceResponse;
import com.twilio.twiml.voice.Gather;
import com.twilio.twiml.voice.Hangup;
import com.twilio.twiml.voice.Say;
import com.twilio.type.PhoneNumber;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

@RestController
@RequestMapping(path = "/api-ivr", produces = MediaType.APPLICATION_XML_VALUE)
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Slf4j
public class IvrApiController {

    private final ContactApiService contactApiService;
    private final HttpServletRequest httpServletRequest;
    private final HttpServletResponse httpServletResponse;

    static {
        Twilio.init("AC727a257505eec4b1b4e43a0b2531feb2", "4228591477bc8a3bed07909efe8b74c6");
    }

    @GetMapping(path = "/test")
    public String test() throws URISyntaxException {

        String from = "+12526808820";
        String to = "+56976188098";

        Call call = Call.creator(new PhoneNumber(to), new PhoneNumber(from),
                new URI("http://isosalud.ddns.net:8080/api-ivr/welcome"))
                .create();

        return call.toString();
    }

    @PostMapping(path = "/status")
    public String statusCall() {
        System.out.println("---status");
        httpServletRequest.getParameterMap().forEach((key, value) -> System.out.println(key + " = " + Arrays.toString(value)));
        System.out.println("---");

        return "OK";
    }

    @PostMapping(path = "/do-call-patient")
    public String appointmentNotify(@RequestParam(name = "CallSid") String callSid) {
        log.info("/do-call-patient Called.. {}", callSid);

        String message = AppContext.getKeyString(callSid);

        VoiceResponse response = new VoiceResponse.Builder()
                .say(new Say.Builder(message)
                        .voice(Say.Voice.MAN)
                        .language(Say.Language.ES_MX)
                        .loop(1)
                        .build())
                .hangup(new Hangup.Builder().build())
                .build();

        return response.toXml();
    }

    @PostMapping(path = "/welcome")
    public String getAppointments() {
        System.out.println("---welcome");
        httpServletRequest.getParameterMap().forEach((key, value) -> System.out.println(key + " = " + Arrays.toString(value)));
        System.out.println("---");
        VoiceResponse response = new VoiceResponse.Builder()
                .say(new Say.Builder(
                        "Hola bla bla bla tienes la cita x dia para confirmar presiona 1 y para rechazar presiona 2")
                        .voice(Say.Voice.MAN)
                        .language(Say.Language.ES_MX)
                        .loop(1)
                        .build())
                .gather(new Gather.Builder()
                        .action("/api-ivr/welcome2")
                        .numDigits(1)
                        .build())
                .build();
        return response.toXml();
    }

    @PostMapping(path = "/welcome2")
    public String response(@RequestParam String Digits) {
        System.out.println("---welcome2");
        httpServletRequest.getParameterMap().forEach((key, value) -> System.out.println(key + " = " + Arrays.toString(value)));
        System.out.println("---");

        System.out.println("Digits = " + Digits);

        VoiceResponse response = new VoiceResponse.Builder()
                .say(new Say.Builder(
                        "Hola de nuevo presionaste "+ Digits)
                        .voice(Say.Voice.ALICE)
                        .language(Say.Language.ES_MX)
                        .build())
                .hangup(new Hangup.Builder().build())
                .build();
        return response.toXml();
    }


}
