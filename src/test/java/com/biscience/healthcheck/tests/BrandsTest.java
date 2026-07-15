package com.biscience.healthcheck.tests;

import com.biscience.healthcheck.pages.BrandsPage;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

final class BrandsTest extends AuthenticatedUiTest {
    private static final String BRAND = "Nike";

    @Test
    @Tag("smoke")
    void brandSearchOpensAUsableReport() {
        BrandsPage brandsPage = homePage.openBrands()
                .openReportFor(BRAND);

        assertThat(brandsPage.reportHeading()).isVisible();
        assertThat(brandsPage.mediaCoverageSection()).isVisible();
        assertThat(brandsPage.performanceOverviewSection()).isVisible();
        assertNoBlockingRuntimeIssues();
    }
}
