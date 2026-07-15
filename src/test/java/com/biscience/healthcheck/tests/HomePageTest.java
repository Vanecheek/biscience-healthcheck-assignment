package com.biscience.healthcheck.tests;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

final class HomePageTest extends AuthenticatedUiTest {

    @Test
    @Tag("smoke")
    void homePageProvidesAccessToCriticalProductAreas() {
        assertThat(homePage.globalSearch()).isVisible();
        assertThat(homePage.globalSearch()).isEditable();
        assertThat(homePage.brandsNavigation()).isVisible();
        assertThat(homePage.chatbotNavigation()).isVisible();
        assertNoBlockingRuntimeIssues();
    }
}
