package com.fujiluxury.test.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ExtentReportManager {

    private static ExtentReports extent;
    private static Map<Integer, ExtentTest> testMap = new HashMap<>();

    /**
     * Khởi tạo ExtentReports
     */
    public static synchronized ExtentReports getInstance() {
        if (extent == null) {
            String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
            String reportPath = System.getProperty("user.dir") + "/reports/TestReport_" + timeStamp + ".html";

            // Đảm bảo thư mục tồn tại
            new File(reportPath).getParentFile().mkdirs();

            ExtentSparkReporter htmlReporter = new ExtentSparkReporter(reportPath);
            htmlReporter.config().setDocumentTitle("Fuji Luxury - Báo cáo tự động");
            htmlReporter.config().setReportName("Báo cáo kiểm thử web Fuji Luxury");
            htmlReporter.config().setTheme(Theme.DARK);
            htmlReporter.config().setEncoding("utf-8");
            htmlReporter.config().setTimeStampFormat("dd/MM/yyyy hh:mm:ss a");

            extent = new ExtentReports();
            extent.attachReporter(htmlReporter);
            extent.setSystemInfo("Website", "Fuji Luxury");
            extent.setSystemInfo("Trình duyệt", "Chrome");
            extent.setSystemInfo("Hệ điều hành", System.getProperty("os.name"));
        }

        return extent;
    }

    /**
     * Tạo test mới
     */
    public static synchronized ExtentTest createTest(String testName, String description) {
        ExtentTest test = getInstance().createTest(testName, description);
        testMap.put((int) Thread.currentThread().getId(), test);
        return test;
    }

    /**
     * Lấy test hiện tại
     */
    public static synchronized ExtentTest getTest() {
        return testMap.get((int) Thread.currentThread().getId());
    }

    /**
     * Chụp màn hình và đính kèm vào báo cáo
     */
    public static void captureAndAttachScreenshot(WebDriver driver, String stepName) {
        String base64Screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
        getTest().info(stepName, MediaEntityBuilder.createScreenCaptureFromBase64String(base64Screenshot).build());
    }

    /**
     * Log các bước thực hiện
     */
    public static void logStep(String stepName) {
        getTest().info(stepName);
    }

    /**
     * Kết thúc và tạo báo cáo
     */
    public static void flushReport() {
        getInstance().flush();
    }

}
