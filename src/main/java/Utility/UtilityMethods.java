package Utility;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import pageObject_model.CartPage;
import pageObject_model.Order;

/**
 * Utility class containing reusable Selenium WebDriver helper methods
 * such as waits, JavaScript actions, element highlighting, and screenshots.
 */
public class UtilityMethods {

    private WebDriver driver;

    /**
     * Constructor to initialize WebDriver.
     * 
     * @param driver WebDriver instance
     */
    public UtilityMethods(WebDriver driver) {
        this.driver = driver;
    }

    @FindBy(css = ".mb-3")
    private List<WebElement> products;

    @FindBy(xpath = "//li//button[@routerlink='/dashboard/cart']")
    private WebElement cart;
    
    @FindBy(css="[routerlink*='myorders']") private WebElement order;
    

    /**
     * Highlights a WebElement temporarily with a red border and yellow background.
     *
     * @param driver  WebDriver instance
     * @param element Element to be highlighted
     * @throws InterruptedException if thread sleep is interrupted
     */
    public static void highlightElement(WebDriver driver, WebElement element) throws InterruptedException {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].setAttribute('style', 'border: 3px solid red; background: yellow;');", element);
        Thread.sleep(500);
        js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, "");
    }

    /**
     * Captures a screenshot of a specific WebElement and saves it as a PNG file.
     *
     * @param element WebElement to capture
     * @throws IOException if file write fails
     */
    public static void takeElementScreenshotOfElement(WebElement element) throws IOException {
        File file = element.getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(file,
                new File(System.getProperty("user.dir")+"//reports//screenshot.png"));
    }

    /**
     * Waits until the element located by the given locator is visible.
     *
     * @param locator By locator for the element
     */
    public void waitForElementToAppear(By locator) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Waits until the given WebElement is visible.
     *
     * @param element WebElement to wait for
     */
    public void waitForElementToAppear(WebElement element) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    /**
     * Scrolls the page until the specified WebElement is in view and moves slightly further down.
     *
     * @param element WebElement to scroll into view
     */
    public void javaScriptScrollIntoView(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView(true); window.scrollBy(0, 500);", element);
    }

    /**
     * Waits for the element to disappear. (Currently uses Thread.sleep).
     *
     * @param element WebElement to wait for disappearance
     * @throws InterruptedException if thread sleep is interrupted
     */
    public void waitFor(WebElement element) throws InterruptedException {
        Thread.sleep(1000);
        // Better: use WebDriverWait with invisibilityOf()
        // WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        // wait.until(ExpectedConditions.invisibilityOf(element));
    }
    public void waitForElementToDisappear(WebElement element) throws InterruptedException {
       
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
         wait.until(ExpectedConditions.invisibilityOf(element));
    }
    /**
     * Waits until the given WebElement is clickable.
     *
     * @param element WebElement to wait for
     */
    public void waitForElementToBeClickable(WebElement element) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    /**
     * Scrolls the page until the specified WebElement is in view (aligns at bottom).
     *
     * @param element WebElement to scroll into view
     */
    public void javaScriptScroll(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView(false);", element);
    }

    /**
     * Performs a JavaScript click on the given WebElement.
     *
     * @param element WebElement to click
     */
    public void javaScriptClick(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", element);
    }

    /**
     * Navigates to the Cart page by scrolling, waiting for clickability, and performing a JS click.
     */
    public void goToCart() {
        javaScriptScroll(cart);
        waitForElementToBeClickable(cart);
        javaScriptClick(cart);
        new CartPage(driver);
    }
/*Navigate to order page
 * 
 */
    public Order goToOrder() {
    	javaScriptClick(order);
    	//order.click();
    	return new Order(driver);
    	
    }
    public void takeScreenShot(String testcaseName) throws IOException {
    	TakesScreenshot ts=(TakesScreenshot)driver;
    	File source=ts.getScreenshotAs(OutputType.FILE);
    	File destination=new File(System.getProperty("user.dir")+"//reports//"+testcaseName+".png");
    	FileUtils.copyFile(source,destination);
    	
    }
   
}