package pranayTest;

import java.io.IOException;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.IRetryAnalyzer;
import org.testng.annotations.Test;

import TestComponents.Retry;
import TestComponents.TestBase;
import pageObject_model.CartPage;
import pageObject_model.PaymentPage;
import pageObject_model.Product_Catelog;
import pageObject_model.SuccessPage;

public class ErrorValidation  extends TestBase {
@Test(groups="ErrorHandling",retryAnalyzer=Retry.class)
	public void ErrorValidation() throws InterruptedException, IOException {
		loginPage.login("bardepkk@gmail.com", "Star123456");
		
		loginPage.getErrorMessage();
		System.out.println(loginPage.getErrorMessage());
		Assert.assertEquals("Incorrect email or password.", loginPage.getErrorMessage(),"Test Pass Sucessefully"); 
	}
@Test(retryAnalyzer=Retry.class,groups="ErrorHandling")
public void ErrorNoProductFound() throws InterruptedException, IOException {

	String product_name = "Pant";

	Product_Catelog product_catelog = loginPage.login("bardepkk@gmail.com", "Star1234");

	List<WebElement> products = product_catelog.getProductList();
	product_catelog.getproductByName(product_name);
	CartPage cartpage = product_catelog.addProductsToCart(product_name);

	boolean match = cartpage.verifyCartProduct("Action");
	Assert.assertFalse( match,"Product not present in cart");
	

}

}
