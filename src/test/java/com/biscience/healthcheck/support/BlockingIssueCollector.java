package com.biscience.healthcheck.support;

import com.microsoft.playwright.ConsoleMessage;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Request;
import com.microsoft.playwright.Response;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class BlockingIssueCollector {
    private static final Set<String> CRITICAL_RESOURCE_TYPES = Set.of("document", "xhr", "fetch");
    private final List<String> blockingIssues = new ArrayList<>();

    public BlockingIssueCollector(Page page) {
        page.onPageError(error -> blockingIssues.add("Uncaught page error: " + error));
        page.onConsoleMessage(this::captureBlockingConsoleError);
        page.onResponse(this::captureServerError);
        page.onRequestFailed(this::captureFailedRequest);
    }

    public void assertNoBlockingIssues() {
        assertTrue(blockingIssues.isEmpty(), () -> "Blocking browser/runtime issues detected:\n- "
                + String.join("\n- ", blockingIssues));
    }

    private void captureBlockingConsoleError(ConsoleMessage message) {
        if (!"error".equals(message.type())) return;
        String normalized = message.text().toLowerCase(Locale.ROOT);
        if (normalized.contains("uncaught") || normalized.contains("chunkloaderror")
                || normalized.contains("out of memory")) {
            blockingIssues.add("Blocking console error: " + message.text());
        }
    }

    private void captureServerError(Response response) {
        if (response.status() >= 500 && isProductUrl(response.url())) {
            blockingIssues.add("HTTP " + response.status() + ": " + response.url());
        }
    }

    private void captureFailedRequest(Request request) {
        String failure = request.failure();
        if (CRITICAL_RESOURCE_TYPES.contains(request.resourceType()) && isProductUrl(request.url())
                && (failure == null || !failure.contains("ERR_ABORTED"))) {
            blockingIssues.add("Failed " + request.resourceType() + " request: "
                    + request.method() + " " + request.url() + " (" + failure + ")");
        }
    }

    private static boolean isProductUrl(String url) {
        try {
            String host = URI.create(url).getHost();
            return host != null && (host.equals("adcint.com") || host.endsWith(".adcint.com"));
        } catch (IllegalArgumentException exception) {
            return false;
        }
    }
}
