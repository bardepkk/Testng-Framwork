package TestComponents;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.bonigarcia.wdm.WebDriverManager;
import pageObject_model.LoginPage;

public class TestBase {
    public WebDriver driver;
    public LoginPage loginPage;
    public Properties properties;

    public WebDriver initializeDriver() throws IOException {
        properties = new Properties();
        FileInputStream file = new FileInputStream(
                System.getProperty("user.dir") + "//src//test//resources//GlobalData.properties");
        properties.load(file);

        String browserName = System.getProperty("browser") != null ? System.getProperty("browser")
                : properties.getProperty("browser");
        String runMode = System.getProperty("runMode") != null ? System.getProperty("runMode")
                : properties.getProperty("runMode");
        String hubUrl = properties.getProperty("hubUrl");

        if (browserName.equalsIgnoreCase("chrome")) {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--incognito");
            if (browserName.contains("headless")) options.addArguments("--headless");

            if (runMode.equalsIgnoreCase("grid")) {
                driver = new RemoteWebDriver(new URL(hubUrl), options);
            } else {
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver(options);
                driver.manage().window().setSize(new Dimension(1440, 900));
            }

        } else if (browserName.equalsIgnoreCase("firefox")) {
            FirefoxOptions options = new FirefoxOptions();
            options.addArguments("--incognito");
            if (browserName.contains("headless")) options.addArguments("--headless");

            if (runMode.equalsIgnoreCase("grid")) {
                driver = new RemoteWebDriver(new URL(hubUrl), options);
            } else {
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver(options);
                driver.manage().window().setSize(new Dimension(1440, 900));
            }

        } else if (browserName.equalsIgnoreCase("edge")) {
            EdgeOptions options = new EdgeOptions();
            options.addArguments("--inprivate");
            if (browserName.contains("headless")) options.addArguments("--headless");

            if (runMode.equalsIgnoreCase("grid")) {
                driver = new RemoteWebDriver(new URL(hubUrl), options);
            } else {
                System.setProperty("webdriver.edge.driver",
                        "C:\\Users\\Admin\\eclipse-workspace\\TestNG_Framework\\browser\\msedgedriver.exe");
                driver = new EdgeDriver(options);
                driver.manage().window().setSize(new Dimension(1440, 900));
            }
        }

        driver.manage().deleteAllCookies();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        return driver;
    }

    @BeforeMethod(alwaysRun = true)
    public LoginPage launchApplication() throws IOException {
        driver = initializeDriver();
        loginPage = new LoginPage(driver);
        loginPage.goTo();
        return loginPage;
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit(); // ✅ quit instead of close (safer with Grid)
        }
    }

    public List<HashMap<String, String>> getJsonDataToMap(String filePath) throws IOException {
        String jsonContent = FileUtils.readFileToString(new File(filePath), StandardCharsets.UTF_8);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(jsonContent, new TypeReference<List<HashMap<String, String>>>() {});
    }

    public String takeScreenShot(String testcaseName, WebDriver driver) throws IOException {
        TakesScreenshot ts = (TakesScreenshot) driver;
        File source = ts.getScreenshotAs(OutputType.FILE);
        File destination = new File(System.getProperty("user.dir") + "//reports//" + testcaseName + ".png");
        FileUtils.copyFile(source, destination);
        return destination.getAbsolutePath();
    }
    public ArrayList<String> getDataExcel(String testCaseName) throws IOException {

		ArrayList<String> a = new ArrayList<String>();

//FileInputStream fis=new FileInputStream(System.getProperty("user.dir")+"\\src\\test\\resources\\Testng test Data.xlsx");
		FileInputStream fis = new FileInputStream("C://Users//Admin//OneDrive//Desktop//Testng test Data.xlsx");
		XSSFWorkbook workbook = new XSSFWorkbook(fis);

		int count = workbook.getNumberOfSheets();

		for (int i = 0; i < count; i++) {
			if (workbook.getSheetName(i).equalsIgnoreCase("testData")) {
				XSSFSheet sheet = workbook.getSheetAt(i);
				Iterator<Row> rows = sheet.iterator();
				Row firstRow = rows.next();

				Iterator<Cell> ce = firstRow.cellIterator();
				int k = 0;
				int column = 0;
				while (ce.hasNext()) {
					Cell value = ce.next();
					if (value.getStringCellValue().equalsIgnoreCase("TestCases")) {
						column = k;
					}
					k++;

				}
				System.out.println(column);

				while (rows.hasNext()) {
					Row r = rows.next();
					if (r.getCell(column).getStringCellValue().equalsIgnoreCase(testCaseName)) {
						Iterator<Cell> cv = r.cellIterator();
						while (cv.hasNext()) {
							Cell c = cv.next();
							if (c.getCellType() == CellType.STRING) {
								a.add(c.getStringCellValue());
							} else {
								a.add(NumberToTextConverter.toText(c.getNumericCellValue()));

							}
						}
					}

				}
			}
		}
		return a;
	}
    public Object[][] getDataExcelAsDataProvider(String testCaseName) throws IOException {
        List<Object[]> dataList = new ArrayList<>();

        FileInputStream fis = new FileInputStream("C://Users//Admin//OneDrive//Desktop//Testng test Data - Copy.xlsx");
       
        XSSFWorkbook workbook = new XSSFWorkbook(fis);

        int sheetCount = workbook.getNumberOfSheets();
        String lastTestCaseName = "";

        for (int i = 0; i < sheetCount; i++) {
            if (workbook.getSheetName(i).equalsIgnoreCase("testData")) {
                XSSFSheet sheet = workbook.getSheetAt(i);
                Iterator<Row> rows = sheet.iterator();
                Row firstRow = rows.next();

                // find TestCases column index
                int tcColumn = -1;
                for (int k = 0; k < firstRow.getLastCellNum(); k++) {
                    if (firstRow.getCell(k).getStringCellValue().equalsIgnoreCase("TestCases")) {
                        tcColumn = k;
                        break;
                    }
                }

                // loop rows
                while (rows.hasNext()) {
                    Row r = rows.next();

                    // handle blank TestCases by reusing last non-empty one
                    String tcName = getCellValue(r.getCell(tcColumn));
                    if (!tcName.trim().isEmpty()) {
                        lastTestCaseName = tcName;
                    }

                    if (lastTestCaseName.equalsIgnoreCase(testCaseName)) {
                        String username = getCellValue(r.getCell(tcColumn + 1));
                        String password = getCellValue(r.getCell(tcColumn + 2));
                        String productName = getCellValue(r.getCell(tcColumn + 3));

                        dataList.add(new Object[]{username, password, productName});
                    }
                }
            }
        }

        workbook.close();
        return dataList.toArray(new Object[0][0]); // return as Object[][]
    }

    /**
     * Helper method to safely extract cell value as String
     */
    private String getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                // to avoid decimals like 123.0 for integer values
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    double val = cell.getNumericCellValue();
                    if (val == (long) val) {
                        return String.valueOf((long) val);
                    } else {
                        return String.valueOf(val);
                    }
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case BLANK:
                return "";
            default:
                return cell.toString().trim();
        }
    }

}
