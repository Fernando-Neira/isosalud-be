package cl.isosalud.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class PersonDto {

    private Integer id;
    private String rut;
    private String firstName;
    private String lastName;
    private String email;
    private AddressDto addressInfo;
    private String gender;
    private String prevision;
    private Integer phone;
    private Integer cellphone;
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate dateOfBirth;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateCreated;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateUpdated;

}