package com.fujiluxury.test.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class RegisterPage extends BasePage{

    private static final String URL = "https://fujiluxury.vn/thanh-vien/dang-ky-dai-ly.html";

    // Locators
    private final By fullNameInput = By.xpath("//input[@id='rFullName']");
    private final By phoneInput = By.xpath("//div[@class='div_input']//input[@id='phone']");
    private final By emailInput = By.xpath("//input[@id='rEmail']");
    private final By passwordInput = By.xpath("//input[@id='rPassWord']");
    private final By confirmPasswordInput = By.xpath("//input[@id='rConfirm_PassWord']");
    private final By termsCheckbox = By.xpath("//input[@id='r_agree']");
    private final By captchaCheckbox = By.xpath("//div[@class='recaptcha-checkbox-border']");
    private final By registerButton = By.xpath("//button[@id='btnRegister']//span[contains(text(),'Đăng ký')]");
    private final By errorMessage = By.xpath("//div[contains(@class,'error-message')]");

    public RegisterPage(WebDriver driver) {
        super(driver);
    }

    public void navigateTo() {
        driver.get(URL);
        isPageLoaded();
    }

    public void enterFullName(String fullName) {
        WebElement input = waitForElementVisible(fullNameInput);
        input.clear();
        input.sendKeys(fullName);
    }

    public void enterPhone(String phone) {
        WebElement input = waitForElementVisible(phoneInput);
        input.clear();
        input.sendKeys(phone);
    }

    public void enterEmail(String email) {
        WebElement input = waitForElementVisible(emailInput);
        input.clear();
        input.sendKeys(email);
    }

    public void enterPassword(String password) {
        WebElement input = waitForElementVisible(passwordInput);
        input.clear();
        input.sendKeys(password);
    }

    public void enterConfirmPassword(String password) {
        WebElement input = waitForElementVisible(confirmPasswordInput);
        input.clear();
        input.sendKeys(password);
    }

    public void checkTermsConditions() {
        WebElement checkbox = waitForElementClickable(termsCheckbox);
        if (!checkbox.isSelected()) {
            checkbox.click();
        }
    }

    public void clickCaptcha() {
        try {
            waitForElementClickable(captchaCheckbox).click();
        } catch (Exception e) {
            // Sometimes captcha is not interactive in test environment
            System.out.println("Captcha interaction failed: " + e.getMessage());
        }
    }

    public void clickRegisterButton() {
        waitForElementClickable(registerButton).click();
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

    // Register with provided information
    public boolean register(String fullName, String phone, String email, String password) {
        enterFullName(fullName);
        enterPhone(phone);
        enterEmail(email);
        enterPassword(password);
        enterConfirmPassword(password);
        checkTermsConditions();

        try {
            clickCaptcha();
        } catch (Exception e) {
            // Continue if captcha fails
        }

        clickRegisterButton();

        // Wait for page to load after registration attempt
        isPageLoaded();

        // Return false if error message is displayed (registration failed)
        return !isErrorMessageDisplayed();
    }

    public void handleCaptcha() {
        try {
            WebElement captchaFrame = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//iframe[@title='reCAPTCHA']")
            ));

            driver.switchTo().frame(captchaFrame);

            WebElement captchaCheckbox = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//span[@role='checkbox']")
            ));

            captchaCheckbox.click();

            driver.switchTo().defaultContent();
        } catch (Exception e) {
            System.out.println("Captcha handling failed: " + e.getMessage());
        }
    }

    public void bypassCaptchaWithJS() {
        try {
            // Tìm checkbox reCAPTCHA
            WebElement captchaCheckbox = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//span[@role='checkbox']")
            ));

            // Sử dụng JavaScript để click checkbox
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].click();", captchaCheckbox);
        } catch (Exception e) {
            System.out.println("Bỏ qua Captcha bằng JS thất bại: " + e.getMessage());
        }
    }

    public boolean handleCaptchaWithRetry(int maxAttempts) {
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                WebElement captchaCheckbox = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//span[@role='checkbox']")
                ));

                captchaCheckbox.click();

                Thread.sleep(1000);

                return true;
            } catch (Exception e) {
                System.out.println("Lần thử " + attempt + " xử lý Captcha thất bại: " + e.getMessage());

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        return false;
    }

}
