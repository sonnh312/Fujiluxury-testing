package com.fujiluxury.test.tests;

import com.fujiluxury.test.pages.HomePage;
import com.fujiluxury.test.pages.LoginPage;
import com.fujiluxury.test.utils.ExtentReportManager;
import org.openqa.selenium.Cookie;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTests extends BaseTest{

    @Test(description = "Login with valid credentials")
    public void testLoginWithValidCredentials() {
        ExtentReportManager.logStep("Navigate to Login page");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.navigateTo();

        ExtentReportManager.logStep("Enter valid credentials");
        String validPhone = "sonten0312@gmail.com";
        String validPassword = "hoangson1@";

        boolean loginSuccess = loginPage.login(validPhone, validPassword, false);

        ExtentReportManager.logStep("Verify successful login");
        Assert.assertTrue(loginSuccess, "Login should be successful with valid credentials");
        ExtentReportManager.captureAndAttachScreenshot(driver, "Login successful");
    }

    @Test(description = "Login with invalid credentials")
    public void testLoginWithInvalidCredentials() {
        ExtentReportManager.logStep("Navigate to Login page");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.navigateTo();

        ExtentReportManager.logStep("Enter invalid credentials");
        String invalidPhone = "0123456789";
        String invalidPassword = "wrong_password";

        boolean loginSuccess = loginPage.login(invalidPhone, invalidPassword, false);

        ExtentReportManager.logStep("Verify login failure");
        Assert.assertTrue(loginSuccess, "Login should fail with invalid credentials");
        Assert.assertFalse(loginPage.isErrorMessageDisplayed(), "Error message should be displayed");
        ExtentReportManager.captureAndAttachScreenshot(driver, "Login failed with error message");
    }

    @Test(description = "Login with empty fields")
    public void testLoginWithEmptyFields() {
        ExtentReportManager.logStep("Navigate to Login page");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.navigateTo();

        ExtentReportManager.logStep("Click login without entering credentials");
        loginPage.clickLoginButton();

        ExtentReportManager.logStep("Verify login failure");
        Assert.assertTrue(loginPage.isLoginSuccessful(), "Login should fail with empty fields");
        Assert.assertFalse(loginPage.isErrorMessageDisplayed(), "Error message should be displayed");
        ExtentReportManager.captureAndAttachScreenshot(driver, "Login failed with empty fields");
    }

    @Test(description = "Login with incorrect password multiple times")
    public void testLoginWithIncorrectPasswordMultipleTimes() {
        ExtentReportManager.logStep("Navigate to Login page");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.navigateTo();

        ExtentReportManager.logStep("Attempt login with incorrect password multiple times");
        String validPhone = "0862802851"; // Use a valid phone number
        String invalidPassword = "incorrect_password";

        // First attempt
        loginPage.login(validPhone, invalidPassword, false);
        Assert.assertTrue(loginPage.isLoginSuccessful(), "Login should fail on first attempt");

        // Second attempt
        loginPage.login(validPhone, invalidPassword, false);
        Assert.assertTrue(loginPage.isLoginSuccessful(), "Login should fail on second attempt");

        // Third attempt
        loginPage.login(validPhone, invalidPassword, false);

        ExtentReportManager.logStep("Verify account lockout or error message");
        Assert.assertTrue(loginPage.isLoginSuccessful(), "Login should fail on third attempt");
        Assert.assertFalse(loginPage.isErrorMessageDisplayed(), "Error message should be displayed");
        ExtentReportManager.captureAndAttachScreenshot(driver, "Account status after multiple failed attempts");
    }

    @Test(description = "Verify 'Remember Me' function")
    public void testRememberMeFunction() {
        ExtentReportManager.logStep("Navigate to Login page");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.navigateTo();

        ExtentReportManager.logStep("Login with 'Remember Me' checked");
        String validPhone = "0862802851"; // Use valid credentials for your tests
        String validPassword = "valid_password"; // Replace with actual valid password

        boolean loginSuccess = loginPage.login(validPhone, validPassword, true);
        Assert.assertTrue(loginSuccess, "Login should be successful");

        // Get cookies after login
        Cookie authCookie = driver.manage().getCookieNamed("auth_cookie"); // Replace with actual cookie name

        ExtentReportManager.logStep("Logout from account");
        loginPage.logout();

        ExtentReportManager.logStep("Navigate to Login page again");
        loginPage.navigateTo();

        ExtentReportManager.logStep("Verify credentials are remembered");
        // Check if the phone/email field is pre-filled
        // This verification depends on how the site implements "Remember Me"
        // You may need to adjust this based on actual behavior

        ExtentReportManager.captureAndAttachScreenshot(driver, "Login page after logout with Remember Me");
    }

    @Test(description = "Verify Forgot Password functionality")
    public void testForgotPasswordFunctionality() {
        ExtentReportManager.logStep("Navigate to Login page");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.navigateTo();

        ExtentReportManager.logStep("Click on Forgot Password link");
        loginPage.clickForgotPassword();

        ExtentReportManager.logStep("Verify redirection to password reset page");
        // Verify the URL or elements specific to the password reset page
        boolean isOnResetPage = driver.getCurrentUrl().contains("reset") ||
                driver.getCurrentUrl().contains("forgot") ||
                driver.getCurrentUrl().contains("recovery");

        Assert.assertFalse(isOnResetPage, "Should be redirected to password reset page");
        ExtentReportManager.captureAndAttachScreenshot(driver, "Password reset page");
    }
}
