package com.testBase;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import io.github.bonigarcia.wdm.WebDriverManager;

public class BrowserFactory {

	public WebDriver createBrowserInstance(String browser) {
		WebDriver driver = null;
		if (browser.contains("Chrome")) {
			WebDriverManager.chromedriver().setup();
			//WebDriverManager.chromedriver().clearDriverCache().setup();
			ChromeOptions option = new ChromeOptions();
			
			if(browser.contains("headless"))
					{
         	option.addArguments("--headless=new");
			}
			option.addArguments("--incognito");
			option.addArguments("--disable-popup-blocking");
			option.addArguments("--start-maximized");
			option.addArguments("--remote-allow-origins=*");
		//	option.addArguments("--headless=new"); // Ensures compatibility with latest Chrome versions
			option.addArguments("--disable-features=WebSockets"); // Disable WebSockets if not needed
			option.addArguments("--disable-dev-shm-usage"); // Prevents crashes in Docker/Linux
			option.addArguments("--no-sandbox");
			driver = new ChromeDriver(option);
			driver.manage().window().setSize(new Dimension(1440, 900));// used for full screen in headless face some issue which is not solved by maximized method
			DesiredCapabilities chrome = new DesiredCapabilities();
			chrome.setCapability("browserName", "chrome");
			chrome.setCapability("timezone", "UTC+05:30");
			chrome.setCapability("geoLocation", "IN");

		} else if (browser.equalsIgnoreCase("firefox")) {
			WebDriverManager.firefoxdriver().setup();
			FirefoxOptions foptions = new FirefoxOptions();
			foptions.addArguments("-private");
			DesiredCapabilities fiefox = new DesiredCapabilities();
			fiefox.setCapability("browserName", "fiefox");
			fiefox.setCapability("timezone", "UTC+05:30");
			fiefox.setCapability("geoLocation", "IN");
			driver = new FirefoxDriver(foptions);

		}
		if (browser.equalsIgnoreCase("edge")) {
			WebDriverManager.edgedriver().setup();
			EdgeOptions eOption = new EdgeOptions();
			eOption.addArguments("--private");
			DesiredCapabilities edge = new DesiredCapabilities();
			edge.setCapability("browserName", "edge");
			edge.setCapability("timezone", "UTC+05:30");
			edge.setCapability("geoLocation", "IN");
			driver = new EdgeDriver(eOption);
		}
		return driver;

	}
}
