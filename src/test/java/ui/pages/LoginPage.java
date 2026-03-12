package ui.pages;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.ConfigManager;

import java.time.Duration;

public class LoginPage extends BasePage {

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    @FindBy(id = "username")
    public WebElement username;

    @FindBy(id = "password")
    public WebElement password;

//    @FindBy(xpath = "//button[@type='button']")
//    public WebElement continueButton;
//
//    @FindBy(xpath = "//button[@type='submit']")
//    public WebElement loginButton;

    @FindBy(xpath = "//button[@type='submit' and normalize-space()='Continue']")
    public WebElement continueButton;

    @FindBy(xpath = "//button[@type='submit' and normalize-space()='Log in']")
    public WebElement loginButton;

    @FindBy(xpath = "//label[text()='Username']")
    public WebElement usernameLabel;

    @FindBy(xpath = "//label[text()='Password']")
    public WebElement passwordLabel;


    @FindBy(xpath = "//a[normalize-space()='Learn more']")
    public WebElement learnMoreLink;

    public void login() {
//        username.sendKeys(ConfigManager.getUsername());
//        continueButton.click();
//        password.sendKeys(ConfigManager.getPassword());
//        loginButton.click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        wait.until(ExpectedConditions.visibilityOf(username)).sendKeys(ConfigManager.getUsername());

        wait.until(ExpectedConditions.elementToBeClickable(continueButton)).click();

        wait.until(ExpectedConditions.visibilityOf(password)).sendKeys(ConfigManager.getPassword());

        wait.until(ExpectedConditions.visibilityOf(loginButton));

        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", loginButton);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", loginButton);
    }
}
