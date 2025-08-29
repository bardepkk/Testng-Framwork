package com.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.testBase.DriverFactory;
import com.testBase.ExtentFactory;

public class ActionEngine {
//utility for reused
	private final String COLOR_PASS = "#28a745"; // Green
	private final String COLOR_FAIL = "#dc3545"; // Red
	private final String COLOR_INFO = "#17a2b8"; // Blue
	private final String COLOR_EXPECTED = "#6610f2"; // Indigo
	private final String COLOR_ACTUAL = "#fd7e14"; // Orange
	private final String FONT_STYLE = "font-family:Arial, sans-serif;font-size:14px;";

	private final int TIMEOUT = Integer.parseInt(getConfig("element.wait.timeout", "20"));

	private static String getConfig(String key, String defaultValue) {
		try (FileInputStream fis = new FileInputStream("config.properties")) {
			Properties prop = new Properties();
			prop.load(fis);
			return prop.getProperty(key, defaultValue);
		} catch (IOException e) {
			return defaultValue;
		}
	}

	public void sendKeys_custom(WebElement element, String fieldName, String valueToBeSent) throws IOException {
		try {
			waitForElement(element, TIMEOUT,fieldName);
			element.clear();
			element.sendKeys(valueToBeSent);
			log(Status.PASS, "<b>" + fieldName + "</b> => Value entered: <i>" + valueToBeSent + "</i>");
		} catch (Exception e) {
			reportFailure("<b>" + fieldName + "</b> => Failed to enter value: " + e.getMessage(), true);
		}
	}


	public void click_custom(WebElement element, String fieldName) throws IOException {
		try {
			waitForElement(element, TIMEOUT,fieldName);
			element.click();
			log(Status.PASS, "<b>Clicked on:</b> " + fieldName);
		} catch (Exception e) {
			reportFailure("Click failed on: <b>" + fieldName + "</b> due to: " + e.getMessage(), true);
		}
	}

	public void clickWithJS_custom(WebElement element, String fieldName) throws IOException {
		try {
			JavascriptExecutor js = (JavascriptExecutor) DriverFactory.getInstance().getDriver();
			js.executeScript("arguments[0].click();", element);
			log(Status.PASS, "<b>JS Clicked on:</b> " + fieldName);
		} catch (Exception e) {
			reportFailure("JS Click failed on: <b>" + fieldName + "</b> due to: " + e.getMessage(), true);
		}
	}

	public void moveToElement_custom(WebElement element, String fieldName) throws IOException {
		try {
			JavascriptExecutor js = (JavascriptExecutor) DriverFactory.getInstance().getDriver();
			js.executeScript("arguments[0].scrollIntoView(true);", element);
			Actions actions = new Actions(DriverFactory.getInstance().getDriver());
			actions.moveToElement(element).perform();
			log(Status.PASS, "<b>Hovered over:</b> " + fieldName);
		} catch (Exception e) {
			reportFailure("Hover failed on: <b>" + fieldName + "</b> due to: " + e.getMessage(), true);
		}
	}

	public void selectDropdownByVisibleText_custom(WebElement element, String fieldName, String visibleText) throws IOException {
		try {
			waitForElement(element, TIMEOUT,fieldName);
			Select select = new Select(element);
			select.selectByVisibleText(visibleText);
			log(Status.PASS, "<b>" + fieldName + "</b> => Selected value: <i>" + visibleText + "</i>");
		} catch (Exception e) {
			reportFailure("<b>" + fieldName + "</b> => Dropdown selection failed: " + e.getMessage(), true);
		}
	}

	public void verifyElementText_custom(WebElement element, String expectedText, String fieldName) throws IOException { 
	    String actualText = "";  // Declare actualText at the start to ensure it is always initialized.
	    try {
	        // Wait for the element to be visible before proceeding
	        waitForElement(element, TIMEOUT,fieldName);

	        // Get and clean actual text from the element
	        actualText = element.getText().trim().replaceAll("\\n", " ");
	        
	        // Clean expected text (to handle multiple new lines or spaces)
	        String expected = expectedText.trim().replaceAll("\\n", " ");

	        // Assertion to check if the actual text equals the expected text
	        Assert.assertEquals(actualText, expected, fieldName + " => Text mismatch.");

	        // Log success if assertion passes
	        logPassWithScreenshot(String.format("<b>%s</b> => Text matched. <br><span style='color:%s;'>Expected:</span> <i>%s</i><br><span style='color:%s;'>Actual:</span> <i>%s</i>",
	                fieldName, COLOR_EXPECTED, expected, COLOR_ACTUAL, actualText));

	    } catch (AssertionError e) {
	        // Catch assertion error and log failure with detailed mismatch
	        actualText = actualText.isEmpty() ? "N/A" : actualText; // Set a default value if actualText is empty
	        reportFailure(String.format("<b>%s</b> => Text mismatch. <br><span style='color:%s;'>Expected:</span> <i>%s</i><br><span style='color:%s;'>Actual:</span> <i>%s</i>",
	                fieldName, COLOR_EXPECTED, expectedText, COLOR_ACTUAL, actualText), true);
	    } catch (Exception e) {
	        // Catch any other exceptions and log the failure with exception details
	        reportFailure(String.format("<b>%s</b> => Verification failed: %s", fieldName, e.getMessage()), true);
	    }
	}


