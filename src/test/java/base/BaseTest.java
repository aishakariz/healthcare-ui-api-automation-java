package base;

import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.ByteArrayInputStream;

public class BaseTest {

       protected static WebDriver driver;

       public static void attachScreenShot(){

           if(driver!=null){

               byte[] screenshot=((TakesScreenshot)driver)
                       .getScreenshotAs(OutputType.BYTES);


               Allure.addAttachment("Screenshot",
                       new ByteArrayInputStream(screenshot));

           }

       }




}
