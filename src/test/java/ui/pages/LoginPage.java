package ui.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import utils.ConfigManager;

public class LoginPage extends BasePage {

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    @FindBy(id = "username")
    public WebElement username;

    @FindBy(id = "password")
    public WebElement password;

    @FindBy(xpath = "//button[@type='button']")
    public WebElement continueButton;

    @FindBy(xpath = "//button[@type='submit']")
    public WebElement loginButton;

    public void login()  {
        username.sendKeys("admin");
        //username.sendKeys(ConfigManager.getUsername()); --> Owner
        continueButton.click();
        password.sendKeys(ConfigManager.getPassword());
        //password.sendKeys(ConfigManager.getPassword());
        loginButton.click();

    }
}
