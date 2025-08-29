package pageObject_model;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import Utility.UtilityMethods;

public class PaymentPage extends UtilityMethods {
	WebDriver driver;

	public PaymentPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	@FindBy(xpath = "//section[@class='ta-results list-group ng-star-inserted']//button")
	private List<WebElement> countries;

	@FindBy(xpath = "//input[@placeholder='Select Country']")
	private WebElement CountryTextbox;
	@FindBy(css = ".action__submit")
	private WebElement placeOrder;

	public WebDriver getDriver() {
		return driver;
	}

	public void setDriver(WebDriver driver) {
		this.driver = driver;
	}

	public List<WebElement> getCountries() {
		return countries;
	}

	public void setCountries(List<WebElement> countries) {
		this.countries = countries;
	}

	public WebElement getCountryTextbox() {
		return CountryTextbox;
	}

	public void setCountryTextbox(WebElement countryTextbox) {
		CountryTextbox = countryTextbox;
	}

	public WebElement getPlaceOrder() {
		return placeOrder;
	}

	public void setPlaceOrder(WebElement placeOrder) {
		this.placeOrder = placeOrder;
	}

	public void selectCountry(String CountryName) {
		CountryTextbox.sendKeys(CountryName);

		for (WebElement country : countries) {
			String countryName = country.getText();
			if (countryName.equalsIgnoreCase("INDIA")) {
				javaScriptClick(country);

			}
		}
	}

	public SuccessPage submitPayment() throws InterruptedException {

		waitForElementToAppear(placeOrder);
		javaScriptScrollIntoView(placeOrder);
		highlightElement(driver, placeOrder);
		javaScriptClick(placeOrder);
		return new SuccessPage(driver);
	}

}
