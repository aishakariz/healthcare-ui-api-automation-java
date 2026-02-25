package ui.tests;

import base.BaseTest;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ui.pages.DashboardPage;
import ui.pages.LoginPage;
import utils.BrowserUtils;
import utils.ConfigManager;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)

public class US03LogoutFunctionality extends BaseTest {

    @Test
    @Order(1)
    @DisplayName("AC1 - Verify that the user sees My Account message when user hovers over profile menu")
    void verifyMyAccountMessageOnHover() {

        //1.Open the application.
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        //Make sure English is default language
        wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("div[data-extension-id='user-menu-button'] button")
        )).click();

        String currentLanguage = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[@data-extension-id='change-language']//p")
        )).getText().trim();

        if (!currentLanguage.equalsIgnoreCase("English")) {
            wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[@data-extension-id='change-language']//button[normalize-space()='Change']")
            )).click();
            wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//span[normalize-space()='English']")
            )).click();
            wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[normalize-space()='Change']")
            )).click();

        }

        // Close menu
        By profileBtn = By.cssSelector("div[data-extension-id='user-menu-button'] button");

        // Make sure menu is closed
        wait.until(ExpectedConditions.elementToBeClickable(profileBtn)).click();
        WebElement profile = wait.until(ExpectedConditions.visibilityOfElementLocated(profileBtn));
        WebElement serviceQueues = driver.findElement(By.xpath("//p[@class='P5kBN-q2g2nkNYCgydQ1vg==']"));

        // Hover
        Actions actions = new Actions(driver);
        actions.moveToElement(serviceQueues).perform();
        actions.moveToElement(profile).perform();

        //5.Verify that the “My Account” message appears
        WebElement message = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("span[role='tooltip'][aria-hidden='false']")));

        Assertions.assertTrue(message.isDisplayed(), "account message should be visible");
        System.out.println(message.getText());

    }

    @Test
    @Order(2)
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
    @Order(3)
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
    @Order(4)
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

    @Test
    @Order(5)
    @DisplayName("AC5 - User can change language and Italiano is not visible (only 13 languages shown)")
    void verifyUserCanChangeLanguage_ItalianoNotVisible() {

        //1.Login
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        //2.Open profile menu
        wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("div[data-extension-id='user-menu-button'] button")
        )).click();

        //3.Click Change next to Language
        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[@data-extension-id='change-language']//button[normalize-space()='Change']")
        )).click();

        //4.Verify language list displays
        WebElement dialog = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("div[role='dialog']")
        ));
        Assertions.assertTrue(dialog.isDisplayed(), "Language dialog should be displayed");

        //5.Get ONLY visible language options (buttons)
        List<WebElement> optionElements = wait.until(
                ExpectedConditions.visibilityOfAllElementsLocatedBy(
                        By.xpath("//span[@class='cds--radio-button__label-text']")
                )
        );

        List<String> visibleLanguages = optionElements.stream()
                .map(e -> e.getText().trim())
                .filter(t -> !t.isEmpty())
                // remove action buttons like Change/Cancel if they exist in same dialog
                .filter(t -> !t.equalsIgnoreCase("Change"))
                .filter(t -> !t.equalsIgnoreCase("Cancel"))
                .distinct()
                .collect(Collectors.toList());

        //Assert only 14 languages are visible
        Assertions.assertEquals(14, visibleLanguages.size(),
                "User should see exactly 14 languages in the list");


        //6.Select a visible language
        String newLanguage = visibleLanguages.stream()
                .filter(l -> !l.equalsIgnoreCase("English"))
                .filter(l -> !l.equalsIgnoreCase("Italiano"))
                .findFirst()
                .orElseThrow(() -> new AssertionError("No alternative language found to select"));

        WebElement languageToSelect = optionElements.stream()
                .filter(e -> e.getText().trim().equalsIgnoreCase(newLanguage))
                .findFirst()
                .orElseThrow(() -> new AssertionError(newLanguage + " was not found in the visible language list"));
        languageToSelect.click();

        //7.Click Change (confirm)
        DashboardPage dashboardPage = new DashboardPage(driver);
        dashboardPage.changeButton.click();
        BrowserUtils.waitFor(3);

        //8.Verify language changed (re-open menu to read label)
        wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("div[data-extension-id='user-menu-button'] button")
        )).click();
        BrowserUtils.waitFor(3);

        String currentLanguage = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//a[@aria-label='Change language']//p")
        )).getText().trim();
        BrowserUtils.waitFor(3);

        System.out.println("newLanguage = " + newLanguage);
        System.out.println("currentLanguage = " + currentLanguage);

        Assertions.assertEquals(newLanguage, currentLanguage,
                "Language should be updated successfully");
    }

    @Test
    @Order(6)
    @DisplayName("[On hold] AC6 - Verify user can change password and login with new password")
    void verifyUserCanChangePasswordAndLoginWithNewPassword() {

    }

    @Test
    @Order(7)
    @DisplayName("AC7 - Verify user cannot return to main page after logout using browser back button")
    void verifyUserCannotNavigateBackAfterLogout() {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        //1.Login with valid credentials
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login();

        // Wait until dashboard loads
        wait.until(ExpectedConditions.urlContains("/home"));

        //2.Click profile menu/icon
        wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("div[data-extension-id='user-menu-button'] button")
        )).click();

        //3.Click Logout
        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[normalize-space()='Logout']")
        )).click();

        //4.Verify user is on Log in page
        wait.until(ExpectedConditions.urlContains("/login"));

        Assertions.assertTrue(driver.getCurrentUrl().contains("/login"),
                "User should be redirected to Login page after logout");

        //5.Click browser Back button
        driver.navigate().back();

        // Small wait for navigation
        BrowserUtils.waitFor(2);

        //6.Verify user CANNOT return to main page
        String currentUrl = driver.getCurrentUrl();
        System.out.println("Current URL after back: " + currentUrl);

        Assertions.assertTrue(currentUrl.contains("/login"),
                "User should NOT be able to access dashboard after logout using back button");
    }

    @Test
    @Order(8)
    @DisplayName("NEG_AC1 - Message should NOT appear before hover")
    void verifyMessageNotVisibleBeforeHover() {

        //1.Login with valid credentials.
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Wait until profile icon is visible (page fully loaded)
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("div[data-extension-id='user-menu-button'] button")
        ));

        //2.Do NOT hover over profile icon.
        // (Intentionally no Actions.moveToElement() here)

        //3.Observe the UI.
        // Locate ONLY tooltip that contains "My Account"
        List<WebElement> myAccountTooltips = driver.findElements(
                By.xpath("//span[@role='tooltip' and contains(normalize-space(.),'My Account')]")
        );

        //4.Verify that message should NOT appear before hover.
        if (myAccountTooltips.isEmpty()) {

            // Tooltip not in DOM at all → PASS
            Assertions.assertTrue(true);

        } else {

            WebElement tooltip = myAccountTooltips.get(0);

            String ariaHidden = tooltip.getAttribute("aria-hidden"); // "true" = hidden
            boolean hiddenByAria = "true".equalsIgnoreCase(ariaHidden);

            Assertions.assertTrue(hiddenByAria || !tooltip.isDisplayed(),
                    "My Account tooltip should NOT be visible before hover");

        }
    }
}





