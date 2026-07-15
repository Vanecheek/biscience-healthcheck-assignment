package com.biscience.healthcheck.tests;

import com.biscience.healthcheck.pages.AiChatbotPage;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

final class AiChatbotTest extends AuthenticatedUiTest {
    private static final String QUESTION =
            "What are the top 10 brands by spend in the last 30 days?";
    private static final String COUNTRY = "France";
    private static final String CHANNEL = "Display";

    @Test
    @Tag("smoke")
    void chatbotPageProvidesAUsableQuestionEntryPoint() {
        AiChatbotPage chatbotPage = homePage.openAiChatbot();

        assertThat(chatbotPage.welcomeHeading()).isVisible();
        assertThat(chatbotPage.questionGuidance()).isVisible();
        assertThat(chatbotPage.input()).isVisible();
        assertThat(chatbotPage.input()).isEditable();
        assertThat(chatbotPage.suggestedQuestion(QUESTION)).isVisible();
        assertNoBlockingRuntimeIssues();
    }

    @Test
    @Tag("smoke")
    void chatbotCompletesASuggestedQuestionFlow() {
        AiChatbotPage chatbotPage = homePage.openAiChatbot()
                .askSuggestedQuestion(QUESTION)
                .selectClarificationFilters(COUNTRY, CHANNEL);

        assertThat(chatbotPage.clarificationHeading()).isVisible();
        assertThat(chatbotPage.continueWithAnswersButton()).isEnabled();
        assertThat(chatbotPage.continueWithoutFiltersButton()).isVisible();
        assertThat(chatbotPage.continueWithoutFiltersButton()).isEnabled();

        chatbotPage.continueWithAnswers();

        assertThat(chatbotPage.dataSources()).isVisible();
        assertThat(chatbotPage.submitFeedback()).isVisible();
        assertNoBlockingRuntimeIssues();
    }
}
