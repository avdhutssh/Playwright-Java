package practice;

import com.microsoft.playwright.*;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.util.logging.Logger;

public class _08_LocalizationSettings {
    private static final Logger logger = Logger.getLogger(_08_LocalizationSettings.class.getName());
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
    // TEST 1: French (France) Localization
    // ========================================
    @Test(priority = 1)
    public void test_01_French_France() {
        logger.info("📌 TEST 1: French (France) Localization");

        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                .setChannel("chrome")
                .setHeadless(false));

        BrowserContext context = browser.newContext(new Browser.NewContextOptions()
                .setLocale("fr-FR")                    // Set French (France) locale
                .setTimezoneId("Europe/Paris")         // Set Paris timezone
                .setIgnoreHTTPSErrors(true));          // Ignore SSL certificate errors

        Page page = context.newPage();

        logger.info("🌍 Locale: fr-FR (French - France)");
        logger.info("🕐 Timezone: Europe/Paris");

        page.navigate("https://browserleaks.com/javascript");
        page.waitForTimeout(3000);

        String localeText = page.locator("#js-locale").textContent();
        Assert.assertTrue(localeText.contains("fr-FR"),
                "Expected French locale but got: " + localeText);
        logger.info("✅ Locale detected as: " + localeText);

        page.waitForTimeout(2000);

        context.close();
        browser.close();
    }

    // ========================================
    // TEST 2: Hindi (India) Localization
    // ========================================
    @Test(priority = 2)
    public void test_02_Hindi_India() {
        logger.info("📌 TEST 2: Hindi (India) Localization");

        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                .setChannel("chrome")
                .setHeadless(false));

        BrowserContext context = browser.newContext(new Browser.NewContextOptions()
                .setLocale("hi-IN")                    // Set Hindi (India) locale
                .setTimezoneId("Asia/Kolkata")         // Set India timezone (IST)
                .setIgnoreHTTPSErrors(true));          // Ignore SSL certificate errors

        Page page = context.newPage();

        logger.info("🌍 Locale: hi-IN (Hindi - India)");
        logger.info("🕐 Timezone: Asia/Kolkata (IST)");

        page.navigate("https://browserleaks.com/javascript");
        page.waitForTimeout(3000);

        String localeText = page.locator("#js-locale").textContent();
        Assert.assertTrue(localeText.contains("hi-IN"),
                "Expected Hindi locale but got: " + localeText);
        logger.info("✅ Locale detected as: " + localeText);

        page.waitForTimeout(2000);

        context.close();
        browser.close();
    }
}

