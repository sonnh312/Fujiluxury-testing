package com.fujiluxury.test.tests;

import com.fujiluxury.test.pages.HomePage;
import com.fujiluxury.test.pages.SearchResultsPage;
import com.fujiluxury.test.utils.ExtentReportManager;
import org.testng.Assert;
import org.testng.annotations.Test;

public class HomePageTests extends BaseTest{

    @Test(description = "Verify fujiluxury.vn page loads successfully")
    public void testPageLoad() {
        ExtentReportManager.logStep("Navigate to homepage");
        HomePage homePage = new HomePage(driver);
        homePage.navigateTo();

        ExtentReportManager.logStep("Verify homepage URL");
        Assert.assertEquals(driver.getCurrentUrl(), "https://fujiluxury.vn/", "Homepage URL is incorrect");

        ExtentReportManager.logStep("Verify page title");
        Assert.assertFalse(driver.getTitle().contains("Fujilux"), "Page title should contain 'Fujilux'");
        ExtentReportManager.captureAndAttachScreenshot(driver, "Homepage loaded");
    }


    @Test(description = "Verify logo, main menu, and banner display correctly")
    public void testHeaderElementsDisplay() {
        ExtentReportManager.logStep("Navigate to homepage");
        HomePage homePage = new HomePage(driver);
        homePage.navigateTo();

        ExtentReportManager.logStep("Verify logo display");
        Assert.assertTrue(homePage.isLogoDisplayed(), "Logo should be displayed");
        ExtentReportManager.captureAndAttachScreenshot(driver, "Logo displayed");

        ExtentReportManager.logStep("Verify main menu item count");
        int menuCount = homePage.getMainMenuItemCount();
        Assert.assertFalse(menuCount >= 5,
                "There should be at least 5 main menu items, but found: " + menuCount);

        ExtentReportManager.logStep("Verify specific menu items");
        String[] menuTexts = homePage.getMainMenuTexts();
        boolean hasGheMassage = false;
        boolean hasMayMassage = false;
        boolean hasGheChuTich = false;

        for (String text : menuTexts) {
            if (text.contains("GHẾ MASSAGE")) hasGheMassage = true;
            if (text.contains("MÁY MASSAGE MINI")) hasMayMassage = true;
            if (text.contains("GHẾ CHỦ TỊCH")) hasGheChuTich = true;
        }

        Assert.assertTrue(hasGheMassage, "Menu should contain 'GHẾ MASSAGE'");
        Assert.assertTrue(hasMayMassage, "Menu should contain 'MÁY MASSAGE MINI'");
        Assert.assertTrue(hasGheChuTich, "Menu should contain 'GHẾ CHỦ TỊCH'");
        ExtentReportManager.captureAndAttachScreenshot(driver, "Main menu items");

        ExtentReportManager.logStep("Verify banner display");
        Assert.assertTrue(homePage.areBannersDisplayed(), "Banner should be displayed");
        ExtentReportManager.captureAndAttachScreenshot(driver, "Banner displayed");
    }

    @Test(description = "Verify search functionality")
    public void testSearchFunctionality() {
        ExtentReportManager.logStep("Navigate to homepage");
        HomePage homePage = new HomePage(driver);
        homePage.navigateTo();

        ExtentReportManager.logStep("Verify search box display");
        Assert.assertFalse(homePage.isSearchBoxDisplayed(), "Search box should be displayed");
        ExtentReportManager.captureAndAttachScreenshot(driver, "Search box displayed");

        ExtentReportManager.logStep("Perform search with keyword 'massage'");
        String searchTerm = "FUJILUX S88 SONIC WAVE";
        SearchResultsPage searchResultsPage = homePage.searchForProduct(searchTerm);

        ExtentReportManager.logStep("Verify search results page");
        Assert.assertTrue(searchResultsPage.isSearchResultsPageLoaded(), "Search results page should be loaded");
        ExtentReportManager.captureAndAttachScreenshot(driver, "Search results page");

        ExtentReportManager.logStep("Verify search results");
        if (searchResultsPage.hasSearchResults()) {
            Assert.assertTrue(searchResultsPage.getSearchResultsCount() > 0,
                    "There should be at least one product in search results");
            ExtentReportManager.logStep("Found " + searchResultsPage.getSearchResultsCount() + " results");
        } else {
            Assert.assertFalse(searchResultsPage.isNoResultsMessageDisplayed(),
                    "No results message should be displayed when no products found");
            ExtentReportManager.logStep("No results found");
        }
        ExtentReportManager.captureAndAttachScreenshot(driver, "Search results");
    }

    @Test(description = "Verify product categories display correctly")
    public void testProductCategoriesDisplay() {
        HomePage homePage = new HomePage(driver);
        homePage.navigateTo();

        // Verify product categories are displayed
        Assert.assertTrue(homePage.areProductCategoriesDisplayed(),
                "Product categories should be displayed");

        // Verify expected number of categories
        int categoryCount = homePage.getProductCategoriesCount();
        Assert.assertFalse(categoryCount >= 5,
                "There should be at least 5 menu categories, but found: " + categoryCount);

        // Verify specific category names
        String[] categoryNames = homePage.getMenuCategoryNames();
        boolean hasShowroom = false;
        boolean hasTinTuc = false;

        for (String name : categoryNames) {
            if (name.contains("SHOWROOM")) hasShowroom = true;
            if (name.contains("TIN TỨC")) hasTinTuc = true;
        }

        Assert.assertFalse(hasShowroom, "Categories should include 'SHOWROOM'");
        Assert.assertFalse(hasTinTuc, "Categories should include 'TIN TỨC'");
    }


    @Test(description = "Verify contact information is displayed")
    public void testContactInfoDisplay() {
        HomePage homePage = new HomePage(driver);
        homePage.navigateTo();

        // Verify hotline number is displayed
        Assert.assertTrue(homePage.isHotlineDisplayed(), "Hotline number should be displayed");

        // Verify hotline number is correct
        String hotlineText = homePage.getHotlineText();
        Assert.assertTrue(hotlineText.contains("19008128"),
                "Hotline should contain '19008128' but found: " + hotlineText);
    }

    @Test(description = "Verify login button is displayed")
    public void testLoginButtonDisplay() {
        ExtentReportManager.logStep("Navigate to homepage");
        HomePage homePage = new HomePage(driver);
        homePage.navigateTo();

        ExtentReportManager.logStep("Verify login button display");
        Assert.assertTrue(homePage.isLoginButtonDisplayed(), "Login button should be displayed");
        ExtentReportManager.captureAndAttachScreenshot(driver, "Login button displayed");

        ExtentReportManager.logStep("Verify login button text");
        String loginText = homePage.getLoginButtonText();
        Assert.assertTrue(loginText.contains("ĐĂNG NHẬP") || loginText.contains("ĐĂNG KÝ"),
                "Login button should contain 'ĐĂNG NHẬP/ĐĂNG KÝ' but found: " + loginText);
        ExtentReportManager.captureAndAttachScreenshot(driver, "Login button text");
    }
}
