package com.testBase;

import org.openqa.selenium.WebDriver;

public class DriverFactory {

	ThreadLocal<WebDriver> driver = new ThreadLocal<WebDriver>();

	// Design pattern--> repressent best practices
	// singleton design pattern-only one instance exist ever, provide global access
	// to that instance by creating getinstance method
	// factory design pattern
	private DriverFactory() {

	}

	private static DriverFactory instance = new DriverFactory();

	public static DriverFactory getInstance() {
		return instance;
	}

	// factory design pattern we are going to design separate factory method for
	// creating object and create object by calling that method.
	public WebDriver getDriver() {
		return driver.get();
	}

	public void setDriver(WebDriver driverParm) {
		driver.set(driverParm);
	}

	public void closeDriver() {
	//	driver.get().close();
		driver.get().quit();
		driver.remove();
	}
}
