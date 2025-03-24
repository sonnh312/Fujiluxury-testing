package com.fujiluxury.test.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;


import java.util.List;

public class HomePage extends BasePage{

    private static final String URL = "https://fujiluxury.vn/";
    //Logo
    private final By logo = By.xpath("//div[contains(@class,'tplogohead')]//img[@alt='Fuji Luxury Group']");
    //Main Menu
    private final By mainMenuItems = By.xpath("//div[@class='grbot']");
    //Banner
    private final By bannerSlides = By.xpath("//div[@class='item slick-slide slick-current slick-active']//div[@class='bg']");
    // Search
    private final By searchBox = By.xpath(".header-search-box");
    private final By searchInput = By.xpath("//form[@id='formSearch']//input[@placeholder='Nhập từ khóa']");
    private final By searchButton = By.xpath("//form[@id='formSearch']//img[@alt='isearch']");
    private final By searchSuggestionsList = By.xpath("//div[@class='hhlists']//div[@class='hhgrip']");
    private final By searchSuggestionItems = By.xpath("//body/div[@id='vnt-wrapper']/div[@id='vnt-container']/div[@id='vnt-content']/div[contains(@class,'boxhome')]/div[contains(@class,'producthome vbggray')]/div[@class='wrapping']/div[@class='hpproducthh']/div[@class='hhconts']/div[@class='tpproducthh']/div[@class='hhlists']/div[@class='hhgrip']/div[1]");

    // Product categories
    private final By productCategories = By.xpath("//body[1]/div[1]/div[1]/div[2]/div[4]/div[1]/div[1]/div[1]");
    //Login
    private final By loginButton = By.xpath("//a[@href='/thanh-vien/dang-nhap-dai-ly.html']");
    //Register
    private final By registerButton = By.xpath("//div[@class='acol hidden-xs hidden-sm']//a[contains(text(),'Đăng ký')]");
    //


    public HomePage(WebDriver driver) {
        super(driver);
    }

    public void navigateTo() {
        driver.get(URL);
        isPageLoaded();
    }

    //check Logo display
    public boolean isLogoDisplayed() {
        return isElementDisplayed(logo);
    }

    //check Main menu display
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

    //Main menu item count
    public int getMainMenuItemCount() {
        return getElements(mainMenuItems).size();
    }

    //Get item text
    public String[] getMainMenuTexts() {
        List<WebElement> menuItems = getElements(mainMenuItems);
        String[] menuTexts = new String[menuItems.size()];

        for (int i = 0; i < menuItems.size(); i++) {
            menuTexts[i] = menuItems.get(i).getText().trim();
        }

        return menuTexts;
    }

    //check banner display
    public boolean areBannersDisplayed() {
        List<WebElement> banners = getElements(bannerSlides);
        if (banners.isEmpty()) {
            return false;
        }

        // At least one banner should be visible
        return banners.stream().anyMatch(WebElement::isDisplayed);
    }

    //check search box display
    public boolean isSearchBoxDisplayed() {
        return isElementDisplayed(searchBox);
    }

    //click for search product
    public SearchResultsPage searchForProduct(String productName) {
        WebElement input = waitForElementVisible(searchInput);
        input.clear();
        input.sendKeys(productName);
        waitForElementClickable(searchButton).click();
        return new SearchResultsPage(driver);
    }

    // check product display
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

    // get product categories
    public int getProductCategoriesCount() {
        return getElements(productCategories).size();
    }

    //get menu categories name
    public String[] getMenuCategoryNames() {
        List<WebElement> categories = getElements(productCategories);
        String[] categoryNames = new String[categories.size()];

        for (int i = 0; i < categories.size(); i++) {
            categoryNames[i] = categories.get(i).getText().trim();
        }

        return categoryNames;
    }

    // Define this field at the class level with your other By locators
    private final By hotlineNumber = By.xpath("//a[contains(text(),'19008128')]");

    // Check if hotline number is displayed
    public boolean isHotlineDisplayed() {
        return isElementDisplayed(hotlineNumber);
    }

    // Get hotline text
    public String getHotlineText() {
        WebElement hotlineElement = waitForElementVisible(hotlineNumber);
        return hotlineElement.getText().trim();
    }


    // check login button display
    public boolean isLoginButtonDisplayed() {
        return isElementDisplayed(loginButton);
    }

    //get login button text
    public String getLoginButtonText() {
        WebElement loginButtonElement = waitForElementVisible(loginButton);
        return loginButtonElement.getText().trim();
    }

    // check login button display
    public boolean isRegisterButtonDisplayed() {
        return isElementDisplayed(registerButton);
    }

    //get login button text
    public String getRegisterButtonText() {
        WebElement loginButtonElement = waitForElementVisible(registerButton);
        return loginButtonElement.getText().trim();
    }
    //Performance
    public boolean isPageLoaded() {
        return super.isPageLoaded();
    }

}
