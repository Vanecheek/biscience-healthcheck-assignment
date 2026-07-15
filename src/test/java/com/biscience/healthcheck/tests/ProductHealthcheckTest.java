package com.biscience.healthcheck.tests;

import com.biscience.healthcheck.pages.AiChatbotPage;
import com.biscience.healthcheck.pages.BrandsPage;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@Tag("smoke")
final class ProductHealthcheckTest extends AuthenticatedUiTest {
    private static final String HEALTHCHECK_BRAND = "Nike";
    private static final String CHATBOT_QUESTION = "What are the top 10 brands by spend in the last 30 days?";

    @Test
    void brandsSearchOpensAUsableBrandReport() {
        BrandsPage brands = app.openBrands().openReportFor(HEALTHCHECK_BRAND);
        assertThat(brands.reportHeading()).isVisible();
        assertThat(brands.mediaCoverageSection()).isVisible();
        assertThat(brands.performanceOverviewSection()).isVisible();
        assertNoBlockingRuntimeIssues();
    }

    @Test
    void aiChatbotAcceptsAQuestionAndReturnsAUsableResponse() {
        AiChatbotPage chatbot = app.openChatbot().askSuggestedQuestion(CHATBOT_QUESTION);
        assertThat(chatbot.clarificationHeading()).isVisible();
        assertThat(chatbot.continueWithoutFiltersButton()).isVisible();
        assertThat(chatbot.continueWithoutFiltersButton()).isEnabled();
        assertNoBlockingRuntimeIssues();
    }
}
