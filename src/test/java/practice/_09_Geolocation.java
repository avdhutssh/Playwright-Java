package practice;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.Geolocation;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.logging.Logger;

public class _09_Geolocation {
    private static final Logger logger = Logger.getLogger(_09_Geolocation.class.getName());
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
    // TEST 1: Paris, France Geolocation
    // ========================================
    @Test(priority = 1)
    public void test_01_Paris_France() {
        logger.info("📌 TEST 1: Paris, France Geolocation");

        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                .setChannel("chrome")
                .setHeadless(false));

        BrowserContext context = browser.newContext(new Browser.NewContextOptions()
                .setGeolocation(new Geolocation(48.8566, 2.3522))  // Paris coordinates
                .setPermissions(Arrays.asList("geolocation"))      // Grant geolocation permission
                .setIgnoreHTTPSErrors(true));

        Page page = context.newPage();

        logger.info("📍 Location: Paris, France");
        logger.info("🌐 Latitude: 48.8566, Longitude: 2.3522");

        page.navigate("https://browserleaks.com/geo");
        page.waitForTimeout(5000);

        String latitude = page.locator("#latitude").getAttribute("data-raw");
        String longitude = page.locator("#longitude").getAttribute("data-raw");
        String location = page.locator("#geo-reverse .flag-text").textContent();

        logger.info("✅ Detected Latitude: " + latitude);
        logger.info("✅ Detected Longitude: " + longitude);
        logger.info("✅ Detected Location: " + location);

        Assert.assertTrue(latitude.contains("48.8"), "Expected Paris latitude but got: " + latitude);
        Assert.assertTrue(longitude.contains("2.3"), "Expected Paris longitude but got: " + longitude);
        Assert.assertTrue(location.contains("France"), "Expected France but got: " + location);

        context.close();
        browser.close();
    }

    // ========================================
    // TEST 2: Mumbai, India Geolocation
    // ========================================
    @Test(priority = 2)
    public void test_02_Mumbai_India() {
        logger.info("📌 TEST 2: Mumbai, India Geolocation");

        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                .setChannel("chrome")
                .setHeadless(false));

        BrowserContext context = browser.newContext(new Browser.NewContextOptions()
                .setGeolocation(new Geolocation(19.0760, 72.8777))  // Mumbai coordinates
                .setPermissions(Arrays.asList("geolocation"))        // Grant geolocation permission
                .setIgnoreHTTPSErrors(true));

        Page page = context.newPage();

        logger.info("📍 Location: Mumbai, India");
        logger.info("🌐 Latitude: 19.0760, Longitude: 72.8777");

        page.navigate("https://browserleaks.com/geo");
        page.waitForTimeout(5000);

        String latitude = page.locator("#latitude").getAttribute("data-raw");
        String longitude = page.locator("#longitude").getAttribute("data-raw");
        String location = page.locator("#geo-reverse .flag-text").textContent();

        logger.info("✅ Detected Latitude: " + latitude);
        logger.info("✅ Detected Longitude: " + longitude);
        logger.info("✅ Detected Location: " + location);

        Assert.assertTrue(latitude.contains("19.0"), "Expected Mumbai latitude but got: " + latitude);
        Assert.assertTrue(longitude.contains("72.8"), "Expected Mumbai longitude but got: " + longitude);
        Assert.assertTrue(location.contains("India"), "Expected India but got: " + location);

        context.close();
        browser.close();
    }
}

