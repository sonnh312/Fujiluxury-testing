package com.fujiluxury.test.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class SearchResultsPage extends HomePage{

    private final By breadcrumb = By.cssSelector(".breadcrumb");
    private final By searchResultTitle = By.cssSelector(".search-result-title");
    private final By productItems = By.cssSelector(".product-item");
    private final By noResultsMessage = By.cssSelector(".no-results-found");

    public SearchResultsPage(WebDriver driver) {
        super(driver);
    }

    public boolean isSearchResultsPageLoaded() {
        return isPageLoaded() && isElementDisplayed(breadcrumb);
    }

    public String getSearchResultTitle() {
        WebElement element = waitForElementVisible(searchResultTitle);
        return element.getText();
    }

    public boolean hasSearchResults() {
        return !getElements(productItems).isEmpty();
    }

    public int getSearchResultsCount() {
        return getElements(productItems).size();
    }

    public boolean isNoResultsMessageDisplayed() {
        return isElementDisplayed(noResultsMessage);
    }

    public String getNoResultsMessage() {
        if (isNoResultsMessageDisplayed()) {
            return driver.findElement(noResultsMessage).getText();
        }
        return "";
    }
}
