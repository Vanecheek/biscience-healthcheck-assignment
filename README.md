# UI System Healthcheck

Recurring Java + Playwright smoke suite for critical AdClarity user journeys.

## What the healthcheck verifies

The suite checks three focused user journeys:

1. **Home page smoke:** an authenticated user reaches a usable home page with global search and navigation to both critical product areas.
2. **Brands:** the landing page exposes saved/recent reports, exact-brand search returns `Nike`, and the selected report contains Media coverage, Performance overview, and core performance metrics.
3. **AI Chatbot:** the question entry point is usable, and a user can submit the built-in top-brands question, select clarification filters, and receive a completed response with data-source and feedback controls.

Every journey also checks for uncaught page errors, blocking console errors, product HTTP 5xx responses, and non-aborted critical product request failures.

The suite deliberately avoids exhaustive functional coverage. It is small enough for frequent execution and focuses on signals that the product's main paths remain usable.

## Architecture

```text
src/test/java/com/biscience/healthcheck/
├── config/       # typed environment/.env configuration
├── pages/        # one classic Page Object per UI page
├── support/      # runtime diagnostics and failure artifacts
└── tests/        # one test class per product area/journey
```

The page layer contains separate `LoginPage`, `HomePage`, `BrandsPage`, and `AiChatbotPage` objects. The test layer mirrors the requested scope with `HomePageTest`, `BrandsTest`, and `AiChatbotTest`.

The browser is reused within a test class. Each test gets a fresh `BrowserContext` and page, so cookies and local storage cannot leak between scenarios. Assertions remain in test classes; Page Objects contain locators, page synchronization, navigation, and user interactions.

## Prerequisites

- JDK 25
- Maven 3.9+

For Homebrew JDK 25 on Apple Silicon:

```bash
export JAVA_HOME=/opt/homebrew/Cellar/jdk-25.0.2/jdk-25.0.2+10/Contents/Home
export PATH="$JAVA_HOME/bin:$PATH"
```

## Configuration

Create the local configuration from the template:

```bash
cp .env.example .env
```

Required values:

```dotenv
ENVIRONMENT_URL=https://your-environment.example.com
LOGIN=your-login
PASSWORD=your-password
```

`.env` is ignored by Git. CI uses encrypted repository secrets instead.

Optional values:

- `HEADLESS=false` opens Chromium locally; default is `true`.
- `SLOW_MO_MS=500` slows actions for visual observation; default is `0`.
- `ACTION_TIMEOUT_MS=20000` changes the normal Playwright action timeout; default is `15000`.
- Java system properties override environment variables, for example `-Denvironment.url=https://...`.

## Install

```bash
mvn test-compile
mvn exec:java -Dexec.classpathScope=test \
  -Dexec.mainClass=com.microsoft.playwright.CLI \
  -Dexec.args="install chromium"
```

## Run

```bash
mvn test
HEADLESS=true SLOW_MO_MS=0 mvn test
HEADLESS=false SLOW_MO_MS=700 mvn test
mvn test -Dtest=HomePageTest
mvn test -Dtest=BrandsTest
mvn test -Dtest=AiChatbotTest
```

## Reports and failure diagnostics

JUnit reports are generated in `target/surefire-reports/`. Generate the HTML report with:

```bash
mvn surefire-report:report-only
```

When a test fails, a full-page screenshot is written to `artifacts/screenshots/`. Network traces are intentionally not persisted because they can contain short-lived authentication headers.

## CI and notifications

`.github/workflows/ui-healthcheck.yml` runs every 30 minutes and can also be started manually. Configure `HEALTHCHECK_LOGIN`, `HEALTHCHECK_PASSWORD`, and optionally `FAILURE_WEBHOOK_URL` as repository secrets.
