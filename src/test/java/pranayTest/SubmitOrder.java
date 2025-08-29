package pranayTest;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import TestComponents.Retry;
import TestComponents.TestBase;
import pageObject_model.CartPage;
import pageObject_model.Order;
import pageObject_model.PaymentPage;
import pageObject_model.Product_Catelog;
import pageObject_model.SuccessPage;

public class SubmitOrder extends TestBase {

	@Test(dataProvider = "getData1", groups = "purchase", retryAnalyzer = Retry.class, description = "Place an order and verify success message")
	public void SubmitOrder(HashMap<String, String> input) throws InterruptedException, IOException {

		Product_Catelog product_catelog = loginPage.login(input.get("email"), input.get("password"));

		List<WebElement> products = product_catelog.getProductList();
		product_catelog.getproductByName(input.get("product_name"));
		CartPage cartpage = product_catelog.addProductsToCart(input.get("product_name"));

		boolean match = cartpage.verifyCartProduct(input.get("product_name"));
		Assert.assertEquals(true, match);

		PaymentPage paymentPage = cartpage.Checkout();
		paymentPage.selectCountry("INDIA");

		SuccessPage successPage = paymentPage.submitPayment();
		Assert.assertTrue(successPage.getSuccessful_message().equalsIgnoreCase("THANKYOU FOR THE ORDER."),
				"Order success message did not match!");
	}

	@Test(dependsOnMethods = "SubmitOrder", dataProvider = "getData2", retryAnalyzer = Retry.class, description = "Verify order history and deletion")
	public void orderHistory(String email, String password, String product_name) throws InterruptedException {

		Product_Catelog product_catelog = loginPage.login(email, password);
		Order order = product_catelog.goToOrder();

		Assert.assertTrue(order.verifyOrderProduct(product_name));
		order.deleteOrder(product_name);
		Assert.assertTrue(order.verifyDeleteOrderNEW(product_name), "Product Order not deleted");
	}

	@DataProvider
	public Object[][] getData2() {
		return new Object[][] { { "bardepkk@gmail.com", "Star1234", "Pant" },
				{ "pranaybn@gmail.com", "Abcd1234@", "ADIDAS ORIGINAL" } };
	}

	@DataProvider
	public Object[][] getData1() throws IOException {
		List<HashMap<String, String>> data = getJsonDataToMap(
				System.getProperty("user.dir") + "//src//test//resources//testData//TestData.json");

		return new Object[][] { { data.get(0) }, { data.get(1) } };
	}
}
