package practice;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class _02_RefactoredDemoScript {

    Playwright playwright;
    Browser browser;
    Page page;

    @BeforeEach
    void setup() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch();
        page = browser.newPage();
    }

    @AfterEach
    void teardown() {
        page.close();
        browser.close();
        playwright.close();
    }

    @Test
    void _verifyPageTitleIsVisible() {
        page.navigate("https://the-internet.herokuapp.com/");
        String title = page.title();
        Assertions.assertEquals("The Internet", title);

    }

    @Test
    void searchProduct() {
        page.navigate("https://practicesoftwaretesting.com/");
        page.locator("[placeholder='Search']").fill("Plier");
        page.locator("button:has-text('Search')").click();
        Assertions.assertTrue(page.locator(".card").count() > 0);
    }
}
