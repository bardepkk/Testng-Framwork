package StepDefination;

import java.io.IOException;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.testng.Assert;

import TestComponents.TestBase;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pageObject_model.CartPage;
import pageObject_model.PaymentPage;
import pageObject_model.Product_Catelog;
import pageObject_model.SuccessPage;
import pageObject_model.LoginPage;

public class StepDefination extends TestBase {
	
	public LoginPage loginPage;
	public Product_Catelog product_catelog;
	public CartPage cartpage;
	public SuccessPage successPage;

	@Given("I landed on login page")
	public void iLandedOnLoginPage() throws IOException {
		loginPage = launchApplication();
	}

	@Given("^user logs in with username (.+) and password (.+)$")
	public void userLogsInWithUsernameAndPassword(String userName, String password) throws InterruptedException {
		product_catelog = loginPage.login(userName, password);
	}

	@When("^user adds product (.+) to the cart$")
	public void userAddsProductToTheCart(String productName) throws InterruptedException {
		List<WebElement> products = product_catelog.getProductList();
		product_catelog.getproductByName(productName);
		cartpage = product_catelog.addProductsToCart(productName);
	}

	@When("^proceeds to checkout (.+) and submit the order$")
	public void proceedsToCheckoutAndSubmitTheOrder(String productName) throws InterruptedException, IOException {
		boolean match = cartpage.verifyCartProduct(productName);
		Assert.assertEquals(match, true, "Product in cart did not match expected!");

		PaymentPage paymentPage = cartpage.Checkout();
		paymentPage.selectCountry("INDIA");
		successPage = paymentPage.submitPayment();
	}

	@Then("the order should be placed successfully with confirmation message {string}")
	public void thenTheOrderShouldBePlacedSuccessfullyWithConfirmationMessage(String expectedMessage) throws IOException, InterruptedException {
		Assert.assertTrue(
			successPage.getSuccessful_message().equalsIgnoreCase(expectedMessage),
			"Order success message did not match!"
		);
		driver.close();	}
}
