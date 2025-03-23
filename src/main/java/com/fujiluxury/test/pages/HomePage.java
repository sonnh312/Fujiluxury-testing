package com.fujiluxury.test.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;


import java.util.List;

public class HomePage extends BasePage{

    private static final String URL = "https://fujiluxury.vn/";
    private final By logo = By.xpath("//div[contains(@class,'tplogohead')]//img[@alt='Fuji Luxury Group']");
    //Main Menu
    private final By mainMenuItems = By.xpath("//div[@class='grbot']");

    //Banner
    private final By bannerSlides = By.xpath("//div[@class='item slick-slide slick-current slick-active']//div[@class='bg']");

    // Search
    private final By searchBox = By.xpath(".header-search-box");
    private final By searchInput = By.xpath("//form[@id='formSearch']//input[@placeholder='Nhập từ khóa']");
    private final By searchButton = By.xpath("//form[@id='formSearch']//img[@alt='isearch']");

    // Product categories
    private final By productCategories = By.xpath("//body[1]/div[1]/div[1]/div[2]/div[4]/div[1]/div[1]/div[1]");
    //Login button
    private final By loginButton = By.xpath("//a[@href='/thanh-vien/dang-nhap-dai-ly.html']");

    public HomePage(WebDriver driver) {
        super(driver);
    }


    public void navigateTo() {
        driver.get(URL);
        isPageLoaded();
    }


    public boolean isLogoDisplayed() {
        return isElementDisplayed(logo);
    }


    public boolean areMainMenuItemsDisplayed() {
        List<WebElement> menuItems = getElements(mainMenuItems);
        if (menuItems.isEmpty()) {
            return false;
        }

        for (WebElement menuItem : menuItems) {
            if (!menuItem.isDisplayed()) {
                return false;
            }
        }
        return true;
    }


    public int getMainMenuItemCount() {
        return getElements(mainMenuItems).size();
    }


    public String[] getMainMenuTexts() {
        List<WebElement> menuItems = getElements(mainMenuItems);
        String[] menuTexts = new String[menuItems.size()];

        for (int i = 0; i < menuItems.size(); i++) {
            menuTexts[i] = menuItems.get(i).getText().trim();
        }

        return menuTexts;
    }

    public boolean areBannersDisplayed() {
        List<WebElement> banners = getElements(bannerSlides);
        if (banners.isEmpty()) {
            return false;
        }

        // At least one banner should be visible
        return banners.stream().anyMatch(WebElement::isDisplayed);
    }


    public boolean isSearchBoxDisplayed() {
        return isElementDisplayed(searchBox);
    }


    public SearchResultsPage searchForProduct(String productName) {
        WebElement input = waitForElementVisible(searchInput);
        input.clear();
        input.sendKeys(productName);
        waitForElementClickable(searchButton).click();
        return new SearchResultsPage(driver);
    }


    public boolean areProductCategoriesDisplayed() {
        List<WebElement> categories = getElements(productCategories);
        if (categories.isEmpty()) {
            return false;
        }

        for (WebElement category : categories) {
            scrollToElement(category);
            if (!category.isDisplayed()) {
                return false;
            }
        }
        return true;
    }


    public int getProductCategoriesCount() {
        return getElements(productCategories).size();
    }

    public boolean isLoginButtonDisplayed() {
        return isElementDisplayed(loginButton);
    }

    public String getLoginButtonText() {
        WebElement loginButtonElement = waitForElementVisible(loginButton);
        return loginButtonElement.getText().trim();
    }



}
