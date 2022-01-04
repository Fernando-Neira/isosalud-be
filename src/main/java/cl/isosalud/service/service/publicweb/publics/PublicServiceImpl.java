package cl.isosalud.service.service.publicweb.publics;

import cl.isosalud.service.dto.ContactEmail;
import cl.isosalud.service.dto.GenericResponseDto;
import cl.isosalud.service.enums.MessagesEnum;
import cl.isosalud.service.service.notifications.email.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PublicServiceImpl implements PublicService {

    private final EmailService emailService;

    @Override
    public GenericResponseDto sendEmail(ContactEmail payload) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("<nombre>", payload.getNombre());
            params.put("<correo>", payload.getCorreo());
            params.put("<celular>", payload.getCelular());
            params.put("<especialidad>", payload.getEspecialidad());
            params.put("<comentario>", payload.getComentario());


            emailService.send(MessagesEnum.PUBLIC_WEB_CONTACT, "clinicaisosalud@gmail.com", params);
            return GenericResponseDto.builder()
                    .message("OK")
                    .details(List.of("Mensaje enviado correctamente, pronto te contactaremos!"))
                    .build();
        } catch (Exception e) {
            return GenericResponseDto.builder()
                    .message("NOK")
                    .details(List.of("Error al enviar correo, favor reintentar mas tarde"))
                    .build();
        }
    }

}
