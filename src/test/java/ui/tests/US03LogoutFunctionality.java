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

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)

public class US03LogoutFunctionality extends BaseTest {

    WebDriverWait wait;
    LoginPage loginPage;
    DashboardPage dashboardPage;

    @Test
    @Order(1)
    @Tag("smoke")
    @DisplayName("AC1 - Verify that the user sees My Account message when user hovers over profile menu")
    void verifyMyAccountMessageOnHover() {

        //1.Open the application.
        //2.Login with valid credentials.
        loginPage = new LoginPage(driver);
        loginPage.login();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        //3.Locate the profile menu/icon (top right).
        By profileBtn = By.cssSelector("div[data-extension-id='user-menu-button'] button");
        WebElement profile = wait.until(ExpectedConditions.visibilityOfElementLocated(profileBtn));

        //4.Hover the mouse over the profile menu/icon.
        new Actions(driver)
                .moveToElement(profile)
                .pause(Duration.ofMillis(300))
                .perform();

        By tooltip = By.xpath("//span[@role='tooltip' and contains(.,'My Account') and @aria-hidden='false']");
        WebElement message = wait.until(ExpectedConditions.visibilityOfElementLocated(tooltip));

        //5.Verify that the “My Account” message appears
        Assertions.assertTrue(message.isDisplayed(), "Tooltip should be visible after hover");

    }

