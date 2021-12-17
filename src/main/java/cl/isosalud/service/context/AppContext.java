package cl.isosalud.service.context;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class AppContext {

    private static final Map<String, Object> context = new HashMap<>();

    public static String getKeyString(String key) {
        return (String) context.get(key);
    }

    public static String getKeyString(String key, String defaultValue) {
        return Optional.ofNullable((String) context.get(key)).orElse(defaultValue);
    }

    public static Object getKey(String key) {
        return context.get(key);
    }

    public static Object getKey(String key, Object defaultValue) {
        return Optional.ofNullable(context.get(key)).orElse(defaultValue);
    }

    public static Object putKey(String key, Object value) {
        return context.put(key, value);
    }

}
