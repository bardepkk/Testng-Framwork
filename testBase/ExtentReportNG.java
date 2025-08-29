 package com.testBase;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.utility.PropertiesOperations;

/**
 * @author: pranay barde
 * 
 */
public class ExtentReportNG {
	
	static ExtentReports extent;

	public static ExtentReports setupExtentReportold() throws Exception {
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyy HH-mm-ss");
		Date date = new Date();
		String actualDate = format.format(date);
		
		String reportPath = System.getProperty("user.dir")+
				"/Reports/ExecutionReport_"+actualDate+".html";
		
		ExtentSparkReporter sparkReport = new ExtentSparkReporter(reportPath);
		
		 extent = new ExtentReports();
		 extent.attachReporter(sparkReport);
		
		sparkReport.config().setDocumentTitle("PARABANK AUTOMATION REPORT");// Report link title
		sparkReport.config().setTheme(Theme.DARK);
		sparkReport.config().setReportName("AUTOMATION TEST RESULTS OF PARABANK"); // name shown in Right of Extent Report
		
		extent.setSystemInfo("Executed on Environment: ", PropertiesOperations.getPropertyValueByKey("url"));
		extent.setSystemInfo("Executed on Browser: ", PropertiesOperations.getPropertyValueByKey("browser"));
		extent.setSystemInfo("Executed on OS: ", System.getProperty("os.name"));
		extent.setSystemInfo("Executed by User: ", System.getProperty("user.name"));

		return extent;
	}

	public static ExtentReports setupExtentReport() throws Exception {
	    // Timestamp for unique archive filename
	    SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");
	    String actualDate = format.format(new Date());

	    // Base paths
	    String baseDir = System.getProperty("user.dir") + "/Reports";
	    String screenshotsDir = baseDir + "/Screenshots";
	    String archiveDir = baseDir + "/archive";

	    // Ensure directories exist
	    new File(baseDir).mkdirs();
	    new File(screenshotsDir).mkdirs();
	    new File(archiveDir).mkdirs();

	    // Jenkins-compatible report path (fixed file name)
	    String reportPath = baseDir + "/index.html";

	    // Create timestamped archive file
	    String archiveReportPath = archiveDir + "/ExecutionReport_" + actualDate + ".html";

	    // Create Spark Reporter
	    ExtentSparkReporter sparkReport = new ExtentSparkReporter(reportPath);
	    extent = new ExtentReports();
	    extent.attachReporter(sparkReport);

	    // Config report appearance
	    sparkReport.config().setDocumentTitle("PARABANK AUTOMATION REPORT");
	    sparkReport.config().setTheme(Theme.DARK);
	    sparkReport.config().setReportName("AUTOMATION TEST RESULTS OF PARABANK");

	    // System info
	    extent.setSystemInfo("Executed on Environment: ", PropertiesOperations.getPropertyValueByKey("url"));
	    extent.setSystemInfo("Executed on Browser: ", PropertiesOperations.getPropertyValueByKey("browser"));
	    extent.setSystemInfo("Executed on OS: ", System.getProperty("os.name"));
	    extent.setSystemInfo("Executed by User: ", System.getProperty("user.name"));

	    // Flush later after test execution, then copy to archive
	    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
	        extent.flush();
	        try {
	            Files.copy(Paths.get(reportPath), Paths.get(archiveReportPath), StandardCopyOption.REPLACE_EXISTING);
	        } catch (IOException e) {
	            System.out.println("Failed to archive Extent Report: " + e.getMessage());
	        }
	    }));

	    return extent;
	}


}
