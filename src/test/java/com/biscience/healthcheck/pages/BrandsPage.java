package com.biscience.healthcheck.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import java.util.regex.Pattern;

public final class BrandsPage extends BasePage {
    private static final double REPORT_TIMEOUT_MS = 30_000;
    private final Locator searchInput;
    private final Locator reportHeading;
    private final Locator mediaCoverageSection;
    private final Locator performanceOverviewSection;

    public BrandsPage(Page page) {
        super(page);
        searchInput = page.getByPlaceholder("Search brands");
        reportHeading = page.getByText("Brand report", new Page.GetByTextOptions().setExact(true))
                .filter(new Locator.FilterOptions().setVisible(true));
        mediaCoverageSection = page.getByText("Media coverage", new Page.GetByTextOptions().setExact(true))
                .filter(new Locator.FilterOptions().setVisible(true));
        performanceOverviewSection = page.getByText("Performance overview", new Page.GetByTextOptions().setExact(true))
                .filter(new Locator.FilterOptions().setVisible(true));
    }

    public BrandsPage waitUntilLoaded() {
        page.waitForURL(Pattern.compile(".*/ad-intelligence/brand/?$"));
        searchInput.waitFor();
        return this;
    }

    public BrandsPage openReportFor(String brandName) {
        searchInput.fill(brandName);
        Locator firstMatchingOption = page.getByRole(AriaRole.OPTION).first();
        firstMatchingOption.waitFor();
        firstMatchingOption.click();
        page.waitForURL(Pattern.compile(".*/ad-intelligence/brand/\\d+$"));
        reportHeading.waitFor(new Locator.WaitForOptions().setTimeout(REPORT_TIMEOUT_MS));
        mediaCoverageSection.waitFor(new Locator.WaitForOptions().setTimeout(REPORT_TIMEOUT_MS));
        performanceOverviewSection.waitFor(new Locator.WaitForOptions().setTimeout(REPORT_TIMEOUT_MS));
        return this;
    }

    public Locator reportHeading() { return reportHeading; }
    public Locator mediaCoverageSection() { return mediaCoverageSection; }
    public Locator performanceOverviewSection() { return performanceOverviewSection; }
}
