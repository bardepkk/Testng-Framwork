package pageObject_model;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import Utility.UtilityMethods;

public class LoginPage extends UtilityMethods {
	WebDriver driver;

	public LoginPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	@FindBy(css = "#userEmail")
	private WebElement user;
	@FindBy(css = "#userPassword")
	private WebElement passwordElement;
	@FindBy(css = "#login")
	private WebElement login;
	@FindBy(css="[class*='flyInOut']")private WebElement errorMessage;

	public Product_Catelog login(String userName, String Password) throws InterruptedException  {
		highlightElement(driver,user );
		user.sendKeys(userName);
		highlightElement(driver,passwordElement );
		passwordElement.sendKeys(Password);		
		login.click();
	return new Product_Catelog(driver);
	}

	public void goTo() {
		driver.get("https://rahulshettyacademy.com/client");

	}
public String getErrorMessage() {
	waitForElementToAppear(errorMessage);
	return errorMessage.getText();
	
}
}
