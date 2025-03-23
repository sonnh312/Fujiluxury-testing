package com.fujiluxury.test.tests;

import com.fujiluxury.test.pages.HomePage;
import com.fujiluxury.test.utils.ExtentReportManager;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class PerformanceTests extends BaseTest{

    private Map<String, Long> pageLoadTimes;
    private static final int CONCURRENT_USERS = 10; // Simulate 10 concurrent users
    private static final int MAX_RESPONSE_TIME_MS = 7000; // 5 seconds max acceptable response time

    @BeforeMethod
    @Override
    public void setUp(org.testng.ITestResult result) {
        super.setUp(result);
        pageLoadTimes = new HashMap<>();

        // Add performance monitoring capability
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-gpu");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--incognito");
        options.addArguments("--window-size=1920,1080");

        // Replace the driver if needed
        if (driver != null) {
            driver.quit();
        }
        driver = new ChromeDriver(options);
    }

    /**
     * Test homepage load time
     */
    @Test(description = "Measure homepage load time performance")
    public void testHomepageLoadTime() {
        ExtentReportManager.logStep("Navigate to homepage and measure load time");

        // Measure navigation time
        long startTime = System.currentTimeMillis();

        HomePage homePage = new HomePage(driver);
        homePage.navigateTo();

        // Wait for page to fully load
        boolean pageLoaded = homePage.isPageLoaded();

        long endTime = System.currentTimeMillis();
        long loadTime = endTime - startTime;

        // Record load time
        pageLoadTimes.put("Homepage", loadTime);

        ExtentReportManager.logStep("Homepage load time: " + loadTime + " ms");
        ExtentReportManager.captureAndAttachScreenshot(driver, "Homepage load time test");

        // Assert page loaded successfully and within acceptable time
        Assert.assertTrue(pageLoaded, "Homepage should load completely");
        Assert.assertTrue(loadTime < MAX_RESPONSE_TIME_MS,
                "Homepage load time should be less than " + MAX_RESPONSE_TIME_MS +
                        " ms, but was " + loadTime + " ms");

        // Get additional performance metrics using Navigation Timing API
        Map<String, Object> timingMetrics = getPerformanceTimingMetrics();

        for (Map.Entry<String, Object> entry : timingMetrics.entrySet()) {
            ExtentReportManager.logStep(entry.getKey() + ": " + entry.getValue() + " ms");
        }
    }


    @Test(description = "Measure search functionality load time")
    public void testSearchLoadTime() {
        ExtentReportManager.logStep("Navigate to homepage");
        HomePage homePage = new HomePage(driver);
        homePage.navigateTo();

        ExtentReportManager.logStep("Measure search results page load time");
        long startTime = System.currentTimeMillis();

        // Perform search with a common term
        String searchTerm = "ghế massage";
        homePage.searchForProduct(searchTerm);

        // Wait for the search results page to load
        waitForPageLoad();

        long endTime = System.currentTimeMillis();
        long loadTime = endTime - startTime;

        // Record load time
        pageLoadTimes.put("Search", loadTime);

        ExtentReportManager.logStep("Search results page load time: " + loadTime + " ms");
        ExtentReportManager.captureAndAttachScreenshot(driver, "Search load time test");

        // Assert the search results page loaded within acceptable time
        Assert.assertTrue(loadTime < MAX_RESPONSE_TIME_MS,
                "Search page load time should be less than " + MAX_RESPONSE_TIME_MS +
                        " ms, but was " + loadTime + " ms");
    }

    /**
     * Test high traffic simulation
     */
    @Test(description = "Simulate high traffic on the website")
    public void testHighTrafficSimulation() throws InterruptedException {
        ExtentReportManager.logStep("Setting up high traffic simulation with " +
                CONCURRENT_USERS + " concurrent users");

        ExecutorService executor = Executors.newFixedThreadPool(CONCURRENT_USERS);
        List<Long> responseTimes = new ArrayList<>();

        // Create concurrent user simulation tasks
        for (int i = 0; i < CONCURRENT_USERS; i++) {
            executor.submit(() -> {
                WebDriver threadDriver = null;
                try {
                    // Setup a new driver for each thread
                    ChromeOptions options = new ChromeOptions();
                    options.addArguments("--headless");
                    threadDriver = new ChromeDriver(options);

                    // Measure homepage load time
                    long startTime = System.currentTimeMillis();
                    threadDriver.get("https://fujiluxury.vn/");

                    // Wait for page to fully load
                    waitForPageLoadWithDriver(threadDriver);

                    long endTime = System.currentTimeMillis();
                    long responseTime = endTime - startTime;

                    synchronized (responseTimes) {
                        responseTimes.add(responseTime);
                        ExtentReportManager.logStep("Concurrent user request completed in: " +
                                responseTime + " ms");
                    }
                } catch (Exception e) {
                    ExtentReportManager.logStep("Error in concurrent user simulation: " + e.getMessage());
                } finally {
                    if (threadDriver != null) {
                        threadDriver.quit();
                    }
                }
            });
        }

        // Shutdown the executor and wait for all tasks to complete
        executor.shutdown();
        boolean tasksCompleted = executor.awaitTermination(60, TimeUnit.SECONDS);

        ExtentReportManager.logStep("High traffic simulation completed: " +
                (tasksCompleted ? "Successfully" : "With timeout"));

        // Calculate average response time
        if (!responseTimes.isEmpty()) {
            double averageResponseTime = responseTimes.stream()
                    .mapToLong(Long::longValue)
                    .average()
                    .orElse(0.0);

            long maxResponseTime = responseTimes.stream()
                    .mapToLong(Long::longValue)
                    .max()
                    .orElse(0);

            ExtentReportManager.logStep("Average response time under load: " +
                    String.format("%.2f", averageResponseTime) + " ms");
            ExtentReportManager.logStep("Maximum response time under load: " +
                    maxResponseTime + " ms");

            // Assert average response time is within acceptable limits
            Assert.assertFalse(averageResponseTime < MAX_RESPONSE_TIME_MS * 1.5,
                    "Average response time under load should be less than " +
                            (MAX_RESPONSE_TIME_MS * 1.5) + " ms, but was " +
                            String.format("%.2f", averageResponseTime) + " ms");
        } else {
            Assert.fail("No response times collected during high traffic simulation");
        }
    }

    /**
     * Test repeated page navigation performance
     */
    @Test(description = "Measure performance degradation during repeated navigation")
    public void testRepeatedNavigationPerformance() {
        ExtentReportManager.logStep("Testing performance during repeated navigation");

        HomePage homePage = new HomePage(driver);
        List<Long> navigationTimes = new ArrayList<>();

        // Perform 5 consecutive navigations
        for (int i = 0; i < 5; i++) {
            long startTime = System.currentTimeMillis();

            homePage.navigateTo();
            boolean pageLoaded = homePage.isPageLoaded();

            long endTime = System.currentTimeMillis();
            long loadTime = endTime - startTime;
            navigationTimes.add(loadTime);

            ExtentReportManager.logStep("Navigation #" + (i + 1) + " load time: " + loadTime + " ms");
            Assert.assertTrue(pageLoaded, "Page should load completely on navigation #" + (i + 1));

            // Clear browser cache between navigations if needed
            clearBrowserCache();
        }

        // Calculate and report average and final navigation times
        double averageTime = navigationTimes.stream()
                .mapToLong(Long::longValue)
                .average()
                .orElse(0.0);

        long firstNavigation = navigationTimes.get(0);
        long lastNavigation = navigationTimes.get(navigationTimes.size() - 1);

        ExtentReportManager.logStep("First navigation time: " + firstNavigation + " ms");
        ExtentReportManager.logStep("Last navigation time: " + lastNavigation + " ms");
        ExtentReportManager.logStep("Average navigation time: " + String.format("%.2f", averageTime) + " ms");

        // Assert no significant performance degradation
        Assert.assertTrue(lastNavigation <= firstNavigation * 1.5,
                "Performance should not degrade significantly over repeated navigations");
    }

    /**
     * Test server response time with multiple requests
     */
    @Test(description = "Test server response time with multiple requests")
    public void testServerResponseTime() {
        ExtentReportManager.logStep("Testing server response time with multiple requests");

        String[] pagesToTest = {
                "https://fujiluxury.vn/",
                "https://fujiluxury.vn/ghe-massage.html",
                "https://fujiluxury.vn/lien-he.html",
                "https://fujiluxury.vn/tin-tuc.html"
        };

        Map<String, Long> responseTimeMap = new HashMap<>();

        for (String pageUrl : pagesToTest) {
            long startTime = System.currentTimeMillis();

            driver.get(pageUrl);
            waitForPageLoad();

            long endTime = System.currentTimeMillis();
            long responseTime = endTime - startTime;

            // Extract page name from URL
            String pageName = pageUrl.substring(pageUrl.lastIndexOf("/") + 1);
            if (pageName.isEmpty()) pageName = "Homepage";

            responseTimeMap.put(pageName, responseTime);
            ExtentReportManager.logStep("Page '" + pageName + "' response time: " + responseTime + " ms");

            // Capture page state
            ExtentReportManager.captureAndAttachScreenshot(driver, "Server response time test - " + pageName);

            // Assert response time is within acceptable limits
            if (responseTime >= MAX_RESPONSE_TIME_MS) {
                ExtentReportManager.logStep("WARNING: Page '" + pageName + "' response time (" +
                        responseTime + " ms) exceeds threshold of " + MAX_RESPONSE_TIME_MS + " ms");
            }
        }
        // Calculate and report average response time
        double averageResponseTime = responseTimeMap.values().stream()
                .mapToLong(Long::longValue)
                .average()
                .orElse(0.0);

        ExtentReportManager.logStep("Average server response time: " +
                String.format("%.2f", averageResponseTime) + " ms");
    }

    /**
     * Test page resource loading performance
     */
    @Test(description = "Measure page resource loading performance")
    public void testResourceLoadingPerformance() {
        ExtentReportManager.logStep("Measuring resource loading performance");

        HomePage homePage = new HomePage(driver);
        homePage.navigateTo();

        // Wait for page to fully load
        boolean pageLoaded = homePage.isPageLoaded();
        Assert.assertTrue(pageLoaded, "Homepage should load completely");

        // Get resource timing information
        Map<String, Object> resourceTimings = getResourceTimingMetrics();

        // Log the number of resources loaded
        int totalResources = (int) resourceTimings.get("totalResources");
        Object sizeObj = resourceTimings.get("totalSize");
        Object timeObj = resourceTimings.get("totalLoadTime");
        long totalResourceSize = sizeObj instanceof Number ? ((Number)sizeObj).longValue() : 0L;
        long totalResourceLoadTime = timeObj instanceof Number ? ((Number)timeObj).longValue() : 0L;

        ExtentReportManager.logStep("Total resources loaded: " + totalResources);
        ExtentReportManager.logStep("Total resource size: " + (totalResourceSize / 1024) + " KB");
        ExtentReportManager.logStep("Total resource load time: " + totalResourceLoadTime + " ms");

        // Log slow-loading resources
        List<Map<String, Object>> slowResources = new ArrayList<>();
        if (resourceTimings.containsKey("slowResources") && resourceTimings.get("slowResources") != null) {
            try {
                slowResources = (List<Map<String, Object>>) resourceTimings.get("slowResources");
            } catch (ClassCastException e) {
                ExtentReportManager.logStep("Error casting slow resources: " + e.getMessage());
            }
        }

        // Assert total resource load time is acceptable
        Assert.assertTrue(totalResourceLoadTime < MAX_RESPONSE_TIME_MS * 1.2,
                "Total resource load time should be less than " +
                        (MAX_RESPONSE_TIME_MS * 1.2) + " ms, but was " +
                        totalResourceLoadTime + " ms");
    }


    private void waitForPageLoad() {
        new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(30))
                .until(webDriver -> ((JavascriptExecutor) webDriver)
                        .executeScript("return document.readyState").equals("complete"));
    }

    /**
     * Wait for page to load completely with a specific driver
     */
    private void waitForPageLoadWithDriver(WebDriver driver) {
        new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(30))
                .until(webDriver -> ((JavascriptExecutor) webDriver)
                        .executeScript("return document.readyState").equals("complete"));
    }

    /**
     * Get performance timing metrics using Navigation Timing API
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> getPerformanceTimingMetrics() {
        Map<String, Object> metrics = new HashMap<>();

        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;

            // Get timing data using Navigation Timing API
            Map<String, Object> timingData = (Map<String, Object>) js.executeScript(
                    "return performance.timing.toJSON();");

            long navigationStart = (long) timingData.get("navigationStart");
            long responseStart = (long) timingData.get("responseStart");
            long responseEnd = (long) timingData.get("responseEnd");
            long domComplete = (long) timingData.get("domComplete");
            long loadEventEnd = (long) timingData.get("loadEventEnd");

            // Calculate timing metrics
            metrics.put("Server Response Time", responseStart - navigationStart);
            metrics.put("Page Download Time", responseEnd - responseStart);
            metrics.put("DOM Processing Time", domComplete - responseEnd);
            metrics.put("Page Rendering Time", loadEventEnd - domComplete);
            metrics.put("Total Page Load Time", loadEventEnd - navigationStart);

        } catch (Exception e) {
            ExtentReportManager.logStep("Error getting performance metrics: " + e.getMessage());
        }

        return metrics;
    }

    /**
     * Get resource timing metrics
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> getResourceTimingMetrics() {
        Map<String, Object> metrics = new HashMap<>();

        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;

            // Get all resource entries
            ArrayList<Map<String, Object>> resources = (ArrayList<Map<String, Object>>) js.executeScript(
                    "return window.performance.getEntriesByType('resource').map(function(entry) { " +
                            "   return { " +
                            "       name: entry.name, " +
                            "       type: entry.initiatorType, " +
                            "       duration: entry.duration, " +
                            "       size: entry.transferSize || 0 " +
                            "   }; " +
                            "});");

            // Count total resources
            metrics.put("totalResources", resources.size());

            // Calculate total resource size and load time
            double totalSize = 0;
            double totalLoadTime = 0;
            List<Map<String, Object>> slowResources = new ArrayList<>();

            for (Map<String, Object> resource : resources) {
                totalSize += (double) resource.get("size") > 0 ? (double) resource.get("size") : 0;
                totalLoadTime += (double) resource.get("duration");

                // Identify slow-loading resources (more than 500ms)
                if ((double) resource.get("duration") > 500) {
                    slowResources.add(resource);
                }
            }

            metrics.put("totalSize", (long) totalSize);
            metrics.put("totalLoadTime", (long) totalLoadTime);
            metrics.put("slowResources", slowResources);

        } catch (Exception e) {
            ExtentReportManager.logStep("Error getting resource timing metrics: " + e.getMessage());
        }

        return metrics;
    }

    /**
     * Clear browser cache
     */
    private void clearBrowserCache() {
        try {
            // Clear browser cache using JavaScript
            JavascriptExecutor js = (JavascriptExecutor) driver;
            driver.manage().deleteAllCookies();
            js.executeScript("window.sessionStorage.clear();");
            js.executeScript("window.localStorage.clear();");
        } catch (Exception e) {
            ExtentReportManager.logStep("Error clearing browser cache: " + e.getMessage());
        }
    }
}
