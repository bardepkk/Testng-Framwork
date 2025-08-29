package pageObject_model;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import Utility.UtilityMethods;

public class Product_Catelog extends UtilityMethods {
	WebDriver driver;

	public Product_Catelog(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	By productby = By.className("mb-3");
	By tost_message = By.cssSelector("#toast-container");
	By addToCart = By.cssSelector(".card-body button:last-of-type");

	@FindBy(css = ".mb-3")
	private List<WebElement> products;
	@FindBy(css = ".ng-animating")
	private WebElement spinner;

	public List<WebElement> getProductList() {
		waitForElementToAppear(productby);
		
		return products;

	}

	public WebElement getproductByName(String product_name) {

		WebElement prodSelected = getProductList().stream()
				.filter(prod -> prod.findElement(By.cssSelector("b")).getText().equalsIgnoreCase(product_name))
				.findFirst().orElse(null);
		return prodSelected;
	}

	public CartPage addProductsToCart(String product_name) throws InterruptedException {
		WebElement prodSelected = getproductByName(product_name);
		WebElement addToCartBtn = prodSelected.findElement(addToCart);
		
		javaScriptScroll(addToCartBtn);
	//	addToCartBtn.click();
		javaScriptClick(addToCartBtn);
		waitForElementToAppear(tost_message);
		waitFor(spinner);
return  new CartPage(driver);
	}

	public void selectProduct(String product_nameSelected) throws InterruptedException {
		for (WebElement product : products) {
			String productName = product.findElement(By.tagName("b")).getText(); // product title
			System.out.println(productName);

			if (productName.equalsIgnoreCase(product_nameSelected)) {

				WebElement addToCartBtn = product.findElement(addToCart);
				javaScriptScrollIntoView(addToCartBtn);
				addToCartBtn.click();
				waitForElementToAppear(tost_message);
				waitForElementToDisappear(spinner);
				break; // stop after adding required product
			}

		}
	}

}
