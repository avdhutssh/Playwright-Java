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

    // ========================================
    // TEST 2: Download with Browser-Level Path (Auto-Save)
    // ========================================
    @Test(priority = 2)
    public void test_02_BrowserLevelDownloadPath() {
        logger.info("📌 TEST 2: Download with Browser-Level Path (Auto-Save)");

        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                .setChannel("chrome")
                .setHeadless(false)
                .setDownloadsPath(DOWNLOAD_DIR));       // Browser auto-saves downloads here

        BrowserContext context = browser.newContext(new Browser.NewContextOptions()
                .setAcceptDownloads(true));

        Page page = context.newPage();

        page.navigate("https://the-internet.herokuapp.com/download");
        page.waitForTimeout(2000);

        Download download = page.waitForDownload(() -> {
            page.locator("a[href*='.txt']").first().click();
        });

        // Get the path where browser automatically saved the file
        Path autoSavedPath = download.path();
        String originalFilename = download.suggestedFilename();

        logger.info("💾 File auto-saved to: " + autoSavedPath);
        logger.info("📄 Original filename: " + originalFilename);
        
        // Verify file exists at the browser-level download path
        Assert.assertTrue(autoSavedPath.toFile().exists(), "Download file should exist");
        Assert.assertTrue(autoSavedPath.toString().contains("temp"), 
                "File should be in temp directory");
        logger.info("✅ File size: " + autoSavedPath.toFile().length() + " bytes");

        context.close();
        browser.close();
    }

    // ========================================
    // TEST 3: Download PNG with Browser-Level Path
    // ========================================
    @Test(priority = 3)
    public void test_03_DownloadPNG_BrowserLevel() {
        logger.info("📌 TEST 3: Download PNG with Browser-Level Path");

        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                .setChannel("chrome")
                .setHeadless(false)
                .setDownloadsPath(DOWNLOAD_DIR));       // Browser auto-saves PNG here

        BrowserContext context = browser.newContext(new Browser.NewContextOptions()
                .setAcceptDownloads(true));

        Page page = context.newPage();

        page.navigate("https://the-internet.herokuapp.com/download");
        page.waitForTimeout(2000);

        Download download = page.waitForDownload(() -> {
            page.locator("a[href$='.png']").first().click();
        });

        Path autoSavedPath = download.path();
        String originalFilename = download.suggestedFilename();

        logger.info("💾 PNG auto-saved to: " + autoSavedPath);
        logger.info("📄 Filename: " + originalFilename);
        
        Assert.assertTrue(autoSavedPath.toFile().exists(), "Download file should exist");
        Assert.assertTrue(originalFilename.endsWith(".png"), "Should be a PNG file");
        logger.info("✅ File size: " + autoSavedPath.toFile().length() + " bytes");

        context.close();
        browser.close();
    }

    // ========================================
    // TEST 4: Download with saveAs (Copy Behavior)
    // ========================================
    @Test(priority = 4)
    public void test_04_DownloadWithSaveAs() {
        logger.info("📌 TEST 4: Download with saveAs (Copy Behavior)");

        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                .setChannel("chrome")
                .setHeadless(false));

        BrowserContext context = browser.newContext(new Browser.NewContextOptions()
                .setAcceptDownloads(true));

        Page page = context.newPage();

        page.navigate("https://the-internet.herokuapp.com/download");
        page.waitForTimeout(2000);

        Download download = page.waitForDownload(() -> {
            page.locator("a[href*='.txt']").first().click();
        });

        Path tempPath = download.path();
        logger.info("📥 File initially downloaded to temp: " + tempPath);

        String filename = "saved_" + System.currentTimeMillis() + ".txt";
        Path finalPath = DOWNLOAD_DIR.resolve(filename);
        download.saveAs(finalPath);

        logger.info("📦 File saved to: " + finalPath);
        logger.info("ℹ️  Note: saveAs() creates a COPY - temp file remains");
        
        Assert.assertTrue(finalPath.toFile().exists(), "File should exist at final location");
        Assert.assertTrue(tempPath.toFile().exists(), "Temp file still exists (copy, not move)");
        logger.info("✅ Final file size: " + finalPath.toFile().length() + " bytes");
        logger.info("✅ Temp file size: " + tempPath.toFile().length() + " bytes");

        logger.info("🧹 Closing context to trigger auto-cleanup...");
        context.close();
        browser.close();

        logger.info("🔍 Verifying temp file cleanup...");
        Assert.assertFalse(tempPath.toFile().exists(), "Temp file should be deleted after context closes");
        Assert.assertTrue(finalPath.toFile().exists(), "Final file should still exist");
        logger.info("✅ Temp file auto-cleaned by Playwright");
        logger.info("✅ Final file persists at: " + finalPath);
    }
}
