package ui.tests;

import base.BaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ui.pages.DashboardPage;
import ui.pages.LoginPage;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

public class US03LogoutFunctionality extends BaseTest {

    @Test
    @DisplayName("AC1 - Verify that the user sees My Account message when user hovers over profile menu")
    void verifyMyAccountMessageOnHover() {
        //1.Open the application.
        LoginPage loginPage = new LoginPage(driver);
        DashboardPage dashboardPage = new DashboardPage(driver);

        //2.Login with valid credentials.
        loginPage.login();

        //3.Locate the profile menu/icon (top right).
        //4.Hover the mouse over the profile menu/icon.
        Actions actions = new Actions(driver);
        actions.moveToElement(dashboardPage.profileIcon).perform();

        //5.Verify that the “My Account” message appears
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement message = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("span[role='tooltip'][aria-hidden='false']")));

        Assertions.assertTrue(message.isDisplayed(), "account message should be visible");
        System.out.println(message.getText());

    }

    @Test
    @DisplayName("AC2 - Verify My Account dropdown displays expected options")
    void verifyMyAccountOptions() {

        //1.Login with valid credentials.
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login();

        DashboardPage dashboardPage = new DashboardPage(driver);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        //2.Click the profile menu/icon to open My Account menu.
        wait.until(ExpectedConditions.elementToBeClickable(dashboardPage.profileIcon)).click();

        //3.Observe the available options.
        wait.until(ExpectedConditions.visibilityOfAllElements(dashboardPage.myAccountOptions));

        //4.Verify that the My Account dropdown menu displays all expected options
        List<WebElement> rows = driver.findElements(
                By.xpath("//a[contains(@class,'cds--switcher__item-link')]"));

        List<String> optionTexts = rows.stream()
                .map(e -> e.getText().trim())
                .filter(t -> !t.isEmpty())
                .filter(t -> !t.equalsIgnoreCase("Change"))   // remove Change
                .collect(Collectors.toList());

        //list of expected options
        List<String> expectedOptions = List.of(
                "Super User",
                "English",
                "Password",
                "Logout"
        );

        //Verify that all the expected option is exited
        for (String expected : expectedOptions) {
            System.out.println(expected);
            Assertions.assertTrue(
                    optionTexts.stream().anyMatch(t -> t.contains(expected)),
                    expected + " option should be present"
            );
        }
    }

    @Test
    @DisplayName("AC3 - Verify user can log out and land on Log in page")
    void verifyUserCanLogoutSuccessfully() {

        //1.Login with valid credentials.
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        //2.Click profile menu/icon.
        wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("div[data-extension-id='user-menu-button'] button")
        )).click();

        //3.Click Logout.
        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[normalize-space()='Logout']")
        )).click();

        //4.Verify that the user can successfully log out
        wait.until(ExpectedConditions.urlContains("/login"));

        Assertions.assertTrue(driver.getCurrentUrl().contains("/login"),
                "User should be redirected to Login page after logout");
        System.out.println(driver.getCurrentUrl());
    }

    @Test
    @DisplayName("AC4 - Verify English is the default Language")
    void verifyDefaultLanguageIsEnglish() {

        //1.Login with valid credentials.
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        //2.Click profile menu/icon to open My Account menu.
        wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("div[data-extension-id='user-menu-button'] button")
        )).click();

        //3.Check the language shown.
        String languageText = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[@data-extension-id='change-language']//p")
        )).getText().trim();

        //4.Verify that the default language displayed
        System.out.println(languageText);
        Assertions.assertEquals("English", languageText,
                "Default language should be English");
    }
}
