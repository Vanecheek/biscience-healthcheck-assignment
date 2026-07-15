package com.biscience.healthcheck.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public final class AiChatbotPage extends BasePage {
    private static final double RESPONSE_TIMEOUT_MS = 60_000;
    private final Locator input;
    private final Locator welcomeHeading;
    private final Locator questionGuidance;
    private final Locator clarificationHeading;
    private final Locator continueWithAnswersButton;
    private final Locator continueWithoutFiltersButton;
    private final Locator dataSources;
    private final Locator submitFeedback;

    public AiChatbotPage(Page page) {
        super(page);
        input = page.getByPlaceholder("Ask anything...");
        welcomeHeading = page.getByText(
                "How can I help you today?", new Page.GetByTextOptions().setExact(true));
        questionGuidance = page.getByText(
                "Choose a suggestion below or type your own question.",
                new Page.GetByTextOptions().setExact(true));
        clarificationHeading = page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions()
                .setName("I need some clarifications to give you the best answer").setExact(true));
        continueWithAnswersButton = page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Continue with my answers").setExact(true));
        continueWithoutFiltersButton = page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Continue without filters").setExact(true));
        dataSources = page.getByText("Data sources", new Page.GetByTextOptions().setExact(true));
        submitFeedback = page.getByText("Submit feedback", new Page.GetByTextOptions().setExact(true));
    }

    public AiChatbotPage waitUntilLoaded() {
        welcomeHeading.waitFor();
        input.waitFor();
        return this;
    }

    public AiChatbotPage askSuggestedQuestion(String question) {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(question).setExact(true)).click();
        clarificationHeading.waitFor(new Locator.WaitForOptions().setTimeout(RESPONSE_TIMEOUT_MS));
        return this;
    }

    public AiChatbotPage selectClarificationFilters(String country, String channel) {
        page.getByLabel(country, new Page.GetByLabelOptions().setExact(true)).check();
        page.getByLabel(channel, new Page.GetByLabelOptions().setExact(true)).check();
        return this;
    }

    public AiChatbotPage continueWithAnswers() {
        continueWithAnswersButton.click();
        dataSources.waitFor(new Locator.WaitForOptions().setTimeout(RESPONSE_TIMEOUT_MS));
        submitFeedback.waitFor(new Locator.WaitForOptions().setTimeout(RESPONSE_TIMEOUT_MS));
        return this;
    }

    public Locator input() { return input; }
    public Locator welcomeHeading() { return welcomeHeading; }
    public Locator questionGuidance() { return questionGuidance; }
    public Locator suggestedQuestion(String question) {
        return page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName(question).setExact(true));
    }
    public Locator clarificationHeading() { return clarificationHeading; }
    public Locator continueWithAnswersButton() { return continueWithAnswersButton; }
    public Locator continueWithoutFiltersButton() { return continueWithoutFiltersButton; }
    public Locator dataSources() { return dataSources; }
    public Locator submitFeedback() { return submitFeedback; }
}
