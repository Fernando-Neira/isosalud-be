package cl.isosalud.service.dto;

import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class AddressDto {

    private int id;
    private int communeId;
    private String street;
    private String commune;
    private RegionDto region;

    @NoArgsConstructor
    @AllArgsConstructor
    @Setter
    @Getter
    @Builder
    public static class RegionDto {
        private int id;
        private String name;
        private String romanNumber;
        private String abbreviation;
    }

}