	public void logPassWithScreenshot(String message) throws IOException {
		ExtentFactory.getInstance().getExtent().log(Status.PASS,
				"<div style='" + FONT_STYLE + "color:" + COLOR_PASS + ";'>" + message + "</div>",
				MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot()).build());
	}

	public String getAlphaNumericString(int length) {
		String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvxyz";
		return generateRandomString(chars, length);
	}

	public String getCharacterString(int length) {
		String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvxyz";
		return generateRandomString(chars, length);
	}

	private String generateRandomString(String chars, int length) {
		StringBuilder sb = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			int index = (int) (Math.random() * chars.length());
			sb.append(chars.charAt(index));
		}
		return sb.toString();
	}

	public boolean isElementPresent_custom(WebElement element, String fieldName) throws IOException {
		try {
			boolean displayed = element.isDisplayed();
			log(Status.PASS, "<b>" + fieldName + "</b> ==> Element presence: <i>" + displayed + "</i>");
			return displayed;
		} catch (Exception e) {
			reportFailure("<b>" + fieldName + "</b> ==> Presence check failed: " + e.getMessage(), true);
			return false;
		}
	}

	private void reportFailure(String message, boolean withScreenshot) throws IOException {
		if (withScreenshot) {
			ExtentFactory.getInstance().getExtent().log(Status.FAIL,
					"<div style='" + FONT_STYLE + "color:" + COLOR_FAIL + ";'>" + message + "</div>",
					MediaEntityBuilder.createScreenCaptureFromPath(captureScreenshot()).build());
		} else {
			ExtentFactory.getInstance().getExtent().log(Status.FAIL,
					"<div style='" + FONT_STYLE + "color:" + COLOR_FAIL + ";'>" + message + "</div>");
		}
		Assert.fail(message);
	}

	private String captureScreenshotold() {
		try {
			File src = ((TakesScreenshot) DriverFactory.getInstance().getDriver()).getScreenshotAs(OutputType.FILE);
			String timestamp = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss").format(new Date());
			String path = System.getProperty("user.dir") + "/Reports/Screenshots/" + timestamp + ".png";
			FileUtils.copyFile(src, new File(path));
			return path;
		} catch (IOException e) {
			return "Screenshot capture failed: " + e.getMessage();
		}
	}
	
	private String captureScreenshot() {
	    try {
	        File src = ((TakesScreenshot) DriverFactory.getInstance().getDriver()).getScreenshotAs(OutputType.FILE);
	        String timestamp = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss").format(new Date());
	        String screenshotDir = System.getProperty("user.dir") + "/Reports/Screenshots";
	        new File(screenshotDir).mkdirs(); // Ensure folder exists
	        String screenshotPath = screenshotDir + "/" + timestamp + ".png";
	        FileUtils.copyFile(src, new File(screenshotPath));
	        
	        // Return relative path from the HTML report's perspective
	        return "Screenshots/" + timestamp + ".png";
	        
	    } catch (IOException e) {
	        return "Screenshot capture failed: " + e.getMessage();
	    }
	}



	
	protected void waitForElement(WebElement element, int timeoutInSeconds, String fieldName ) throws IOException {
		
		 WebDriver driver = DriverFactory.getInstance().getDriver();
		    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
		try {
	        // Wait until the element is visible
	        wait.until(ExpectedConditions.visibilityOf(element));
	        log(Status.INFO, "<b>" + element+">>" +fieldName+ "</b> is visible after waiting up to " + timeoutInSeconds + " seconds.");
	    } catch (Exception e) {
	        String errorMessage = "<b>" + element +">>"+fieldName+ "</b> Element not visible within " + timeoutInSeconds + " seconds. Exception: <i>" + e.getMessage() + "</i>";
	        reportFailure(errorMessage, true);
	    }
	}
	
	

	public void waitForElementPresence(WebElement element, int seconds) {
		WebDriver driver = DriverFactory.getInstance().getDriver();
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(seconds));
		String locatorId = element.getAttribute("id");
		if (locatorId != null && !locatorId.isEmpty()) {
			wait.until(ExpectedConditions.presenceOfElementLocated(By.id(locatorId)));
		}
	}

	private void log(Status status, String message) {
		ExtentFactory.getInstance().getExtent().log(status,
				"<div style='" + FONT_STYLE + "color:" + getColorByStatus(status) + ";'>" + message + "</div>");
	}

	private String getColorByStatus(Status status) {
		switch (status) {
			case PASS:
				return COLOR_PASS;
			case FAIL:
				return COLOR_FAIL;
			case INFO:
				return COLOR_INFO;
			default:
				return "#000000";
		}
	}
}