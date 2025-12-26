package pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;

/**
 * Base page class with common functionality for all pages
 */
public abstract class BasePage {
    protected Page page;
    protected static final String BASE_URL = "https://avito-tech-internship-psi.vercel.app";

    public BasePage(Page page) {
        this.page = page;
    }

    public void waitForPageLoad() {
        page.waitForLoadState();
    }

    public void waitForElement(String selector) {
        page.waitForSelector(selector, new Page.WaitForSelectorOptions()
                .setState(WaitForSelectorState.VISIBLE));
    }

    public void waitForElementHidden(String selector) {
        page.waitForSelector(selector, new Page.WaitForSelectorOptions()
                .setState(WaitForSelectorState.HIDDEN));
    }

    public boolean isElementVisible(String selector) {
        return page.locator(selector).isVisible();
    }

    public String getCurrentUrl() {
        return page.url();
    }

    public void click(String selector) {
        page.locator(selector).click();
    }

    public void fill(String selector, String text) {
        page.locator(selector).fill(text);
    }

    public String getText(String selector) {
        return page.locator(selector).textContent();
    }
}
