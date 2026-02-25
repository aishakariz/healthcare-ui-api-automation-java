package ui.tests;

import base.BaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ui.pages.LoginPage;
import utils.ConfigManager;

import java.time.Duration;

public class US01LoginPageLandingTests extends BaseTest {

    @Test
    @Order(1)
    @DisplayName("AC1 - User lands on Login page when navigating to base URL")
    void verifyUserLandsOnLoginPage() {
        driver.get(ConfigManager.getBaseUrl());

        LoginPage loginpage = new LoginPage(driver);

        Assertions.assertTrue(loginpage.username.isDisplayed(), "User should be redirected to Login page");

    }

    @Test
    @Order(2)
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

    @Test
    @Order(3)
    @DisplayName("AC03 - Username section elements are visible")
    void verifyUsernameSectionElementsAreVisible() {

        LoginPage loginPage = new LoginPage(driver);
        // loginPage.login();

        System.out.println("loginPage.usernameLabel.isDisplayed() = " + loginPage.usernameLabel.isDisplayed());
        System.out.println("loginPage.continueButton.isDisplayed() = " + loginPage.continueButton.isDisplayed());

//        Given user is on the login page
//        Then "Username" label should be visible
//        And username input field should be visible
//        And "Continue" button should be visible

    }

    @Test
    @Order(4)
    @DisplayName("AC04 - Password section appears after entering username")
    void verifyPasswordSectionElementsAreVisible() {

        LoginPage loginPage = new LoginPage(driver);
        loginPage.username.sendKeys(ConfigManager.getUsername());
        loginPage.continueButton.click();
        System.out.println("loginPage.passwordLabel.isDisplayed() = " + loginPage.passwordLabel.isDisplayed());
        System.out.println("loginPage.password.isDisplayed() = " + loginPage.password.isDisplayed());
        System.out.println("loginPage.loginButton.isDisplayed() = " + loginPage.loginButton.isDisplayed());


//    Scenario: AC04 Password section appears after entering username
//    Given user is on the login page
//    When user enters username "admin"
//    And user clicks "Continue"
//    Then "Password" label should be visible
//    And password input field should be visible
//    And "Log in" button should be visible

    }
}








