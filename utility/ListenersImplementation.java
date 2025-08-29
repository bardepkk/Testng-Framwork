package com.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.IRetryAnalyzer;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.testBase.DriverFactory;
import com.testBase.ExtentFactory;
import com.testBase.ExtentReportNG;

/**
 * @author: pranay barde
 */ 
public class ListenersImplementation implements ITestListener,IRetryAnalyzer{
//	JiraOperations jiraOps = new JiraOperations();
	static ExtentReports report;
		   ExtentTest test;
		   static int TOTALTCCOUNT;
			static int PASSTCCOUNT;
			static int FAILTCCOUNT;
			static int UNTESTEDTCCOUNT;
			private long startTime;
			private long endTime;
			private long totalTime;
			private long seconds;
			private long minutes;
			private long hours;
			Map<String, String> timeCalculation = new HashMap<String, String>();
			int retryLimit =1;
			public int counter =0;
			public int maxAttempt =1;
			
			public boolean retry(ITestResult result) {
				if(counter<maxAttempt) {
					counter++;
					return true;
				}
				return false;
			}
		   
	public void onTestStart(ITestResult result) {
		startTime = System.currentTimeMillis();
		System.out.println("---Execution has been Started--");
		//before each test case
		test = report.createTest(result.getMethod().getMethodName());
		ExtentFactory.getInstance().setExtent(test);
	}

	public void onTestSuccess(ITestResult result) {
	    ExtentFactory.getInstance().getExtent().log(Status.PASS, "Test Case: " + result.getMethod().getMethodName() + " is Passed.");

	    File src = ((TakesScreenshot) DriverFactory.getInstance().getDriver()).getScreenshotAs(OutputType.FILE);

	    // ✅ Fix: Remove Invalid Characters for Windows
	    SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss"); // Use `_` instead of `:`
	    String actualDate = format.format(new Date());

	    String screenshotPath = System.getProperty("user.dir") + "/Reports/Screenshots/" + actualDate + "image.png";
	    File dest = new File(screenshotPath);

	    try {
	        // ✅ Fix: Read and Release the File Lock Before Copying
	        try (FileInputStream fis = new FileInputStream(src)) {
	            byte[] buffer = new byte[fis.available()];
	            fis.read(buffer);
	        }

	        // ✅ Fix: Ensure Unique File Name to Avoid Conflicts
	        if (dest.exists()) {
	            dest = new File(System.getProperty("user.dir") + "/Reports/Screenshots/" + actualDate + "_duplicate.png");
	        }

	        FileUtils.copyFile(src, dest);
	        ExtentFactory.getInstance().getExtent().addScreenCaptureFromPath(screenshotPath, "Test case pass screenshot");
	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally {
	        ExtentFactory.getInstance().removeExtentObject();
	    }
	}


	public void onTestFailure(ITestResult result) {
		ExtentFactory.getInstance().getExtent().log(Status.FAIL, "Test Case: "+result.getMethod().getMethodName()+ " is Failed.");
		ExtentFactory.getInstance().getExtent().log(Status.FAIL, result.getThrowable());
		
		//add screenshot for failed test.
		File src = ((TakesScreenshot)DriverFactory.getInstance().getDriver()).getScreenshotAs(OutputType.FILE);
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyy HH-mm-ss");
		Date date = new Date();
		String actualDate = format.format(date);
		
		String screenshotPath = System.getProperty("user.dir")+
				"/Reports/Screenshots/"+actualDate+"image"+".png";
		File dest = new File(screenshotPath);
		
		try {
			FileUtils.copyFile(src, dest);
		} catch (IOException e) {
			e.printStackTrace();
		}
		ExtentFactory.getInstance().getExtent().addScreenCaptureFromPath(screenshotPath, "Test case failure screenshot");
		ExtentFactory.getInstance().removeExtentObject();
}

	public void onTestSkipped(ITestResult result) {
		ExtentFactory.getInstance().getExtent().log(Status.SKIP, "Test Case: "+result.getMethod().getMethodName()+ " is skipped.");
		
		
		File src = ((TakesScreenshot)DriverFactory.getInstance().getDriver()).getScreenshotAs(OutputType.FILE);
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyy HH-mm-ss");
		Date date = new Date();
		String actualDate = format.format(date);
		
		String screenshotPath = System.getProperty("user.dir")+
				"/Reports/Screenshots/"+actualDate+"image"+".png";
		File dest = new File(screenshotPath);
		
		try {
			FileUtils.copyFile(src, dest);
		} catch (IOException e) {
			e.printStackTrace();
		}
		ExtentFactory.getInstance().getExtent().addScreenCaptureFromPath(screenshotPath, "Test case failure screenshot");
		ExtentFactory.getInstance().removeExtentObject();

	}

	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
	}

	public void onTestFailedWithTimeout(ITestResult result) {
	}

	public void onStart(ITestContext context) {
		try {
			 report = ExtentReportNG.setupExtentReport();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void onFinish(ITestContext context) {
		//close extent
		UNTESTEDTCCOUNT = TOTALTCCOUNT - (PASSTCCOUNT + FAILTCCOUNT);
		endTime = System.currentTimeMillis();
		totalTime = endTime - startTime;
		minutes = TimeUnit.MILLISECONDS.toMinutes(totalTime) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(totalTime));	
		seconds = TimeUnit.MILLISECONDS.toSeconds(totalTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(totalTime));
		hours = TimeUnit.MILLISECONDS.toHours(totalTime);
		timeCalculation.put("seconds", String.valueOf(String.format("%02d", seconds)));
		timeCalculation.put("minutes", String.valueOf(String.format("%02d", minutes)));
		timeCalculation.put("hours", String.valueOf(String.format("%02d", hours)));
		
		
		report.flush();
	}

}