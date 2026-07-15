package com.biscience.healthcheck.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public final class AiChatbotPage extends BasePage {
    private static final double RESPONSE_TIMEOUT_MS = 60_000;
    private final Locator input;
    private final Locator clarificationHeading;
    private final Locator continueWithoutFiltersButton;

    public AiChatbotPage(Page page) {
        super(page);
        input = page.getByPlaceholder("Ask anything...");
        clarificationHeading = page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions()
                .setName("I need some clarifications to give you the best answer").setExact(true));
        continueWithoutFiltersButton = page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Continue without filters").setExact(true));
    }

    public AiChatbotPage waitUntilLoaded() {
        page.getByText("How can I help you today?", new Page.GetByTextOptions().setExact(true)).waitFor();
        input.waitFor();
        return this;
    }

    public AiChatbotPage askSuggestedQuestion(String question) {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(question).setExact(true)).click();
        clarificationHeading.waitFor(new Locator.WaitForOptions().setTimeout(RESPONSE_TIMEOUT_MS));
        return this;
    }

    public Locator clarificationHeading() { return clarificationHeading; }
    public Locator continueWithoutFiltersButton() { return continueWithoutFiltersButton; }
}
