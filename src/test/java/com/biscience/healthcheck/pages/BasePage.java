package com.biscience.healthcheck.pages;

import com.microsoft.playwright.Page;
import java.util.Objects;

public abstract class BasePage {
    protected final Page page;

    protected BasePage(Page page) {
        this.page = Objects.requireNonNull(page, "page must not be null");
    }
}
