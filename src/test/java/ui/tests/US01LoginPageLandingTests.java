package ui.tests;

import base.BaseTest;
import org.apache.http.util.Asserts;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ui.pages.LoginPage;
import utils.ConfigManager;

import java.time.Duration;
import java.util.Set;

public class US01LoginPageLandingTests extends BaseTest {

    @Test
    @DisplayName("AC01 - User lands on Login page when navigating to base URL")
    void verifyUserLandsOnLoginPage() {

        // driver.get(ConfigManager.getBaseUrl());

        LoginPage loginpage = new LoginPage(driver);

        Assertions.assertTrue(loginpage.username.isDisplayed(), "User should be redirected to Login page");

    }

    @Test
    @DisplayName("AC02 - Verify URL is automatically redirected to login path")
    void verifyUrlIsRedirectedToLoginPath() {

        // 1. Navigate to base URL
        // driver.get(ConfigManager.getBaseUrl());

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
    @DisplayName("AC03 - Username section elements are visible")
    void verifyUsernameSectionElementsAreVisible() {

        driver.get(ConfigManager.getBaseUrl());

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
    @DisplayName("AC04 - Password section appears after entering username")
    void verifyPasswordSectionElementsAreVisible() {

        driver.get(ConfigManager.getBaseUrl());

        LoginPage loginPage = new LoginPage(driver);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        loginPage.username.sendKeys(ConfigManager.getUsername());
        loginPage.continueButton.click();

        wait.until(ExpectedConditions.visibilityOf(loginPage.password));
        wait.until(ExpectedConditions.visibilityOf(loginPage.loginButton));

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

    @Test
    @DisplayName("AC05 - Verify password tooltip appears on hover")
    void verifyPasswordTooltipAppearsOnHover() {

        driver.get(ConfigManager.getBaseUrl());

        LoginPage loginPage = new LoginPage(driver);
        loginPage.username.sendKeys(ConfigManager.getUsername());
        loginPage.continueButton.click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(loginPage.password));
        wait.until(ExpectedConditions.visibilityOf(loginPage.eyeIcon));

        // Step 2: Hover over eye icon
        Actions actions = new Actions(driver);
        actions.moveToElement(loginPage.eyeIcon).perform();

        wait.until(ExpectedConditions.visibilityOf(loginPage.tooltip));
        wait.until(ExpectedConditions.textToBePresentInElement(loginPage.tooltip, "Show password"));


        // Step 3: Verify tooltip text

        String tooltipText = loginPage.tooltip.getText();
        Assertions.assertEquals("Show password", tooltipText, "Tooltip text should be 'Show password'");


//    Scenario: AC05 Show password tooltip appears on hover
//    Given user is on password step
//    When user hovers over the eye icon
//    Then tooltip text should be "Show password"

    }

    @Test
    @DisplayName("AC06 - Verify password visibility toggle changes tooltip text")
    void verifyPasswordVisibilityToggleChangesTooltipText() {

        driver.get(ConfigManager.getBaseUrl());

        LoginPage loginPage = new LoginPage(driver);
        loginPage.username.sendKeys(ConfigManager.getUsername());
        loginPage.continueButton.click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(loginPage.password));
        wait.until(ExpectedConditions.visibilityOf(loginPage.eyeIcon));

        Actions actions = new Actions(driver);

        // Before click -> "Show password"
        actions.moveToElement(loginPage.eyeIcon).perform();
        String beforeClickTooltipText = loginPage.tooltip.getText();
        Assertions.assertEquals("Show password", beforeClickTooltipText, "Tooltip text should be 'Show password'");

        // Click -> "Hide password"
        loginPage.eyeIcon.click();
        actions.moveToElement(loginPage.eyeIcon).perform();
        wait.until(ExpectedConditions.textToBePresentInElement(loginPage.tooltip, "Hide password"));
        Assertions.assertEquals("Hide password", loginPage.tooltip.getText(), "Tooltip text should be 'Hide password'");

        // Click again -> "Show password"
        loginPage.eyeIcon.click();
        actions.moveToElement(loginPage.eyeIcon).perform();
        wait.until(ExpectedConditions.textToBePresentInElement(loginPage.tooltip, "Show password"));
        Assertions.assertEquals("Show password", loginPage.tooltip.getText(), "Tooltip text should be 'Show password'");


//    Scenario: AC06 Password visibility toggle changes tooltip text
//    Given user is on password step
//    When user clicks the eye icon
//    Then tooltip (or aria-label/title) should be "Hide password"
//    When user clicks the eye icon again
//    Then tooltip (or aria-label/title) should be "Show password"

    }

    @Test
    @DisplayName("AC07 Learn more link opens \"openmrs.org\" in new tab")
    void verifyLearnMoreLinkOpensNewTab() {

        driver.get(ConfigManager.getBaseUrl());

        LoginPage loginPage = new LoginPage(driver);
        Assertions.assertTrue(loginPage.learnMoreLink.isDisplayed(), "Learn more link should be visible");
        // System.out.println("loginPage.learnMoreLink.isDisplayed() = " + loginPage.learnMoreLink.isDisplayed());

        String originalWindow = driver.getWindowHandle();

        loginPage.learnMoreLink.click();

        // Wait until new tab opens

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(driver -> driver.getWindowHandles().size() > 1);

        Set<String> allWindows = driver.getWindowHandles();
        for (String window : allWindows) {
            if (!window.equals(originalWindow)) {
                driver.switchTo().window(window);
                break;
            }
        }

        // Assertion: URL should be openmrs.org
        String actualUrl = driver.getCurrentUrl();
        Assertions.assertEquals("https://openmrs.org/", actualUrl);

        // Optional: switch back
        driver.switchTo().window(originalWindow);


//            Scenario: AC07 Learn more link opens "openmrs.org" in new tab
//    Given user is on the login page
//    When user clicks "Learn more"
//    Then a new tab should open
//    And new tab URL should be "https://openmrs.org/"

    }
}
