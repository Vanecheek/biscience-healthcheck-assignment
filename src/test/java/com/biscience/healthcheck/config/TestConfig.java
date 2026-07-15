package com.biscience.healthcheck.config;

import io.github.cdimascio.dotenv.Dotenv;
import java.net.URI;
import java.time.Duration;

public record TestConfig(String environmentUrl, String login, String password, boolean headless,
                         double slowMoMs, Duration actionTimeout) {
    private static final Dotenv DOTENV = Dotenv.configure().ignoreIfMissing().load();

    public static TestConfig load() {
        String environmentUrl = required("environment.url", "ENVIRONMENT_URL");
        validateUrl(environmentUrl);
        return new TestConfig(stripTrailingSlash(environmentUrl), required("login", "LOGIN"),
                required("password", "PASSWORD"),
                Boolean.parseBoolean(value("headless", "HEADLESS", "true")),
                Double.parseDouble(value("slow.mo.ms", "SLOW_MO_MS", "0")),
                Duration.ofMillis(Long.parseLong(value("action.timeout.ms", "ACTION_TIMEOUT_MS", "15000"))));
    }

    private static String required(String systemProperty, String environmentVariable) {
        String configuredValue = value(systemProperty, environmentVariable, null);
        if (configuredValue == null || configuredValue.isBlank()) {
            throw new IllegalStateException("Missing required configuration: " + environmentVariable);
        }
        return configuredValue;
    }

    private static String value(String systemProperty, String environmentVariable, String defaultValue) {
        String systemValue = System.getProperty(systemProperty);
        if (systemValue != null && !systemValue.isBlank()) return systemValue;
        String environmentValue = System.getenv(environmentVariable);
        if (environmentValue != null && !environmentValue.isBlank()) return environmentValue;
        String dotenvValue = DOTENV.get(environmentVariable);
        return dotenvValue == null || dotenvValue.isBlank() ? defaultValue : dotenvValue;
    }

    private static void validateUrl(String value) {
        try {
            URI uri = URI.create(value);
            if (!uri.isAbsolute() || uri.getHost() == null) throw new IllegalArgumentException();
        } catch (IllegalArgumentException exception) {
            throw new IllegalStateException("ENVIRONMENT_URL must be a valid absolute URL", exception);
        }
    }

    private static String stripTrailingSlash(String value) {
        return value.endsWith("/") ? value.substring(0, value.length() - 1) : value;
    }
}
