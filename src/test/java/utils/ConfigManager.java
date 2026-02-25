package utils;

import io.github.cdimascio.dotenv.Dotenv;

public class ConfigManager {

    private static final Dotenv dotenv =
            Dotenv.configure()
                    .ignoreIfMissing()
                    .load();

    public static String getBaseUrl() {
        return dotenv.get("BASE_URL");
    }

    public static String getApiBaseUrl() {
        return dotenv.get("API_BASE_URL");
    }

    public static String getUsername() {
        return dotenv.get("APP_USERNAME");
    }

    public static String getPassword() {
        return dotenv.get("APP_PASSWORD");
    }
}