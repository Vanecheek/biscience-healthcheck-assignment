package com.biscience.healthcheck.tests;

import com.biscience.healthcheck.pages.AiChatbotPage;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

final class AiChatbotTest extends AuthenticatedUiTest {
    private static final String QUESTION =
            "What are the top 10 brands by spend in the last 30 days?";

    @Test
    @Tag("smoke")
    void chatbotAcceptsAQuestionAndReturnsAUsableResponse() {
        AiChatbotPage chatbotPage = homePage.openAiChatbot()
                .askSuggestedQuestion(QUESTION);

        assertThat(chatbotPage.clarificationHeading()).isVisible();
        assertThat(chatbotPage.continueWithoutFiltersButton()).isVisible();
        assertThat(chatbotPage.continueWithoutFiltersButton()).isEnabled();
        assertNoBlockingRuntimeIssues();
    }
}
