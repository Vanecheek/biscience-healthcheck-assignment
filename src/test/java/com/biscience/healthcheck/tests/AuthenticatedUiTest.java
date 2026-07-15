package com.biscience.healthcheck.tests;

import com.biscience.healthcheck.pages.HomePage;
import com.biscience.healthcheck.pages.LoginPage;
import org.junit.jupiter.api.BeforeEach;

public abstract class AuthenticatedUiTest extends BaseUiTest {
    protected HomePage homePage;

    @BeforeEach
    void authenticate() {
        homePage = new LoginPage(page)
                .open(config.environmentUrl())
                .loginAs(config.login(), config.password());
    }
}
