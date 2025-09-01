package TestComponents;

import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.ITestContext;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.JavascriptExecutor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Listeners extends TestBase implements ITestListener {

    ExtentTest test;
    ExtentReports extent = ExtentReportsNg.getReportObject();

    // Save screenshots in reports/screenshots/<testName>/
    private static final String SCREENSHOT_DIR = System.getProperty("user.dir") + "/reports/screenshots/";

    static {
        new File(SCREENSHOT_DIR).mkdirs();
    }

    ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

    @Override
    public void onTestStart(ITestResult result) {
        test = extent.createTest(result.getMethod().getMethodName());
        extentTest.set(test);
        test.log(Status.INFO, "🔹 Test Started: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        test.log(Status.PASS, "✅ Test PASSED");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        extentTest.get().fail(result.getThrowable());

        WebDriver driver = null;
        try {
            driver = (WebDriver) result.getTestClass().getRealClass().getField("driver").get(result.getInstance());

            // highlight last known element if available
            WebElement failedElement = (WebElement) result.getAttribute("failedElement");
            if (failedElement != null) {
                highlightElement(failedElement, driver);
                test.warning("⚠️ Highlighted failed element before screenshot.");
            }

            // Save screenshot
            String screenshotPath = saveScreenshotToFile(result.getMethod().getMethodName(), driver);

            // Attach screenshot
            test.addScreenCaptureFromPath(screenshotPath, result.getMethod().getMethodName());

        } catch (Exception e) {
            test.warning("⚠️ Unable to capture screenshot: " + e.getMessage());
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        test.log(Status.SKIP, "⚠️ Test SKIPPED");
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {}

    @Override
    public void onStart(ITestContext context) {
        System.out.println("🚀 Execution Started: " + context.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        System.out.println("🏁 Execution Finished: " + context.getName());
        extent.flush();
    }

    // ===== Save Screenshot to File =====
    public String saveScreenshotToFile(String methodName, WebDriver driver) throws IOException {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String folderPath = SCREENSHOT_DIR + methodName + "/";
        new File(folderPath).mkdirs();

        String fileName = methodName + "_" + timestamp + ".png";
        Path destination = Path.of(folderPath + fileName);

        byte[] source = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        Files.write(destination, source);

        return destination.toString();
    }

    // ===== Highlight Element (red border) =====
    private void highlightElement(WebElement element, WebDriver driver) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].style.border='3px solid red'", element);
        } catch (Exception e) {
            System.out.println("⚠️ Could not highlight element: " + e.getMessage());
        }
    }
}
