package ui.tests;

import base.BaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ui.pages.LoginPage;
import utils.ConfigManager;

import java.time.Duration;

public class US01LoginPageLandingTests extends BaseTest {

    @Test
    @DisplayName("AC1 - User lands on Login page when navigating to base URL")
    void verifyUserLandsOnLoginPage() {
        driver.get(ConfigManager.getBaseUrl());

        LoginPage loginpage = new LoginPage(driver);

        Assertions.assertTrue(loginpage.username.isDisplayed(), "User should be redirected to Login page");

    }

    @Test
    @DisplayName("AC2 - Verify URL is automatically redirected to login path")
    void verifyUrlIsRedirectedToLoginPath() {

        // 1. Navigate to base URL
        driver.get(ConfigManager.getBaseUrl());

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlContains("/login"));

        // 2. Get currentURL after redirection
        String currentUrl = driver.getCurrentUrl();

        // 3. Verify it contains loginPpath

        Assertions.assertEquals(
                "https://o3.openmrs.org/openmrs/spa/login",
                currentUrl,
                "URL does not match expected login redirect URL"
        );

    }
}








