package com.biscience.healthcheck.tests;

import com.biscience.healthcheck.pages.AppShellPage;
import com.biscience.healthcheck.pages.LoginPage;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class AuthenticatedUiTest extends BaseUiTest {
    protected AppShellPage app;

    @BeforeEach
    void authenticate() {
        app = new LoginPage(page).open(config.environmentUrl()).loginAs(config.login(), config.password());
        assertTrue(app.isDisplayed(), "Authenticated application shell should be usable");
    }
}
