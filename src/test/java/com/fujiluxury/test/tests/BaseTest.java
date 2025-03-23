package com.fujiluxury.test.tests;

import com.fujiluxury.test.utils.ExtentReportManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.time.Duration;

@Listeners(com.fujiluxury.test.utils.ExtentReportListener.class)
public class BaseTest {

    protected WebDriver driver;

    @BeforeMethod
    public void setUp(ITestResult result) {
        // Init test in report doc
        ExtentReportManager.createTest(
                result.getMethod().getMethodName(),
                result.getMethod().getDescription()
        );

        ExtentReportManager.logStep("Init Browser Chrome");

        // Setup driver
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");

        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        // Screenshot if test failed
        if (result.getStatus() == ITestResult.FAILURE) {
            ExtentReportManager.captureAndAttachScreenshot(driver, "Test failed - Screenshot and capture");
        }

        ExtentReportManager.logStep("Close Browser");

        if (driver != null) {
            driver.quit();
        }
    }

    @AfterSuite
    public void afterSuite() {
        // Finished Report
        ExtentReportManager.flushReport();
    }
}
