package utils;

import io.github.cdimascio.dotenv.Dotenv;

public class ConfigManager {

    private static final Dotenv dotenv =
            Dotenv.configure()
                    .ignoreIfMissing()
                    .load();

    public static String getApiBaseUrl() {
        return dotenv.get("API_BASE_URL");
    }

    public static String getUsername() {
        return dotenv.get("USERNAME");
    }

    public static String getPassword() {
        return dotenv.get("PASSWORD");
    }
}