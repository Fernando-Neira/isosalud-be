package cl.isosalud.service.util;

import lombok.experimental.UtilityClass;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@UtilityClass
public class MessageParamsResolver {

    public static String resolve(String message, Map<String, Object> params) {
        AtomicReference<String> newMessage = new AtomicReference<>(message);

        params.forEach((s, o) -> {
            newMessage.set(newMessage.get().replace(s, String.valueOf(o)));
        });

        return newMessage.get();
    }

}
