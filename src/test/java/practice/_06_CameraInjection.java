package practice;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.logging.Logger;

/**
 * Prerequisites:
 * - Video file must be in Y4M or MJPEG format (NOT MP4!)
 * - Convert using FFmpeg: ffmpeg -i video.mp4 -pix_fmt yuv420p output.y4m
 */
public class _06_CameraInjection {
    private static final Logger logger = Logger.getLogger(_06_CameraInjection.class.getName());
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
    // TEST 1: Camera Capture with Actual Camera
    // ========================================
    @Test(priority = 1)
    public void test_01_CameraWithoutFile() {
        logger.info("📌 TEST 1: Camera Capture with Actual Camera");

        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                .setHeadless(false));  // Show browser UI

        // Note: Permissions are set at context level (not browser level)
        // This allows different contexts to have different permissions
        BrowserContext context = browser.newContext(new Browser.NewContextOptions()
                .setPermissions(Arrays.asList("camera", "microphone")));

        Page page = context.newPage();

        page.navigate("https://webcamtoy.com/");
        page.waitForTimeout(2000);

        page.locator("#button-init").click();

        page.waitForTimeout(5000);

        page.locator("#button-capture").click();
        page.waitForTimeout(2000);

        page.locator("#button-save").waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(5000));

        page.waitForTimeout(2000);

        Path tempDir = Paths.get("src/test/resources/TestData/temp");
        if (!tempDir.toFile().exists()) {
            tempDir.toFile().mkdirs();
            logger.info("📁 Created temp directory: " + tempDir);
        }

        String timestamp = String.valueOf(System.currentTimeMillis());
        String fileName = "selfie_" + timestamp + ".png";
        Path downloadPath = tempDir.resolve(fileName);

        Download download = page.waitForDownload(() -> {
            page.locator("#button-save").click();
        });

        download.saveAs(downloadPath);
        logger.info("💾 Photo saved to: " + downloadPath);

        if (downloadPath.toFile().exists() && downloadPath.toFile().length() > 0) {
            logger.info("✅ Photo saved successfully!");
            logger.info("📊 File size: " + downloadPath.toFile().length() + " bytes");
        } else {
            logger.warning("⚠️ Photo file not found or empty!");
        }

        page.waitForTimeout(2000);

        context.close();
        browser.close();
    }

}
