package cl.isosalud.service.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class LocationDto {

    private Integer id;
    private String name;
    private String romanNumber;
    private String abbreviation;
    private List<ComunneDto> communes;

    @NoArgsConstructor
    @AllArgsConstructor
    @Setter
    @Getter
    @Builder
    public static class ComunneDto {
        private int id;
        private String name;
    }

}