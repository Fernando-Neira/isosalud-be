package cl.isosalud.service.service.publicweb.publics;

import cl.isosalud.service.dto.ContactEmail;
import cl.isosalud.service.dto.GenericResponseDto;

public interface PublicService {
    GenericResponseDto sendEmail(ContactEmail payload);
}
