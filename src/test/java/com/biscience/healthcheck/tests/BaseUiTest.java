package com.biscience.healthcheck.tests;

import com.biscience.healthcheck.config.TestConfig;
import com.biscience.healthcheck.support.BlockingIssueCollector;
import com.biscience.healthcheck.support.FailureArtifactsExtension;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.RegisterExtension;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BaseUiTest {
    protected final TestConfig config = TestConfig.load();
    protected Page page;
    protected BlockingIssueCollector blockingIssues;
    private Playwright playwright;
    private Browser browser;
    private BrowserContext context;

    @RegisterExtension
    final FailureArtifactsExtension failureArtifacts = new FailureArtifactsExtension(() -> page);

    @BeforeAll
    void launchBrowser() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                .setHeadless(config.headless()).setSlowMo(config.slowMoMs()));
    }

    @BeforeEach
    void createIsolatedPage() {
        context = browser.newContext(new Browser.NewContextOptions().setViewportSize(1440, 900));
        context.setDefaultTimeout(config.actionTimeout().toMillis());
        page = context.newPage();
        blockingIssues = new BlockingIssueCollector(page);
    }

    @AfterEach
    void closeContext() {
        if (context != null) context.close();
        page = null;
        blockingIssues = null;
    }

    @AfterAll
    void closeBrowser() {
        if (browser != null) browser.close();
        if (playwright != null) playwright.close();
    }

    protected void assertNoBlockingRuntimeIssues() {
        blockingIssues.assertNoBlockingIssues();
    }
}
