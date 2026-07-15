package com.biscience.healthcheck.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import java.util.regex.Pattern;

public final class AppShellPage extends BasePage {
    private final Locator globalSearch;
    private final Locator brandsNavigation;
    private final Locator chatbotNavigation;

    public AppShellPage(Page page) {
        super(page);
        globalSearch = page.getByPlaceholder("Search for Brands, Categories, Advertisers, Publishers, and more...");
        brandsNavigation = page.getByText("Brands", new Page.GetByTextOptions().setExact(true));
        chatbotNavigation = page.locator("a").filter(new Locator.FilterOptions().setHasText("Ask anything"));
    }

    public AppShellPage waitUntilLoaded() {
        page.waitForURL(Pattern.compile(".*/ad-intelligence/.*"));
        globalSearch.waitFor();
        return this;
    }

    public BrandsPage openBrands() {
        brandsNavigation.click();
        return new BrandsPage(page).waitUntilLoaded();
    }

    public AiChatbotPage openChatbot() {
        chatbotNavigation.click();
        return new AiChatbotPage(page).waitUntilLoaded();
    }

    public boolean isDisplayed() {
        return globalSearch.isVisible() && brandsNavigation.isVisible() && chatbotNavigation.isVisible();
    }
}
