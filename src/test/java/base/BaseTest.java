package base;

import io.qameta.allure.Allure;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import utils.ConfigManager;

import java.io.ByteArrayInputStream;
import java.time.Duration;

public class BaseTest {

    protected static WebDriver driver;

    @BeforeEach
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get(ConfigManager.getBaseUrl());
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }

    public static void attachScreenShot() {

        if (driver != null) {
            byte[] screenshot = ((TakesScreenshot) driver)
                    .getScreenshotAs(OutputType.BYTES);
            Allure.addAttachment("Screenshot",
                    new ByteArrayInputStream(screenshot));
        }
    }
}
