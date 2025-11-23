package practice;

import com.microsoft.playwright.*;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.util.logging.Logger;

public class _07_MobileEmulation {
    private static final Logger logger = Logger.getLogger(_07_MobileEmulation.class.getName());
    private static Playwright playwright;

    @BeforeSuite
    public void setup() {
        playwright = Playwright.create();
    }

    @AfterSuite
    public void teardown() {
        if (playwright != null) {
            playwright.close();
        }
    }

    // ========================================
    // TEST 1: iPhone 14 Pro Emulation
    // ========================================
    @Test(priority = 1)
    public void test_01_iPhone14Pro() {
        logger.info("📌 TEST 1: iPhone 14 Pro Emulation");

        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                .setChannel("chrome")
                .setHeadless(false));

        // iPhone 14 Pro specifications
        BrowserContext context = browser.newContext(new Browser.NewContextOptions()
                .setViewportSize(390, 844)        // iPhone 14 Pro screen dimensions
                .setDeviceScaleFactor(3)          // Super Retina XDR display (3x pixel ratio)
                .setIsMobile(true)                // Enable mobile mode
                .setHasTouch(true)                // Enable touch events
                .setIgnoreHTTPSErrors(true)       // Ignore SSL certificate errors
                .setUserAgent("Mozilla/5.0 (iPhone; CPU iPhone OS 17_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.0 Mobile/15E148 Safari/604.1")
        );

        Page page = context.newPage();

        logger.info("📱 Device: iPhone 14 Pro");
        logger.info("📐 Viewport: 390 x 844");
        logger.info("🎨 Device Scale Factor: 3x (Super Retina XDR)");

        page.navigate("https://www.whatismybrowser.com/");
        Assert.assertEquals(page.viewportSize().width, 390);
        Assert.assertEquals(page.viewportSize().height, 844);
        logger.info("✅ Viewport verified: 390x844");

        // Verify device detection
        String deviceText = page.locator(".string-medium").first().textContent();
        Assert.assertTrue(deviceText.contains("Apple iPhone"), "Expected 'Apple iPhone' but got: " + deviceText);
        logger.info("✅ Device detected as: " + deviceText);
        page.waitForTimeout(2000);

        page.navigate("https://www.useragentstring.com/");
        page.waitForTimeout(3000);

        logger.info("✅ iPhone 14 Pro emulation active");

        context.close();
        browser.close();
    }

}

