package pranayTest;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import io.github.bonigarcia.wdm.WebDriverManager;
import pageObject_model.LoginPage;

import static org.openqa.selenium.support.locators.RelativeLocator.with;
public class Standalone {

	public static void main(String[] args) throws InterruptedException, IOException {
		
		String product_name="Pant";
WebDriverManager.chromedriver().setup();
ChromeOptions option = new ChromeOptions();
option.addArguments("--incognito");
WebDriver driver=new ChromeDriver(option);


driver.get("https://rahulshettyacademy.com/client");
driver.manage().window().maximize();
//driver.manage().deleteAllCookies();
LoginPage login_pages=new LoginPage(driver);
driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
WebDriverWait wait= new WebDriverWait(driver, Duration.ofSeconds(10));
WebElement user=driver.findElement(By.cssSelector("#userEmail"));
highlightElement(driver,user);
user.sendKeys("bardepkk@gmail.com");
WebElement password=driver.findElement(By.cssSelector("#userPassword"));
highlightElement(driver,password);
password.sendKeys("Star1234");
WebElement login=driver.findElement(By.cssSelector("#login"));
highlightElement(driver,login);
login.click();


wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("mb-3")));
List<WebElement>products=driver.findElements(By.className("mb-3"));
WebElement prodSelected=products.stream().filter(prod->prod.findElement(By.cssSelector("b")).getText().equalsIgnoreCase(product_name)).findFirst().orElse(null);
WebElement addToCartBtn =  prodSelected.findElement(By.cssSelector(".card-body button:last-of-type"));
JavascriptExecutor js= (JavascriptExecutor)driver;
js.executeScript("arguments[0].scrollIntoView(true);",addToCartBtn);
highlightElement(driver, addToCartBtn);	
addToCartBtn.click();


wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#toast-container")));

wait.until(ExpectedConditions.invisibilityOf(driver.findElement(By.cssSelector(".ng-animating"))));



WebElement cart=driver.findElement(By.xpath("//li//button[@routerlink='/dashboard/cart']"));

((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(false);", cart);
wait.until(ExpectedConditions.elementToBeClickable(cart));

((JavascriptExecutor) driver).executeScript("arguments[0].click();", cart);

//for (WebElement product : products) {
//    String productName = product.findElement(By.tagName("b")).getText();
//    System.out.println(productName);
//
//    if (productName.equalsIgnoreCase("ADIDAS ORIGINAL")) {
//        WebElement nameElement = product.findElement(By.tagName("b"));  // product title
//        WebElement addToCartBtn =  product.findElement(By.cssSelector(".card-body button:last-of-type"));
//        highlightElement(driver, addToCartBtn);	
//   //     addToCartBtn.click();
//
//    driver.findElement(By.xpath("//li//button[@routerlink='/dashboard/cart']")).click();
//        
//    
//        break; // stop after adding required product
//    }
//}
List<WebElement>cartProduct= driver.findElements(By.cssSelector(".cartSection h3"));
boolean match=cartProduct.stream().anyMatch(product->product.getText().equalsIgnoreCase(product_name));
Assert.assertEquals(true, match);
driver.findElement(By.xpath("//button[text()='Checkout']")).click();
WebElement CountryTextbox=driver.findElement(By.xpath("//input[@placeholder='Select Country']"));
CountryTextbox.sendKeys("INDIA");
List<WebElement> countrys=driver.findElements(By.xpath("//section[@class='ta-results list-group ng-star-inserted']//button"));
for(WebElement country: countrys) {
String countryName=country.getText();
if(countryName.equalsIgnoreCase("INDIA")) {
js.executeScript("arguments[0].click();", country);}}



//WebElement placeOrder=driver.findElement(with(By.tagName("div")).below(CountryTextbox));
WebElement placeOrder=driver.findElement(By.cssSelector(".action__submit"));
wait.until(ExpectedConditions.visibilityOf(placeOrder));
js.executeScript("arguments[0].scrollIntoView(true);",placeOrder);
js.executeScript("arguments[0].click();",placeOrder);
WebElement successful_message=driver.findElement(By.cssSelector(".hero-primary"));
highlightElement(driver,successful_message);

ScreenShotOfElement(successful_message);
System.out.println(successful_message.getText());
Assert.assertEquals(successful_message.getText(), "Thankyou for the order.");


driver.quit();

	}
	public static void highlightElement(WebDriver driver, WebElement element) throws InterruptedException {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        // Add red border + yellow background
        js.executeScript("arguments[0].setAttribute('style', 'border: 3px solid red; background: yellow;');", element);
        Thread.sleep(500); // keep highlight for a moment
        // Remove style after highlight
        js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, "");
    }
	public static void ScreenShotOfElement(WebElement ele) throws IOException {
		File file=ele.getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(file, new File("C:/Users/Admin/eclipse-workspace/TestNG_Framework/test-output/screenshot/logo.png"));
	}
}
