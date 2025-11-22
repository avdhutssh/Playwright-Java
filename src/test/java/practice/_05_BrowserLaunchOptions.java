package practice;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.ColorScheme;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Browser Launch Options - Core Test Suite
 * Demonstrates essential browser configuration options
 * Reference: Documentation/03_Browser_Launch_Options.md
 */
public class _05_BrowserLaunchOptions {

    private static final Logger logger = Logger.getLogger(_05_BrowserLaunchOptions.class.getName());
    private static Playwright playwright;

    @BeforeSuite
    public void setUp() {
        logger.info("\n========================================");
        logger.info("🚀 Browser Launch Options Test Suite");
        logger.info("========================================\n");
        playwright = Playwright.create();
    }

    @AfterSuite
    public void tearDown() {
        if (playwright != null) {
            playwright.close();
            logger.info("\n✅ Playwright closed\n");
        }
    }

    // ========================================
    // TEST 1: Basic Launch Options
    // ========================================
    @Test(priority = 1)
    public void test_01_BasicLaunchOptions() {
        logger.info("📌 TEST 1: Basic Launch Options - Headless, SlowMo, Timeout");

        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                .setHeadless(false)           // Show browser UI
                .setSlowMo(500)               // Slow down actions by 500ms for visibility
                .setTimeout(30000));          // Launch timeout in milliseconds

        BrowserContext context = browser.newContext();
        Page page = context.newPage();
        page.navigate("https://example.com");

        Assert.assertNotNull(page.title());

        context.close();
        browser.close();
    }
}
