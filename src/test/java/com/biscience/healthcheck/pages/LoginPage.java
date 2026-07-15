package com.biscience.healthcheck.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import static com.microsoft.playwright.options.AriaRole.BUTTON;

public final class LoginPage extends BasePage {
    private final Locator emailInput;
    private final Locator passwordInput;
    private final Locator loginButton;

    public LoginPage(Page page) {
        super(page);
        emailInput = page.getByPlaceholder("E-mail address");
        passwordInput = page.getByPlaceholder("Password");
        loginButton = page.getByRole(BUTTON, new Page.GetByRoleOptions().setName("Login").setExact(true));
    }

    public LoginPage open(String environmentUrl) {
        page.navigate(environmentUrl);
        emailInput.waitFor();
        return this;
    }

    public HomePage loginAs(String login, String password) {
        emailInput.fill(login);
        passwordInput.fill(password);
        loginButton.click();
        return new HomePage(page).waitUntilLoaded();
    }

    public boolean isDisplayed() {
        return emailInput.isVisible() && passwordInput.isVisible() && loginButton.isVisible();
    }
}
