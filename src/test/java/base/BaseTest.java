package base;

import io.qameta.allure.Allure;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import utils.ConfigManager;

import java.io.ByteArrayInputStream;
import java.time.Duration;

public class BaseTest {

    // Keep this so our existing code still works
    protected WebDriver driver;

    @BeforeEach
    public void setUp() {

        // Initialize thread-safe driver
        DriverManager.initDriver();

        // Bridge (so old code using 'driver' still works)
        driver = DriverManager.getDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
        driver.manage().window().maximize();
        driver.get(ConfigManager.getBaseUrl());
    }

    @AfterEach
    public void tearDown() {

        if (DriverManager.getDriver() != null) {
            DriverManager.quitDriver();
        }
    }

    // Screenshot method for Allure
    public static void attachScreenShot(String name) {

        WebDriver currentDriver = DriverManager.getDriver();

        if (currentDriver != null) {

            byte[] screenshot = ((TakesScreenshot) currentDriver)
                    .getScreenshotAs(OutputType.BYTES);

            Allure.addAttachment(name,
                    new ByteArrayInputStream(screenshot));
        }
    }
}