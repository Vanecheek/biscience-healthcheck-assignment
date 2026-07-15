package com.biscience.healthcheck.tests;

import com.biscience.healthcheck.pages.LoginPage;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("smoke")
final class EnvironmentAvailabilityTest extends BaseUiTest {
    @Test
    void loginEntryPointIsAvailable() {
        LoginPage loginPage = new LoginPage(page).open(config.environmentUrl());
        assertTrue(loginPage.isDisplayed(), "Login form should be visible and usable");
        assertNoBlockingRuntimeIssues();
    }
}
