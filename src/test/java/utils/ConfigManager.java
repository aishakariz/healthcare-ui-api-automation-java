package utils;

public class ConfigManager {

    public static String getBaseUrl() {
        return System.getenv("BASE_URL");
    }

    public static String getApiBaseUrl() {
        return System.getenv("API_BASE_URL");
    }

    public static String getUsername() {
        return System.getenv("USERNAME");
    }

    public static String getPassword() {
        return System.getenv("PASSWORD");
    }

    public static String getBrowser() {
        return System.getenv("BROWSER");
    }
}