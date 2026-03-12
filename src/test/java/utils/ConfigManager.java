package utils;

import io.github.cdimascio.dotenv.Dotenv;

public class ConfigManager {

    private static final Dotenv dotenv =
            Dotenv.configure()
                    .ignoreIfMissing()
                    .load();

    private static String getValue(String key) {
        // 1️⃣ First check System Environment (CI)
        String envValue = System.getenv(key);
        if (envValue != null && !envValue.isEmpty()) {
            return envValue;
        }

        // 2️⃣ Fallback to .env file (local)
        return dotenv.get(key);
    }

    public static String getBaseUrl() {
        return getValue("BASE_URL");
    }

    public static String getApiBaseUrl() {
        return getValue("API_BASE_URL");
    }

    public static String getUsername() {
        return getValue("APP_USERNAME");
    }

    public static String getPassword() {
        return getValue("APP_PASSWORD");
    }
}

