package com.fujiluxury.test.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class LoginPage extends BasePage{

    private static final String URL = "https://fujiluxury.vn/thanh-vien/dang-nhap-dai-ly.html";

    // Login form elements
    private final By phoneOrEmailInput = By.xpath("//input[@name='rEmail']");
    private final By passwordInput = By.xpath("//input[@name='rPassWord']");
    private final By rememberMeCheckbox = By.xpath("//input[@id='ch_remember']");
    private final By loginButton = By.xpath("//button[@id='btnLogin']//span[contains(text(),'Đăng nhập')]");
    private final By forgotPasswordLink = By.xpath("//a[contains(text(),'Bạn quên mật khẩu?')]");
    private final By errorMessage = By.xpath("//div[contains(@class,'alert-danger')]");
    private final By captchaCheckbox = By.xpath("//div[@class='recaptcha-checkbox-border']");

    // Account information section locators (for verification after login)
    private final By accountInfoHeader = By.xpath("//div[@class='vhmnmemberpc hidden-sm hidden-xs']//li[@class='current']//a");
    private final By logoutButton = By.xpath("//div[@class='vhmnmemberpc hidden-sm hidden-xs']//ul//li//a[@href='javascript:;']//span[@class='text'][contains(text(),'Đăng xuất')]");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void navigateTo() {
        driver.get(URL);
        isPageLoaded();
    }

    public void enterPhoneOrEmail(String phoneOrEmail) {
        WebElement input = waitForElementVisible(phoneOrEmailInput);
        input.clear();
        input.sendKeys(phoneOrEmail);
    }

    public void enterPassword(String password) {
        WebElement input = waitForElementVisible(passwordInput);
        input.clear();
        input.sendKeys(password);
    }

    public void clickLoginButton() {
        waitForElementClickable(loginButton).click();
    }

    public void checkRememberMe() {
        WebElement checkbox = waitForElementClickable(rememberMeCheckbox);
        if (!checkbox.isSelected()) {
            checkbox.click();
        }
    }

    public void clickForgotPassword() {
        waitForElementClickable(forgotPasswordLink).click();
    }

    public void clickCaptcha() {
        try {
            waitForElementClickable(captchaCheckbox).click();
        } catch (Exception e) {
            // Sometimes captcha is not interactive in test environment
            System.out.println("Captcha interaction failed: " + e.getMessage());
        }
    }

    public boolean isErrorMessageDisplayed() {
        try {
            return waitForElementVisible(errorMessage).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getErrorMessageText() {
        try {
            return waitForElementVisible(errorMessage).getText();
        } catch (Exception e) {
            return "";
        }
    }

    public boolean isLoginSuccessful() {
        try {
            return waitForElementVisible(accountInfoHeader).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void logout() {
        if (isElementDisplayed(logoutButton)) {
            waitForElementClickable(logoutButton).click();
        }
    }

    // Login with provided credentials
    public boolean login(String phoneOrEmail, String password, boolean rememberMe) {
        enterPhoneOrEmail(phoneOrEmail);
        enterPassword(password);

        if (rememberMe) {
            checkRememberMe();
        }

        try {
            clickCaptcha();
        } catch (Exception e) {
            // Continue if captcha fails
        }

        clickLoginButton();

        // Wait for page to load after login attempt
        isPageLoaded();

        // Return true if login was successful
        return isLoginSuccessful();
    }
}
