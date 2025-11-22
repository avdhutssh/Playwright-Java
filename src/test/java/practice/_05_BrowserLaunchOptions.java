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

    // ========================================
    // TEST 6: Ignore HTTPS Errors
    // ========================================
    @Test(priority = 6)
    public void test_06_IgnoreHTTPSErrors() {
        logger.info("📌 TEST 6: Ignore HTTPS Errors - Accept self-signed certificates");

        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                .setHeadless(false));                  // Show browser UI

        BrowserContext context = browser.newContext(new Browser.NewContextOptions()
                .setIgnoreHTTPSErrors(true));          // Ignore SSL/TLS certificate errors

        Page page = context.newPage();
        page.navigate("https://expired.badssl.com/");  // Site with expired certificate

        Assert.assertNotNull(page.content());

        context.close();
        browser.close();
    }

    // ========================================
    // TEST 7: Custom HTTP Headers
    // ========================================
    @Test(priority = 7)
    public void test_07_CustomHTTPHeaders() {
        logger.info("📌 TEST 7: Custom HTTP Headers - Add headers to all requests");

        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                .setHeadless(false));                  // Show browser UI

        BrowserContext context = browser.newContext(new Browser.NewContextOptions()
                .setExtraHTTPHeaders(Map.of(           // Add custom HTTP headers
                        "X-Custom-Header", "AvdhutDemo",
                        "X-API-Key", "test-key-123"
                )));

        Page page = context.newPage();
        page.navigate("https://httpbin.org/headers");

        Assert.assertTrue(page.content().contains("AvdhutDemo"));

        context.close();
        browser.close();
    }

    // ========================================
    // TEST 8: Offline Mode
    // ========================================
    @Test(priority = 8)
    public void test_08_OfflineMode() {
        logger.info("📌 TEST 8: Offline Mode - Simulate no internet connection");

        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                .setHeadless(false));                  // Show browser UI

        BrowserContext context = browser.newContext(new Browser.NewContextOptions()
                .setOffline(true));                    // Enable offline mode

        Page page = context.newPage();

        Assert.assertThrows(Exception.class, () -> {
            page.navigate("https://example.com", new Page.NavigateOptions().setTimeout(5000));
        });


        context.close();
        browser.close();
    }

    // ========================================
    // TEST 9: Color Scheme (Dark Mode)
    // ========================================
    @Test(priority = 9)
    public void test_09_ColorScheme() {
        logger.info("📌 TEST 9: Color Scheme - Force dark mode preference");

        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                .setHeadless(false));

        BrowserContext darkContext = browser.newContext(new Browser.NewContextOptions()
                .setColorScheme(ColorScheme.DARK));

        Page darkPage = darkContext.newPage();

        darkPage.navigate("https://developer.mozilla.org");
        darkPage.waitForTimeout(3000);

        String colorScheme = (String) darkPage.evaluate(
                "() => window.matchMedia('(prefers-color-scheme: dark)').matches ? 'dark' : 'light'"
        );

        logger.info("Color scheme detected: " + colorScheme);
        Assert.assertEquals(colorScheme, "dark");

        darkContext.close();

        BrowserContext lightContext = browser.newContext(new Browser.NewContextOptions()
                .setColorScheme(ColorScheme.LIGHT));

        Page lightPage = lightContext.newPage();
        lightPage.navigate("https://developer.mozilla.org");
        lightPage.waitForTimeout(3000);

        String lightScheme = (String) lightPage.evaluate(
                "() => window.matchMedia('(prefers-color-scheme: dark)').matches ? 'dark' : 'light'"
        );

        logger.info("Color scheme detected: " + lightScheme);
        Assert.assertEquals(lightScheme, "light");

        lightContext.close();
        browser.close();
    }

    // ========================================
    // TEST 10: CI/CD Optimized Configuration
    // ========================================
    @Test(priority = 10)
    public void test_10_CICDConfiguration() {
        logger.info("📌 TEST 10: CI/CD Configuration - Docker-friendly settings");

        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                .setHeadless(false)                    // Show browser UI (set true for CI)
                .setArgs(Arrays.asList(
                        "--no-sandbox",                // Required for Docker containers
                        "--disable-gpu",               // Disable GPU hardware acceleration
                        "--disable-dev-shm-usage",     // Overcome limited shared memory
                        "--disable-setuid-sandbox",    // Additional sandboxing flag
                        "--disable-extensions"         // Disable browser extensions
                )));

        BrowserContext context = browser.newContext(new Browser.NewContextOptions()
                .setViewportSize(1920, 1080)           // Standard viewport for CI
                .setIgnoreHTTPSErrors(true));          // Ignore SSL errors in test env

        Page page = context.newPage();
        page.navigate("https://example.com");

        Assert.assertNotNull(page.title());

        context.close();
        browser.close();
    }

    // ========================================
    // TEST 11: Channel Selection
    // ========================================
    @Test(priority = 11)
    public void test_11_ChannelSelection() {
        logger.info("📌 TEST 11: Channel Selection - Use Chrome instead of Chromium");

        try {
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                    .setHeadless(false)                // Show browser UI
                    .setChannel("chrome"));            // Use installed Chrome browser

            BrowserContext context = browser.newContext();
            Page page = context.newPage();
            page.navigate("https://example.com");

            Assert.assertNotNull(page.title());

            context.close();
            browser.close();
        } catch (Exception e) {
            logger.warning("Chrome not installed, test skipped");
        }
    }
}
