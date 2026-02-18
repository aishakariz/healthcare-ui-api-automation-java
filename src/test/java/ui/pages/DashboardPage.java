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

    @FindBy(xpath = "//div[@data-extension-id='change-language']//button[normalize-space()='Change']")
    public WebElement languageChangeButton;

    @FindBy(css = "div[role='dialog']")
    public WebElement languageDialog;

    @FindBy(xpath = "//div[@role='dialog']//button")
    public List<WebElement> languageOptions;

    @FindBy(xpath = "//div[@role='dialog']//button[normalize-space()='Change']")
    public WebElement changeButton;

    @FindBy(xpath = "//div[@data-extension-id='change-language']//p")
    public WebElement currentLanguageLabel;


    public void selectLanguage(String language) {
        languageOptions.stream()
                .filter(e -> e.getText().equalsIgnoreCase(language))
                .findFirst()
                .orElseThrow()
                .click();
    }

    public void confirmLanguageChange() {
        changeButton.click();
    }

    public String getCurrentLanguage() {
        return currentLanguageLabel.getText().trim();
    }
}
