package com.biscience.healthcheck.support;

import com.microsoft.playwright.Page;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Supplier;

public final class FailureArtifactsExtension implements AfterTestExecutionCallback {
    private static final Path SCREENSHOT_DIRECTORY = Path.of("artifacts", "screenshots");
    private final Supplier<Page> pageSupplier;

    public FailureArtifactsExtension(Supplier<Page> pageSupplier) {
        this.pageSupplier = pageSupplier;
    }

    @Override
    public void afterTestExecution(ExtensionContext extensionContext) {
        if (extensionContext.getExecutionException().isEmpty()) return;
        String artifactName = sanitize(extensionContext.getRequiredTestClass().getSimpleName()
                + "-" + extensionContext.getRequiredTestMethod().getName());
        captureScreenshot(artifactName);
    }

    private void captureScreenshot(String artifactName) {
        Page page = pageSupplier.get();
        if (page == null || page.isClosed()) return;
        try {
            Files.createDirectories(SCREENSHOT_DIRECTORY);
            page.screenshot(new Page.ScreenshotOptions()
                    .setPath(SCREENSHOT_DIRECTORY.resolve(artifactName + ".png")).setFullPage(true));
        } catch (Exception exception) {
            System.err.println("Could not capture failure screenshot: " + exception.getMessage());
        }
    }

    private static String sanitize(String value) {
        return value.replaceAll("[^a-zA-Z0-9._-]", "-");
    }
}
