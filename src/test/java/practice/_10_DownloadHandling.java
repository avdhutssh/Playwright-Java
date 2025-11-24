package practice;

import com.microsoft.playwright.*;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

public class _10_DownloadHandling {
    private static final Logger logger = Logger.getLogger(_10_DownloadHandling.class.getName());
    private static Playwright playwright;
    private static final Path DOWNLOAD_DIR = Paths.get("src", "test", "resources", "TestData", "temp");

    @BeforeSuite
    public void setup() {
        playwright = Playwright.create();
        
        if (!DOWNLOAD_DIR.toFile().exists()) {
            DOWNLOAD_DIR.toFile().mkdirs();
            logger.info("📁 Created download directory: " + DOWNLOAD_DIR);
        }
    }

    @AfterSuite
    public void teardown() {
        if (playwright != null) {
            playwright.close();
        }
    }

    // ========================================
    // TEST 1: Download with Context-Level Path
    // ========================================
    @Test(priority = 1)
    public void test_01_ContextLevelDownloadPath() {
        logger.info("📌 TEST 1: Download with Context-Level Path");

        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                .setChannel("chrome")
                .setHeadless(false));

        BrowserContext context = browser.newContext(new Browser.NewContextOptions()
                .setAcceptDownloads(true));              // Enable downloads at context level

        Page page = context.newPage();

        page.navigate("https://the-internet.herokuapp.com/download");
        page.waitForTimeout(2000);

        Download download = page.waitForDownload(() -> {
            page.locator("a[href*='.txt']").first().click();
        });

        String filename = "context_" + System.currentTimeMillis() + ".txt";
        Path downloadPath = DOWNLOAD_DIR.resolve(filename);
        download.saveAs(downloadPath);

        logger.info("💾 File downloaded to: " + downloadPath);
        Assert.assertTrue(downloadPath.toFile().exists(), "Download file should exist");
        logger.info("✅ File size: " + downloadPath.toFile().length() + " bytes");

        context.close();
        browser.close();
    }
}
