package com.fujiluxury.test.tests;


import com.fujiluxury.test.pages.RegisterPage;
import com.fujiluxury.test.utils.ExtentReportManager;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.UUID;
public class RegisterPageTests extends BaseTest{

    @Test(description = "Register with valid information")
    public void testRegisterWithValidInformation() {
        ExtentReportManager.logStep("Navigate to Register page");
        RegisterPage registerPage = new RegisterPage(driver);
        registerPage.navigateTo();

        // Generate unique email to avoid duplicate registration issues
        String uniqueId = UUID.randomUUID().toString().substring(0, 8);
        String fullName = "Test User " + uniqueId;
        String phone = "098" + (int)(Math.random() * 10000000);
        String email = "testuser" + uniqueId + "@example.com";
        String password = "Test@123456";

        ExtentReportManager.logStep("Fill registration form with valid information");
        boolean registerSuccess = registerPage.register(fullName, phone, email, password);

        ExtentReportManager.logStep("Verify successful registration");
        Assert.assertTrue(registerSuccess, "Registration should be successful with valid information");
        ExtentReportManager.captureAndAttachScreenshot(driver, "Registration successful");
    }

//    @Test(description = "Register with existing email")
//    public void testRegisterWithExistingEmail() {
//        ExtentReportManager.logStep("Navigate to Register page");
//        RegisterPage registerPage = new RegisterPage(driver);
//        registerPage.navigateTo();
//
//        // Use an email that is already registered
//        String fullName = "Test User";
//        String phone = "0987654321";
//        String existingEmail = "sonte0312@gmail.com"; // Replace with an email that is known to exist
//        String password = "Test@123456";
//
//        ExtentReportManager.logStep("Fill registration form with existing email");
//        boolean registerSuccess = registerPage.register(fullName, phone, existingEmail, password);
//
//        ExtentReportManager.logStep("Verify registration failure");
//        Assert.assertFalse(registerSuccess, "Registration should fail with existing email");
//        Assert.assertTrue(registerPage.isErrorMessageDisplayed(), "Error message should be displayed");
//        ExtentReportManager.captureAndAttachScreenshot(driver, "Registration failed - Existing email");
//    }
//
//    @Test(description = "Register with empty fields")
//    public void testRegisterWithEmptyFields() {
//        ExtentReportManager.logStep("Navigate to Register page");
//        RegisterPage registerPage = new RegisterPage(driver);
//        registerPage.navigateTo();
//
//        ExtentReportManager.logStep("Click register without filling required fields");
//        registerPage.clickRegisterButton();
//
//        ExtentReportManager.logStep("Verify registration failure");
//        Assert.assertTrue(registerPage.isErrorMessageDisplayed(), "Error message should be displayed");
//        ExtentReportManager.captureAndAttachScreenshot(driver, "Registration failed - Empty fields");
//    }
//
//    @Test(description = "Register with invalid phone number")
//    public void testRegisterWithInvalidPhone() {
//        ExtentReportManager.logStep("Navigate to Register page");
//        RegisterPage registerPage = new RegisterPage(driver);
//        registerPage.navigateTo();
//
//        String uniqueId = UUID.randomUUID().toString().substring(0, 8);
//        String fullName = "Test User " + uniqueId;
//        String invalidPhone = "123"; // Too short for a valid phone number
//        String email = "testuser" + uniqueId + "@example.com";
//        String password = "Test@123456";
//
//        ExtentReportManager.logStep("Fill registration form with invalid phone number");
//        boolean registerSuccess = registerPage.register(fullName, invalidPhone, email, password);
//
//        ExtentReportManager.logStep("Verify registration failure");
//        Assert.assertFalse(registerSuccess, "Registration should fail with invalid phone number");
//        Assert.assertTrue(registerPage.isErrorMessageDisplayed(), "Error message should be displayed");
//        ExtentReportManager.captureAndAttachScreenshot(driver, "Registration failed - Invalid phone");
//    }
//
//    @Test(description = "Register with invalid email format")
//    public void testRegisterWithInvalidEmail() {
//        ExtentReportManager.logStep("Navigate to Register page");
//        RegisterPage registerPage = new RegisterPage(driver);
//        registerPage.navigateTo();
//
//        String uniqueId = UUID.randomUUID().toString().substring(0, 8);
//        String fullName = "Test User " + uniqueId;
//        String phone = "098" + (int)(Math.random() * 10000000);
//        String invalidEmail = "notanemail"; // Invalid email format
//        String password = "Test@123456";
//
//        ExtentReportManager.logStep("Fill registration form with invalid email format");
//        boolean registerSuccess = registerPage.register(fullName, phone, invalidEmail, password);
//
//        ExtentReportManager.logStep("Verify registration failure");
//        Assert.assertFalse(registerSuccess, "Registration should fail with invalid email format");
//        Assert.assertTrue(registerPage.isErrorMessageDisplayed(), "Error message should be displayed");
//        ExtentReportManager.captureAndAttachScreenshot(driver, "Registration failed - Invalid email");
//    }
//
//    @Test(description = "Register without accepting terms and conditions")
//    public void testRegisterWithoutAcceptingTerms() {
//        ExtentReportManager.logStep("Navigate to Register page");
//        RegisterPage registerPage = new RegisterPage(driver);
//        registerPage.navigateTo();
//
//        String uniqueId = UUID.randomUUID().toString().substring(0, 8);
//        String fullName = "Test User " + uniqueId;
//        String phone = "098" + (int)(Math.random() * 10000000);
//        String email = "testuser" + uniqueId + "@example.com";
//        String password = "Test@123456";
//
//        ExtentReportManager.logStep("Fill registration form without accepting terms");
//
//        // Fill in all fields but don't check terms checkbox
//        registerPage.enterFullName(fullName);
//        registerPage.enterPhone(phone);
//        registerPage.enterEmail(email);
//        registerPage.enterPassword(password);
//        registerPage.enterConfirmPassword(password);
//
//        try {
//            registerPage.clickCaptcha();
//        } catch (Exception e) {
//            // Continue if captcha fails
//        }
//
//        registerPage.clickRegisterButton();
//
//        ExtentReportManager.logStep("Verify registration failure");
//        Assert.assertTrue(registerPage.isErrorMessageDisplayed(), "Error message should be displayed");
//        ExtentReportManager.captureAndAttachScreenshot(driver, "Registration failed - Terms not accepted");
//    }

}
