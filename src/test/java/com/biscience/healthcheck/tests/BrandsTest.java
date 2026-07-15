package com.biscience.healthcheck.tests;

import com.biscience.healthcheck.pages.BrandsPage;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

final class BrandsTest extends AuthenticatedUiTest {
    private static final String BRAND = "Nike";

    @Test
    @Tag("smoke")
    void brandsPageSupportsSearchingForAnExactBrand() {
        BrandsPage brandsPage = homePage.openBrands();

        assertThat(brandsPage.searchInput()).isVisible();
        assertThat(brandsPage.savedReportsSection()).isVisible();
        assertThat(brandsPage.recentReportsSection()).isVisible();

        brandsPage.searchFor(BRAND);

        assertThat(brandsPage.searchInput()).hasValue(BRAND);
        assertThat(brandsPage.matchingBrandOption(BRAND)).isVisible();
        assertNoBlockingRuntimeIssues();
    }

    @Test
    @Tag("smoke")
    void brandSearchOpensAUsableReport() {
        BrandsPage brandsPage = homePage.openBrands()
                .openReportFor(BRAND);

        assertThat(brandsPage.reportHeading()).isVisible();
        assertThat(brandsPage.mediaCoverageSection()).isVisible();
        assertThat(brandsPage.performanceOverviewSection()).isVisible();
        assertThat(brandsPage.spendMetric()).isVisible();
        assertThat(brandsPage.impressionsMetric()).isVisible();
        assertThat(brandsPage.cpmMetric()).isVisible();
        assertThat(brandsPage.ctrMetric()).isVisible();
        assertNoBlockingRuntimeIssues();
    }
}