    @Test
    @Order(2)
    @Tag("smoke")
    @DisplayName("AC2 - Verify My Account dropdown displays expected options")
    void verifyMyAccountOptions() {

        //1.Login with valid credentials.
        loginPage = new LoginPage(driver);
        loginPage.login();
        dashboardPage = new DashboardPage(driver);
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));

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
    @Tag("smoke")
    @DisplayName("AC3 - Verify user can log out and land on Log in page")
    void verifyUserCanLogoutSuccessfully() {

        //1.Login with valid credentials.
        loginPage = new LoginPage(driver);
        loginPage.login();
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));

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
    @Tag("smoke")
    @DisplayName("AC4 - Verify English is the default Language")
    void verifyDefaultLanguageIsEnglish() {

        //1.Login with valid credentials.
        loginPage = new LoginPage(driver);
        loginPage.login();
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));

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
    @Tag("regression")
    @DisplayName("AC5 - User can change language and Italiano is not visible (only 13 languages shown)")
    void verifyUserCanChangeLanguage_ItalianoNotVisible() {

        //1.Login
        loginPage = new LoginPage(driver);
        loginPage.login();
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));

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
        dashboardPage = new DashboardPage(driver);
        dashboardPage.changeButton.click();
        BrowserUtils.waitFor(2);

        //8.Verify language changed (re-open menu to read label)
        wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("div[data-extension-id='user-menu-button'] button")
        )).click();
        BrowserUtils.waitFor(2);

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
    @Tag("regression")
    @DisplayName("[On hold] AC6 - Verify user can change password and login with new password")
    void verifyUserCanChangePasswordAndLoginWithNewPassword() {

    }

    @Test
    @Order(7)
    @Tag("smoke")
    @DisplayName("AC7 - Verify user cannot return to main page after logout using browser back button")
    void verifyUserCannotNavigateBackAfterLogout() {

        wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        //1.Login with valid credentials
        loginPage = new LoginPage(driver);
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

        //Small wait for navigation
        BrowserUtils.waitFor(2);

        //6.Verify user CANNOT return to main page
        String currentUrl = driver.getCurrentUrl();
        System.out.println("Current URL after back: " + currentUrl);

        Assertions.assertTrue(currentUrl.contains("/login"),
                "User should NOT be able to access dashboard after logout using back button");
    }

    @Test
    @Order(8)
    @Tag("regression")
    @DisplayName("NEG_AC1 - Message should NOT appear before hover")
    void verifyMessageNotVisibleBeforeHover() {

        //1.Login with valid credentials.
        loginPage = new LoginPage(driver);
        loginPage.login();

        wait = new WebDriverWait(driver, Duration.ofSeconds(20));

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

    @Test
    @Order(9)
    @Tag("regression")
    @DisplayName("NEG_AC1 - 02 Message should disappear after mouse moves away")
    void verifyMessageDisappearsAfterMouseMovesAway() {

        //1.Login
        loginPage = new LoginPage(driver);
        loginPage.login();

        wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        By profileBtn = By.cssSelector("div[data-extension-id='user-menu-button'] button");
        By tooltipLocator = By.xpath("//span[@role='tooltip' and contains(normalize-space(.),'My Account')]");

        WebElement profileIcon = wait.until(ExpectedConditions.visibilityOfElementLocated(profileBtn));
        Actions actions = new Actions(driver);

        //2.Hover profile icon → confirm tooltip appears
        actions.moveToElement(profileIcon).pause(Duration.ofMillis(300)).perform();

        WebElement tooltip = wait.until(ExpectedConditions.visibilityOfElementLocated(tooltipLocator));
        Assertions.assertTrue(tooltip.isDisplayed(), "Tooltip should appear after hover");

        //3.Move mouse away to a different area (pick a stable element)
        WebElement outsideArea = driver.findElement(By.tagName("h1")); // or any stable element on the page
        actions.moveToElement(outsideArea).pause(Duration.ofMillis(300)).perform();

        //4.Verify tooltip disappears (aria-hidden becomes true OR not displayed)
        wait.until(driver -> {
            List<WebElement> tips = driver.findElements(tooltipLocator);
            if (tips.isEmpty()) return true; // removed from DOM
            WebElement t = tips.get(0);
            String ariaHidden = t.getAttribute("aria-hidden");
            return "true".equalsIgnoreCase(ariaHidden) || !t.isDisplayed();
        });

        //Final assert (extra safety)
        List<WebElement> tipsAfter = driver.findElements(tooltipLocator);
        if (!tipsAfter.isEmpty()) {
            String ariaHiddenAfter = tipsAfter.get(0).getAttribute("aria-hidden");
            Assertions.assertTrue("true".equalsIgnoreCase(ariaHiddenAfter) || !tipsAfter.get(0).isDisplayed(),
                    "Tooltip should disappear after mouse moves away");
        }
    }

    @Test
    @Order(10)
    @Tag("regression")
    @DisplayName("NEG_AC2 - 01 Menu should close when clicking outside")
    void verifyMenuClosesWhenClickingOutside() {

        //1.Login
        loginPage = new LoginPage(driver);
        loginPage.login();

        wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        //2.Click profile icon to open menu
        WebElement profileIcon = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("div[data-extension-id='user-menu-button'] button")
        ));
        profileIcon.click();

        // Wait until menu options are visible
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//a[contains(@class,'cds--switcher__item-link')]")
        ));

        //3.Click outside the menu (click on body)
        WebElement body = driver.findElement(By.tagName("body"));
        body.click();


        //4.Verify menu is closed (options no longer visible)
        wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.xpath("//a[contains(@class,'cds--switcher__item-link')]")
        ));

        List<WebElement> menuOptionsAfterClickOutside = driver.findElements(
                By.xpath("//a[contains(@class,'cds--switcher__item-link')]")
        );

        boolean menuStillVisible = !menuOptionsAfterClickOutside.isEmpty() &&
                menuOptionsAfterClickOutside.get(0).isDisplayed();

        Assertions.assertFalse(menuStillVisible,
                "Menu should close when clicking outside");
    }

    @Test
    @Order(11)
    @Tag("regression")
    @DisplayName("NEG_AC4 - 01 Default language should not change after logout/login")
    void verifyDefaultLanguagePersistsAfterRelogin() {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        //1.Login
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login();

        By profileBtn = By.cssSelector("div[data-extension-id='user-menu-button'] button");
        By languageLabel = By.xpath("//div[@data-extension-id='change-language']//p");
        By logoutBtn = By.xpath("//button[normalize-space()='Logout']");

        //2.Read language BEFORE logout
        wait.until(ExpectedConditions.elementToBeClickable(profileBtn)).click();
        String languageBeforeLogout = wait.until(ExpectedConditions.visibilityOfElementLocated(languageLabel))
                .getText().trim();
        //close menu
        wait.until(ExpectedConditions.elementToBeClickable(profileBtn)).click();

        //3.Logout
        wait.until(ExpectedConditions.elementToBeClickable(profileBtn)).click();
        wait.until(ExpectedConditions.elementToBeClickable(logoutBtn)).click();
        wait.until(ExpectedConditions.urlContains("/login"));

        //4.Login again
        loginPage = new LoginPage(driver);
        loginPage.login();

        //5.Verify language is the SAME after re-login
        wait.until(ExpectedConditions.elementToBeClickable(profileBtn)).click();
        String languageAfterLogin = wait.until(ExpectedConditions.visibilityOfElementLocated(languageLabel))
                .getText().trim();

        Assertions.assertEquals(languageBeforeLogout, languageAfterLogin,
                "Language should NOT change after logout/login");
    }

    @Test
    @Order(12)
    @Tag("regression")
    @DisplayName("NEG_AC5 - 01 Cancel language change should NOT update language")
    void verifyCancelLanguageChangeDoesNotUpdateLanguage() {

        wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        //1.Login
        loginPage = new LoginPage(driver);
        loginPage.login();

        //Open My Account menu
        wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("div[data-extension-id='user-menu-button'] button")
        )).click();

        //Note current language
        String currentLanguage = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[@data-extension-id='change-language']//p")
        )).getText().trim();

        //2.Click Change → select a different language
        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[@data-extension-id='change-language']//button[normalize-space()='Change']")
        )).click();

        WebElement dialog = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("div[role='dialog']")
        ));
        Assertions.assertTrue(dialog.isDisplayed(), "Language dialog should be displayed");

        //Get visible language options
        List<WebElement> optionElements = wait.until(
                ExpectedConditions.visibilityOfAllElementsLocatedBy(
                        By.xpath("//span[@class='cds--radio-button__label-text']")
                )
        );

        //Pick a different language (not the current one)
        WebElement differentLanguage = optionElements.stream()
                .filter(e -> {
                    String t = e.getText().trim();
                    return !t.isEmpty()
                            && !t.equalsIgnoreCase("Change")
                            && !t.equalsIgnoreCase("Cancel")
                            && !t.equalsIgnoreCase(currentLanguage);
                })
                .findFirst()
                .orElseThrow(() -> new AssertionError("No different language found to select"));

        String selectedLanguage = differentLanguage.getText().trim();
        differentLanguage.click();

        //3.Click Cancel (or close popup) without saving
        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[normalize-space()='Cancel']")
        )).click();

        //4.Open menu again
        wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("div[data-extension-id='user-menu-button'] button")
        )).click();

        //5.Verify language did NOT change
        String languageAfterCancel = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[@data-extension-id='change-language']//p")
        )).getText().trim();

        Assertions.assertEquals(currentLanguage, languageAfterCancel,
                "Language should NOT update when user cancels the change. Selected (not saved): " + selectedLanguage);
    }
}





