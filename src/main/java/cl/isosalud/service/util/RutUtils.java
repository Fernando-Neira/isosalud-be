package cl.isosalud.service.util;

import cl.isosalud.service.dto.RutValidationDto;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RutUtils {

    public static RutValidationDto validaRut(String rut) {
        Pattern pattern = Pattern.compile("^[0-9]+-[0-9kK]$");
        Matcher matcher = pattern.matcher(rut);

        if (!matcher.matches()) {
            return RutValidationDto.builder()
                    .isFormatValid(false)
                    .build();
        }

        String[] stringRut = rut.split("-");
        return RutValidationDto.builder()
                .isFormatValid(true)
                .isDvValid(stringRut[1].toLowerCase().equals(dv(stringRut[0])))
                .build();
    }

    private static String dv(String rut) {
        int M = 0;
        int S = 1;
        int T = Integer.parseInt(rut);

        for (; T != 0; T = (int) Math.floor(T /= 10)) {
            S = (S + T % 10 * (9 - M++ % 6)) % 11;
        }

        return (S > 0) ? String.valueOf(S - 1) : "k";
    }

}
