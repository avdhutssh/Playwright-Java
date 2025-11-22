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

    // ========================================
    // TEST 2: Browser Arguments
    // ========================================
    @Test(priority = 2)
    public void test_02_BrowserArguments() {
        logger.info("📌 TEST 2: Browser Arguments - Custom window size, maximize, disable notifications");

        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                .setHeadless(false)                    // Show browser UI
                .setArgs(Arrays.asList(
                        "--window-size=1280,720",      // Set initial window size
                        "--start-maximized",           // Start browser maximized
                        "--disable-notifications"      // Disable browser notifications
                )));

        BrowserContext context = browser.newContext();
        Page page = context.newPage();
        page.navigate("https://example.com");

        Assert.assertNotNull(page.title());

        context.close();
        browser.close();
    }

    // ========================================
    // TEST 3: Viewport Configuration
    // ========================================
    @Test(priority = 3)
    public void test_03_ViewportConfiguration() {
        logger.info("📌 TEST 3: Viewport Configuration - Custom viewport dimensions");

        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                .setHeadless(false));                  // Show browser UI

        BrowserContext context = browser.newContext(new Browser.NewContextOptions()
                .setViewportSize(1920, 1080));         // Set viewport width x height

        Page page = context.newPage();
        page.navigate("https://example.com");

        Assert.assertEquals(page.viewportSize().width, 1920);
        Assert.assertEquals(page.viewportSize().height, 1080);

        context.close();
        browser.close();
    }

    // ========================================
    // TEST 4: Device Scale Factor
    // ========================================
    @Test(priority = 4)
    public void test_04_DeviceScaleFactor() {
        logger.info("📌 TEST 4: Device Scale Factor - High-DPI rendering (Retina)");

        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                .setHeadless(false));                  // Show browser UI

        BrowserContext context = browser.newContext(new Browser.NewContextOptions()
                .setViewportSize(1920, 1080)           // Set viewport size
                .setDeviceScaleFactor(2));             // Pixel ratio: 2x (Retina display)

        Page page = context.newPage();
        page.navigate("https://example.com");

        Assert.assertNotNull(page.content());

        context.close();
        browser.close();
    }

    // ========================================
    // TEST 5: Custom User Agent
    // ========================================
    @Test(priority = 5)
    public void test_05_CustomUserAgent() {
        logger.info("📌 TEST 5: Custom User Agent - Set custom UA string");

        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                .setHeadless(false));                  // Show browser UI

        BrowserContext context = browser.newContext(new Browser.NewContextOptions()
                .setUserAgent("MyTestBot/1.0 (Automated Testing)"));  // Custom user agent string

        Page page = context.newPage();
        String userAgent = (String) page.evaluate("() => navigator.userAgent");

        Assert.assertTrue(userAgent.contains("MyTestBot"));

        context.close();
        browser.close();
    }
}
