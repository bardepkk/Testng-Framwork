package pageObject_model;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.aventstack.extentreports.ExtentTest;

import Utility.UtilityMethods;

public class Order extends UtilityMethods {
	WebDriver driver;

	public Order(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	@FindBy(xpath = "//td[2]")
	private List<WebElement> orderProducts;
	@FindBy(xpath = "//button[text()='View']")
	private WebElement view;
	@FindBy(xpath = "//button[text()='Delete']")
	private WebElement delete;
	@FindBy(xpath = "//h1[text()='Your Orders']")
	private WebElement yourOrder;
	
	@FindBy (css=".mt-4")private WebElement no_Order;
	@FindBy(css="[aria-label='Orders Deleted Successfully']")private WebElement OderDeltedSuccessful;
	
	public boolean verifyMutipleOrderProducts(List<String> productNames) {
	    goToOrder();
	    List<String> actualProducts = orderProducts.stream()
	            .map(product -> product.getText().trim())
	            .toList();

	    return productNames.stream()
	            .allMatch(expected -> actualProducts.stream()
	                    .anyMatch(actual -> actual.equalsIgnoreCase(expected)));
	}

	public boolean verifyOrderProduct(String product_name) {
		goToOrder();
		return orderProducts.stream().anyMatch(product -> product.getText().equalsIgnoreCase(product_name));

	}

	public void deleteOrder(String productName) {
	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	    int deleteCount = 0; // ✅ counter for deleted entries

	    while (true) {
	        String xpath = "//tr[td[2][normalize-space()='" + productName + "']]//button[contains(text(),'Delete')]";
	        List<WebElement> deleteButtons = driver.findElements(By.xpath(xpath));

	        if (deleteButtons.isEmpty()) {
	            if (deleteCount == 0) {
	                System.out.println("No products found with name '" + productName + "'.");
	            } else {
	                System.out.println("Total deleted entries for '" + productName + "': " + deleteCount);
	            }
	            break;
	        }

	        WebElement deleteBtn = deleteButtons.get(0);

	        // ✅ Wait until clickable
	        wait.until(ExpectedConditions.elementToBeClickable(deleteBtn));

	        deleteBtn.click();

	        // ✅ Wait until row disappears OR success toast shows up
	        wait.until(ExpectedConditions.or(
	            ExpectedConditions.stalenessOf(deleteBtn),
	            ExpectedConditions.visibilityOf(OderDeltedSuccessful)
	        ));

	        deleteCount++; // increment counter
	        System.out.println("Deleted one '" + productName + "' entry (count so far: " + deleteCount + ").");
	    }
	}



	    public void deleteMultipleOrders(String... productNames) {
	        for (String productName : productNames) {
	            System.out.println("Deleting product: " + productName);

	            // Keep deleting until no row with productName remains
	            while (true) {
	                String xpath = "//tr[td[2][normalize-space()='" + productName + "']]//button[contains(text(),'Delete')]";
	                List<WebElement> deleteButtons = driver.findElements(By.xpath(xpath));

	                if (deleteButtons.isEmpty()) {
	                    System.out.println("✅ All products with name '" + productName + "' are deleted (or none found).");
	                    break;
	                }

	                WebElement deleteBtn = deleteButtons.get(0);
	                deleteBtn.click();

	                // Explicit wait until that row disappears
	                new WebDriverWait(driver, Duration.ofSeconds(5))
	                        .until(ExpectedConditions.stalenessOf(deleteBtn));
	            
	        }
	    }

	}
	    


	public boolean verifyDeleteOrder(String product_name) {
	        // Ensure order page is loaded
		  WebDriverWait wait=new WebDriverWait(driver,Duration.ofSeconds(10));
		  wait.until(ExpectedConditions.or(
	                ExpectedConditions.invisibilityOfElementLocated(By.xpath("//h1[text()='Your Orders']")),
	                ExpectedConditions.visibilityOf(yourOrder)));
	        if (yourOrder.isDisplayed()) {
	            //  If "Your Orders" is visible, check the product list
	            return orderProducts.stream()
	                    .noneMatch(product -> product.getText().equalsIgnoreCase(product_name));
	        } else {
	            //  If order page not shown, we can assume deletion succeeded
	            System.out.println("Order deleted successfully, no 'Your Orders' section found.");
	            return true;
	        }
}
	public boolean verifyDeleteOrderNEW(String product_name) {
	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

	    try {
	        // Wait until either:
	        // 1. The 'Your Orders' header is gone from the DOM
	        // 2. Or the product disappears from the orders list
	        wait.until(driver1 -> {
	            // Case 1: Header completely gone
	            if (driver1.findElements(By.xpath("//h1[text()='Your Orders']")).isEmpty()) {
	                System.out.println("Order deleted successfully, 'Your Orders' section is not visible.");
	                return true;
	            }
	            // Case 2: Header is present -> check product list
	            return orderProducts.stream()
	                    .noneMatch(product -> product.getText().equalsIgnoreCase(product_name));
	        });

	        return true;
	    } catch (Exception e) {
	        System.out.println("Failed while verifying order deletion: " + e.getMessage());
	        return false;
	    }
	}

	}
