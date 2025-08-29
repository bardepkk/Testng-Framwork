package pageObject_model;

import java.io.IOException;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import Utility.UtilityMethods;

public class SuccessPage extends  UtilityMethods {
	WebDriver driver;

	public SuccessPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	} 
@FindBy(css=".hero-primary")private WebElement successful_message;


public String getSuccessful_message() throws IOException, InterruptedException {
	highlightElement(driver,successful_message );
System.out.println(successful_message.getText());
takeElementScreenshotOfElement(successful_message);
return successful_message.getText();}

}
