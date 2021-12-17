package cl.isosalud.service.dto;

import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class ContactEmail {

    private String nombre;
    private String correo;
    private String celular;
    private String especialidad;
    private String comentario;


}