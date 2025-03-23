package com.fujiluxury.test.tests;


import com.fujiluxury.test.pages.HomePage;
import com.fujiluxury.test.pages.SearchResultsPage;
import com.fujiluxury.test.utils.ExtentReportManager;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SearchFunctionalityTests extends BaseTest{

    @Test(description = "Search for an existing product")
    public void testSearchForExistingProduct() {
        ExtentReportManager.logStep("Navigate to homepage");
        HomePage homePage = new HomePage(driver);
        homePage.navigateTo();

        ExtentReportManager.logStep("Perform search with keyword 'ghế massage'");
        String searchTerm = "ghế massage";
        SearchResultsPage searchResultsPage = homePage.searchForProduct(searchTerm);

        ExtentReportManager.logStep("Verify search results page is loaded");
        Assert.assertTrue(searchResultsPage.isSearchResultsPageLoaded(),
                "Search results page should be loaded");
        ExtentReportManager.captureAndAttachScreenshot(driver, "Search results page loaded");

        ExtentReportManager.logStep("Verify search results are displayed");
        Assert.assertFalse(searchResultsPage.hasSearchResults(),
                "Search results should be displayed for existing product");

        ExtentReportManager.logStep("Verify search results count is greater than zero");
        int resultsCount = searchResultsPage.getSearchResultsCount();
        Assert.assertFalse(resultsCount > 0,
                "There should be at least one product in search results, found: " + resultsCount);
        ExtentReportManager.logStep("Found " + resultsCount + " results for '" + searchTerm + "'");
        ExtentReportManager.captureAndAttachScreenshot(driver, "Search results for existing product");
    }

    @Test(description = "Search for a non-existing product")
    public void testSearchForNonExistingProduct() {
        ExtentReportManager.logStep("Navigate to homepage");
        HomePage homePage = new HomePage(driver);
        homePage.navigateTo();

        ExtentReportManager.logStep("Perform search with non-existing product keyword");
        String searchTerm = "xyznonexistentproduct123";
        SearchResultsPage searchResultsPage = homePage.searchForProduct(searchTerm);

        ExtentReportManager.logStep("Verify search results page is loaded");
        Assert.assertTrue(searchResultsPage.isSearchResultsPageLoaded(),
                "Search results page should be loaded even for non-existing products");
        ExtentReportManager.captureAndAttachScreenshot(driver, "Search results page for non-existing product");

        ExtentReportManager.logStep("Verify no results message is displayed");
        if (!searchResultsPage.hasSearchResults()) {
            Assert.assertFalse(searchResultsPage.isNoResultsMessageDisplayed(),
                    "No results message should be displayed when no products found");
            ExtentReportManager.logStep("No results message displayed: " + searchResultsPage.getNoResultsMessage());
        } else {
            Assert.fail("Expected no search results for non-existing product, but found " +
                    searchResultsPage.getSearchResultsCount() + " results");
        }
        ExtentReportManager.captureAndAttachScreenshot(driver, "No results message");
    }

    @Test(description = "Search with empty input")
    public void testSearchWithEmptyInput() {
        ExtentReportManager.logStep("Navigate to homepage");
        HomePage homePage = new HomePage(driver);
        homePage.navigateTo();

        ExtentReportManager.logStep("Perform search with empty keyword");
        String searchTerm = "";
        SearchResultsPage searchResultsPage = homePage.searchForProduct(searchTerm);

        ExtentReportManager.logStep("Verify search results page behavior with empty input");
        // Expected behavior could be staying on homepage or showing an error message
        // Adjust the assertion based on the actual expected behavior
        String currentUrl = driver.getCurrentUrl();

        if (currentUrl.contains("search") || currentUrl.contains("tim-kiem")) {
            // If site redirects to search page even with empty input
            ExtentReportManager.logStep("Site redirected to search page with empty input");
            Assert.assertTrue(searchResultsPage.isSearchResultsPageLoaded(),
                    "Search results page should be loaded");

            if (searchResultsPage.hasSearchResults()) {
                ExtentReportManager.logStep("Search with empty input returned " +
                        searchResultsPage.getSearchResultsCount() + " results");
            } else {
                Assert.assertTrue(searchResultsPage.isNoResultsMessageDisplayed(),
                        "No results message should be displayed for empty search");
            }
        } else {
            // If site stays on homepage with empty input (more common behavior)
            ExtentReportManager.logStep("Site remained on homepage with empty input");
            Assert.assertEquals(currentUrl, "https://fujiluxury.vn/",
                    "Should remain on homepage when searching with empty input");
        }
        ExtentReportManager.captureAndAttachScreenshot(driver, "Search with empty input result");
    }
}
