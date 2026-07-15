package com.biscience.healthcheck;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JavaRuntimeSmokeTest {
    @Test
    @Tag("smoke")
    void usesJava25OrNewerRuntime() {
        assertTrue(Runtime.version().feature() >= 25, "Expected Java 25+ runtime");
    }
}
