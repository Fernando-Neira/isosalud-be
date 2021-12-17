package cl.isosalud.service.service.publicweb.publics;

import cl.isosalud.service.dto.ContactEmail;
import cl.isosalud.service.dto.GenericResponseDto;
import cl.isosalud.service.dto.SendOtpResponseDto;
import cl.isosalud.service.dto.SuccessfulAuthenticationResponseDto;

public interface PublicService {
    GenericResponseDto sendEmail(ContactEmail payload);
}
