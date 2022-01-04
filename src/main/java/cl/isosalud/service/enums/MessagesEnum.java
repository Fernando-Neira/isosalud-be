package cl.isosalud.service.enums;

import lombok.Getter;

@Getter
public enum MessagesEnum {

    APPOINTMENT_CREATED("APPOINTMENT_CREATED", ""),
    APPOINTMENT_EDITED("APPOINTMENT_EDITED", ""),
    OTP_SEND("OTP_SEND", ""),
    PUBLIC_WEB_CONTACT("PUBLIC_WEB_CONTACT", "");


    private final String key;
    private final String defaultMsg;

    MessagesEnum(String key, String defaultMsg) {
        this.key = key;
        this.defaultMsg = defaultMsg;
    }


}
