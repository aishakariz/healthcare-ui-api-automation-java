package ui.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class DashboardPage extends BasePage {

    public DashboardPage(WebDriver driver) {
        super(driver);
    }

    @FindBy(xpath = "//div[@data-extension-id='user-menu-button']")
    public WebElement profileIcon;

    @FindBy(xpath = "//a[@class='cds--switcher__item-link']")
    public List<WebElement> myAccountOptions;

    @FindBy(xpath = "//button[@type='submit']")
    public WebElement changeButton;


}
