package utils;

import base.DriverManager;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.ByteArrayInputStream;

public class TestListener implements AfterTestExecutionCallback {

    @Override
    public void afterTestExecution(ExtensionContext context) {

        if (context.getExecutionException().isPresent()) {

            WebDriver driver = DriverManager.getDriver();

            if (driver != null) {

                byte[] screenshot = ((TakesScreenshot) driver)
                        .getScreenshotAs(OutputType.BYTES);

                String testName = context.getDisplayName();

                Allure.addAttachment(
                        "Failure Screenshot - " + testName,
                        new ByteArrayInputStream(screenshot)
                );
            }
        }
    }
}
