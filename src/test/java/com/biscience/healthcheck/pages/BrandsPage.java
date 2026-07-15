package com.biscience.healthcheck.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import java.util.regex.Pattern;

public final class BrandsPage extends BasePage {
    private static final double REPORT_TIMEOUT_MS = 30_000;
    private final Locator searchInput;
    private final Locator savedReportsSection;
    private final Locator recentReportsSection;
    private final Locator reportHeading;
    private final Locator mediaCoverageSection;
    private final Locator performanceOverviewSection;
    private final Locator spendMetric;
    private final Locator impressionsMetric;
    private final Locator cpmMetric;
    private final Locator ctrMetric;

    public BrandsPage(Page page) {
        super(page);
        searchInput = page.getByPlaceholder("Search brands");
        savedReportsSection = page.getByText(
                "Saved Brand reports", new Page.GetByTextOptions().setExact(true));
        recentReportsSection = page.getByText(
                "Recent Brand reports", new Page.GetByTextOptions().setExact(true));
        reportHeading = page.getByText("Brand report", new Page.GetByTextOptions().setExact(true))
                .filter(new Locator.FilterOptions().setVisible(true));
        mediaCoverageSection = page.getByText("Media coverage", new Page.GetByTextOptions().setExact(true))
                .filter(new Locator.FilterOptions().setVisible(true));
        performanceOverviewSection = page.getByText("Performance overview", new Page.GetByTextOptions().setExact(true))
                .filter(new Locator.FilterOptions().setVisible(true));
        spendMetric = visibleText("Spend").first();
        impressionsMetric = visibleText("Impressions").first();
        cpmMetric = visibleText("CPM").first();
        ctrMetric = visibleText("CTR").first();
    }

    public BrandsPage waitUntilLoaded() {
        page.waitForURL(Pattern.compile(".*/ad-intelligence/brand/?$"));
        searchInput.waitFor();
        return this;
    }

    public BrandsPage openReportFor(String brandName) {
        searchFor(brandName);
        matchingBrandOption(brandName).click();
        page.waitForURL(Pattern.compile(".*/ad-intelligence/brand/\\d+$"));
        reportHeading.waitFor(new Locator.WaitForOptions().setTimeout(REPORT_TIMEOUT_MS));
        mediaCoverageSection.waitFor(new Locator.WaitForOptions().setTimeout(REPORT_TIMEOUT_MS));
        performanceOverviewSection.waitFor(new Locator.WaitForOptions().setTimeout(REPORT_TIMEOUT_MS));
        return this;
    }

    public BrandsPage searchFor(String brandName) {
        searchInput.fill(brandName);
        matchingBrandOption(brandName).waitFor();
        return this;
    }

    public Locator matchingBrandOption(String brandName) {
        Locator exactBrandName = page.getByText(
                brandName, new Page.GetByTextOptions().setExact(true));
        return page.getByRole(AriaRole.OPTION)
                .filter(new Locator.FilterOptions().setHas(exactBrandName))
                .first();
    }

    public Locator searchInput() { return searchInput; }
    public Locator savedReportsSection() { return savedReportsSection; }
    public Locator recentReportsSection() { return recentReportsSection; }
    public Locator reportHeading() { return reportHeading; }
    public Locator mediaCoverageSection() { return mediaCoverageSection; }
    public Locator performanceOverviewSection() { return performanceOverviewSection; }
    public Locator spendMetric() { return spendMetric; }
    public Locator impressionsMetric() { return impressionsMetric; }
    public Locator cpmMetric() { return cpmMetric; }
    public Locator ctrMetric() { return ctrMetric; }

    private Locator visibleText(String text) {
        return page.getByText(text, new Page.GetByTextOptions().setExact(true))
                .filter(new Locator.FilterOptions().setVisible(true));
    }
}
