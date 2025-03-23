package com.fujiluxury.test.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExtentReportListener implements ITestListener{

    private ExtentReports extent;
    private ExtentTest test;
    private static final String OUTPUT_FOLDER = "./reports/";
    private static final String FILE_NAME = "TestReport.html";

    @Override
    public void onStart(ITestContext context) {
        // Create if not exist
        new File(OUTPUT_FOLDER).mkdirs();

        // init ExtentReports
        ExtentSparkReporter htmlReporter = new ExtentSparkReporter(OUTPUT_FOLDER + FILE_NAME);

        // Config
        htmlReporter.config().setDocumentTitle("Báo cáo kiểm thử tự động - Fuji Luxury");
        htmlReporter.config().setReportName("Báo cáo kiểm thử");
        htmlReporter.config().setTheme(Theme.STANDARD);
        htmlReporter.config().setEncoding("utf-8");

        // Create instance ExtentReports
        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);

        // Add information file
        extent.setSystemInfo("Môi trường", "Test");
        extent.setSystemInfo("Trình duyệt", "Chrome");
        extent.setSystemInfo("Hệ điều hành", System.getProperty("os.Window"));
        extent.setSystemInfo("Người thực hiện", System.getProperty("user.Son"));
    }

    @Override
    public void onTestStart(ITestResult result) {
        // Create new test when starts
        String testName = result.getMethod().getMethodName();
        String description = result.getMethod().getDescription();
        test = extent.createTest(testName, description);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        // Test log passed
        test.log(Status.PASS, "Test passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        // Test log failed
        test.log(Status.FAIL, "Test failed: " + result.getThrowable());

        // Screenshot when failed
        Object testClass = result.getInstance();
        WebDriver driver = null;

        // Get driver from test class
        try {
            driver = (WebDriver) testClass.getClass().getSuperclass().getDeclaredField("driver").get(testClass);
        } catch (Exception e) {
            test.log(Status.WARNING, "Could not take screenshot: " + e.getMessage());
        }

        // Save and take note
        if (driver != null) {
            String screenshotPath = captureScreenshot(driver, result.getName());
            if (screenshotPath != null) {
                test.addScreenCaptureFromPath(screenshotPath);
            } else {
                test.log(Status.WARNING, "Failed to capture screenshot");
            }
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        // Ghi nhận test bị bỏ qua
        test.log(Status.SKIP, "Test skipped: " + result.getThrowable());
    }

    @Override
    public void onFinish(ITestContext context) {
        // Hoàn tất báo cáo khi tất cả test chạy xong
        extent.flush();
    }


    private String captureScreenshot(WebDriver driver, String testName) {
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String fileName = testName + "_" + timestamp + ".png";
        String filePath = OUTPUT_FOLDER + "screenshots/" + fileName;

        // Tạo thư mục lưu ảnh chụp màn hình
        new File(OUTPUT_FOLDER + "screenshots/").mkdirs();

        // Thực hiện chụp màn hình
        File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

        try {
            Path destination = Paths.get(filePath);
            Files.copy(srcFile.toPath(), destination);
            return "screenshots/" + fileName;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }



}
