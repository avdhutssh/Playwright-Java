package practice;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.junit.UsePlaywright;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@UsePlaywright
public class _03_RefactoredUsingOnlyPage {

    @Test
    void _verifyPageTitleIsVisible(Page page) {
        page.navigate("https://the-internet.herokuapp.com/");
        String title = page.title();
        Assertions.assertEquals("The Internet", title);

    }

    @Test
    void searchProduct(Page page) {
        page.navigate("https://practicesoftwaretesting.com/");
        page.locator("[placeholder='Search']").fill("Plier");
        page.locator("button:has-text('Search')").click();
        Assertions.assertTrue(page.locator(".card").count() > 0);
    }
}
