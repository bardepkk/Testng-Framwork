package pageObject_model;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import Utility.UtilityMethods;

public class CartPage extends UtilityMethods {
	WebDriver driver;

	public CartPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	@FindBy(css = ".cartSection h3") 	private	List<WebElement> cartProduct;
	@FindBy(xpath = "//button[text()='Checkout']")
	private WebElement CheckoutBtn;

	public boolean verifyCartProduct(String product_name) {
		goToCart();
		return cartProduct.stream().anyMatch(product -> product.getText().equalsIgnoreCase(product_name));

	}

	public PaymentPage Checkout() {
		CheckoutBtn.click();
	return new PaymentPage(driver);
	}

}
