package utils;

import base.BaseTest;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.ByteArrayInputStream;

public class TestListener implements AfterTestExecutionCallback {

    @Override
    public void afterTestExecution(ExtensionContext context) {

        // Check if test failed
        if (context.getExecutionException().isPresent()) {

            System.out.println("Test Failed: Taking Screenshot...");

            try {
                attachScreenshot();
            } catch (Exception e) {
                System.out.println("Screenshot capture failed: " + e.getMessage());
            }
        }
    }

    private void attachScreenshot() {

        if (BaseTest.driver != null) {

            byte[] screenshot = ((TakesScreenshot) BaseTest.driver)
                    .getScreenshotAs(OutputType.BYTES);

            Allure.addAttachment(
                    "Failure Screenshot",
                    "image/png",
                    new ByteArrayInputStream(screenshot),
                    ".png"
            );
        }
    }
}
