package practice;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class _01_DemoScript {

    @Test
    void _verifyPageTitleIsVisible() {
        Playwright playwright = Playwright.create();
        Browser browser = playwright.chromium().launch();
        Page page = browser.newPage();
        page.navigate("https://the-internet.herokuapp.com/");
        String title = page.title();
        Assertions.assertEquals("The Internet", title);
        page.close();
        browser.close();
        playwright.close();
    }

    @Test
    void searchProduct() {
        Playwright playwright = Playwright.create();
        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        Page page = browser.newPage();
        page.navigate("https://practicesoftwaretesting.com/");
        page.locator("[placeholder='Search']").fill("Plier");
        page.locator("button:has-text('Search')").click();
        Assertions.assertTrue(page.locator(".card").count() > 0);
        browser.close();
        playwright.close();
    }
}
