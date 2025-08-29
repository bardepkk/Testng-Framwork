package TestComponents;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ExtentReportsNg {

	private static ExtentReports extent;

	public static ExtentReports getReportObject() {
		if (extent == null) { // ensures only one instance

			// Timestamp for unique report names
			String timestamp = new SimpleDateFormat("yyyy_MM_dd_HHmmss").format(new Date());

			// Extent report path -> inside reports/extent
			String path = System.getProperty("user.dir") + "/reports/extent/ExecutionReport_" + timestamp + ".html";

			ExtentSparkReporter reporter = new ExtentSparkReporter(path);
			reporter.config().setReportName("Web Automation Results");
			reporter.config().setDocumentTitle("Test Results");
			reporter.config().setTheme(Theme.DARK);

			extent = new ExtentReports();
			extent.attachReporter(reporter);

			// Add useful system info
			extent.setSystemInfo("Tester", "Pranay Barde");
			extent.setSystemInfo("Framework", "Selenium TestNG");
			extent.setSystemInfo("OS", System.getProperty("os.name"));
			extent.setSystemInfo("Java Version", System.getProperty("java.version"));
		}
		return extent;
	}
}
