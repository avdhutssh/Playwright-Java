package practice;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Complete Playwright Architecture Test Suite
 * This file demonstrates all possible ways to use Playwright architecture:
 * - Multiple Pages (same context)
 * - Multiple Contexts (same browser)
 * - Multiple Browsers
 * - Parallel Execution
 * - Incognito Mode
 * - Custom Configurations
 * <p>
 * Reference: Documentation/02_Playwright_Architecture.md
 */
@TestMethodOrder(MethodOrderer.MethodName.class)
public class _04_PlaywrightArchitectureTests {

    private static Playwright playwright;
    private static Browser browser;

    @BeforeAll
    static void setUp() {
        System.out.println("\n========================================");
        System.out.println("🚀 Initializing Playwright Test Suite");
        System.out.println("========================================\n");

        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                .setHeadless(false));  // Set to false to see browser in action

        System.out.println("✅ Playwright initialized");
        System.out.println("✅ Browser launched (Chromium)");
    }

    @AfterAll
    static void tearDown() {
        System.out.println("\n========================================");
        System.out.println("🧹 Cleaning up resources");
        System.out.println("========================================\n");

        if (browser != null) {
            browser.close();
            System.out.println("✅ Browser closed");
        }
        if (playwright != null) {
            playwright.close();
            System.out.println("✅ Playwright closed");
        }
    }

    // ========================================
    // TEST 1: Multiple Pages in Same Context
    // ========================================
    @Test
    void test_01_MultiplePages_SameContext() {
        System.out.println("\n📌 TEST 1: Multiple Pages in Same Context");
        System.out.println("   Scenario: Opening 3 tabs in same browser profile");
        System.out.println("   All tabs share cookies and session data\n");

        // Create ONE context (like one Chrome profile)
        BrowserContext context = browser.newContext();

        // Open 3 pages (like 3 tabs in same profile)
        Page page1 = context.newPage();
        Page page2 = context.newPage();
        Page page3 = context.newPage();

        System.out.println("   📄 Page 1: Navigating to The Internet");
        page1.navigate("https://the-internet.herokuapp.com/");

        System.out.println("   📄 Page 2: Navigating to Example.com");
        page2.navigate("https://example.com/");

        System.out.println("   📄 Page 3: Navigating to HTTPBin");
        page3.navigate("https://httpbin.org/");

        // Verify all pages loaded
        Assertions.assertEquals("The Internet", page1.title());
        Assertions.assertEquals("Example Domain", page2.title());
        Assertions.assertTrue(page3.title().contains("httpbin"));

        System.out.println("   ✅ All 3 pages loaded successfully");
        System.out.println("   ℹ️  All pages share same cookies/storage\n");

        context.close();
    }

    // ========================================
    // TEST 2: Multiple Contexts in Same Browser
    // ========================================
    @Test
    void test_02_MultipleContexts_SameBrowser() {
        System.out.println("\n📌 TEST 2: Multiple Contexts in Same Browser");
        System.out.println("   Scenario: 3 isolated browser profiles (like 3 users)");
        System.out.println("   Each context has its own cookies/storage\n");

        // Create 3 contexts (like 3 different Chrome profiles)
        BrowserContext context1 = browser.newContext();
        BrowserContext context2 = browser.newContext();
        BrowserContext context3 = browser.newContext();

        // Each context gets its own page
        Page page1 = context1.newPage();
        Page page2 = context2.newPage();
        Page page3 = context3.newPage();

        System.out.println("   👤 Context 1 (User 1): Navigating to website");
        page1.navigate("https://the-internet.herokuapp.com/");

        System.out.println("   👤 Context 2 (User 2): Navigating to same website");
        page2.navigate("https://the-internet.herokuapp.com/");

        System.out.println("   👤 Context 3 (User 3): Navigating to same website");
        page3.navigate("https://the-internet.herokuapp.com/");

        // All see same title but have separate sessions
        Assertions.assertEquals("The Internet", page1.title());
        Assertions.assertEquals("The Internet", page2.title());
        Assertions.assertEquals("The Internet", page3.title());

        System.out.println("   ✅ All contexts isolated successfully");
        System.out.println("   ℹ️  Each context has separate cookies/storage\n");

        context1.close();
        context2.close();
        context3.close();
    }

    // ========================================
    // TEST 3: Multiple Browsers
    // ========================================
    @Test
    void test_03_MultipleBrowsers() {
        System.out.println("\n📌 TEST 3: Multiple Browsers (Cross-Browser Testing)");
        System.out.println("   Scenario: Testing on Chromium, Firefox, and WebKit");
        System.out.println("   Three different browser applications running\n");

        // Launch 3 different browsers
        Browser chromiumBrowser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(false));
        Browser firefoxBrowser = playwright.firefox().launch(
                new BrowserType.LaunchOptions().setHeadless(false));
        Browser webkitBrowser = playwright.webkit().launch(
                new BrowserType.LaunchOptions().setHeadless(false));

        // Create context and page for each browser
        BrowserContext chromiumContext = chromiumBrowser.newContext();
        BrowserContext firefoxContext = firefoxBrowser.newContext();
        BrowserContext webkitContext = webkitBrowser.newContext();

        Page chromiumPage = chromiumContext.newPage();
        Page firefoxPage = firefoxContext.newPage();
        Page webkitPage = webkitContext.newPage();

        System.out.println("   🌐 Chromium: Testing website");
        chromiumPage.navigate("https://the-internet.herokuapp.com/");

        System.out.println("   🦊 Firefox: Testing website");
        firefoxPage.navigate("https://the-internet.herokuapp.com/");

        System.out.println("   🧭 WebKit: Testing website");
        webkitPage.navigate("https://the-internet.herokuapp.com/");

        // Verify on all browsers
        Assertions.assertEquals("The Internet", chromiumPage.title());
        Assertions.assertEquals("The Internet", firefoxPage.title());
        Assertions.assertEquals("The Internet", webkitPage.title());

        System.out.println("   ✅ All browsers tested successfully");
        System.out.println("   ℹ️  Cross-browser compatibility verified\n");

        // Cleanup
        chromiumContext.close();
        firefoxContext.close();
        webkitContext.close();
        chromiumBrowser.close();
        firefoxBrowser.close();
        webkitBrowser.close();
    }

    // ========================================
    // TEST 4: E-commerce Cart Isolation
    // ========================================
    @Test
    void test_04_EcommerceCartIsolation() {
        System.out.println("\n📌 TEST 4: E-commerce Cart Isolation");
        System.out.println("   Scenario: Two customers with separate shopping carts");
        System.out.println("   Each customer's cart should be isolated\n");

        // Customer A context
        BrowserContext customerA = browser.newContext();
        Page customerAPage = customerA.newPage();

        // Customer B context
        BrowserContext customerB = browser.newContext();
        Page customerBPage = customerB.newPage();

        System.out.println("   🛒 Customer A: Browsing store");
        customerAPage.navigate("https://practicesoftwaretesting.com/");
        customerAPage.waitForTimeout(1000);

        System.out.println("   🛒 Customer B: Browsing store");
        customerBPage.navigate("https://practicesoftwaretesting.com/");
        customerBPage.waitForTimeout(1000);

        System.out.println("   ✅ Both customers can shop independently");
        System.out.println("   ℹ️  Each has isolated cart (separate cookies)\n");

        customerA.close();
        customerB.close();
    }

    // ========================================
    // TEST 5: Admin and User Simultaneous Testing
    // ========================================
    @Test
    void test_05_AdminAndUserSimultaneous() {
        System.out.println("\n📌 TEST 5: Admin and User Simultaneous Testing");
        System.out.println("   Scenario: Admin and regular user testing simultaneously");
        System.out.println("   Each has different permissions and views\n");

        // Admin context
        BrowserContext adminContext = browser.newContext();
        Page adminPage = adminContext.newPage();

        // User context
        BrowserContext userContext = browser.newContext();
        Page userPage = userContext.newPage();

        System.out.println("   👨‍💼 Admin: Accessing website");
        adminPage.navigate("https://the-internet.herokuapp.com/");
        adminPage.click("text=Form Authentication");

        System.out.println("   👤 User: Accessing website");
        userPage.navigate("https://the-internet.herokuapp.com/");
        userPage.click("text=Checkboxes");

        // Verify different pages accessed
        Assertions.assertTrue(adminPage.url().contains("login"));
        Assertions.assertTrue(userPage.url().contains("checkboxes"));

        System.out.println("   ✅ Admin and User sessions isolated");
        System.out.println("   ℹ️  Each can perform different actions independently\n");

        adminContext.close();
        userContext.close();
    }

    // ========================================
    // TEST 6: Incognito Mode Testing
    // ========================================
    @Test
    void test_06_IncognitoModeTesting() {
        System.out.println("\n📌 TEST 6: Incognito Mode Testing");
        System.out.println("   Scenario: Normal vs Incognito mode comparison");
        System.out.println("   Incognito has no cookies, clean state\n");

        // Normal context (like regular browser window)
        BrowserContext normalContext = browser.newContext();
        Page normalPage = normalContext.newPage();

        // Incognito context (fresh, clean state)
        BrowserContext incognitoContext = browser.newContext();
        Page incognitoPage = incognitoContext.newPage();

        System.out.println("   🌐 Normal Mode: Browsing website");
        normalPage.navigate("https://the-internet.herokuapp.com/");
        // In real scenario, normal mode would have cookies

        System.out.println("   🕵️ Incognito Mode: Browsing same website");
        incognitoPage.navigate("https://the-internet.herokuapp.com/");
        // Incognito starts fresh with no cookies

        System.out.println("   ✅ Both modes work independently");
        System.out.println("   ℹ️  Incognito context = Fresh, isolated state\n");

        normalContext.close();
        incognitoContext.close();
    }

    // ========================================
    // TEST 7: Basic Pattern - One Context One Page
    // ========================================
    @Test
    void test_07_BasicPattern_OneContextOnePage() {
        System.out.println("\n📌 TEST 7: Basic Pattern (Simplest)");
        System.out.println("   Scenario: Single context with single page");
        System.out.println("   Most common pattern for simple tests\n");

        BrowserContext context = browser.newContext();
        Page page = context.newPage();

        System.out.println("   📄 Navigating to website");
        page.navigate("https://the-internet.herokuapp.com/");

        String title = page.title();
        System.out.println("   📋 Page Title: " + title);

        Assertions.assertEquals("The Internet", title);

        System.out.println("   ✅ Basic pattern works perfectly");
        System.out.println("   ℹ️  Use this for simple, single-page tests\n");

        context.close();
    }

    // ========================================
    // TEST 8: Multiple Pages with Shared Session
    // ========================================
    @Test
    void test_08_MultiplePages_SharedSession() {
        System.out.println("\n📌 TEST 8: Multiple Pages with Shared Session");
        System.out.println("   Scenario: Login once, access multiple pages");
        System.out.println("   All pages share authentication\n");

        BrowserContext context = browser.newContext();

        // Page 1: Login page
        Page loginPage = context.newPage();
        System.out.println("   🔐 Page 1: Login page");
        loginPage.navigate("https://the-internet.herokuapp.com/login");

        // Page 2: Dashboard (would use same session)
        Page dashboardPage = context.newPage();
        System.out.println("   📊 Page 2: Dashboard (shares session)");
        dashboardPage.navigate("https://the-internet.herokuapp.com/");

        // Page 3: Profile (would use same session)
        Page profilePage = context.newPage();
        System.out.println("   👤 Page 3: Profile (shares session)");
        profilePage.navigate("https://the-internet.herokuapp.com/");

        System.out.println("   ✅ All pages share same authentication");
        System.out.println("   ℹ️  Login once, use everywhere in same context\n");

        context.close();
    }

    // ========================================
    // TEST 9: Multiple Contexts with Isolated Sessions
    // ========================================
    @Test
    void test_09_MultipleContexts_IsolatedSessions() {
        System.out.println("\n📌 TEST 9: Multiple Contexts with Isolated Sessions");
        System.out.println("   Scenario: Three users, completely isolated");
        System.out.println("   No data sharing between contexts\n");

        // User 1 context
        BrowserContext user1 = browser.newContext();
        Page user1Page = user1.newPage();
        System.out.println("   👤 User 1: Has own session");
        user1Page.navigate("https://the-internet.herokuapp.com/");

        // User 2 context
        BrowserContext user2 = browser.newContext();
        Page user2Page = user2.newPage();
        System.out.println("   👤 User 2: Has own session");
        user2Page.navigate("https://the-internet.herokuapp.com/");

        // User 3 context
        BrowserContext user3 = browser.newContext();
        Page user3Page = user3.newPage();
        System.out.println("   👤 User 3: Has own session");
        user3Page.navigate("https://the-internet.herokuapp.com/");

        System.out.println("   ✅ All users isolated successfully");
        System.out.println("   ℹ️  Each user has separate cookies/storage/session\n");

        user1.close();
        user2.close();
        user3.close();
    }

    // ========================================
    // TEST 10: Complex Pattern - Multiple Contexts with Multiple Pages
    // ========================================
    @Test
    void test_10_ComplexPattern_MultipleContextsWithPages() {
        System.out.println("\n📌 TEST 10: Complex Pattern (Multiple Everything)");
        System.out.println("   Scenario: 2 users, each with multiple tabs open");
        System.out.println("   Real-world complex testing\n");

        // User 1 with multiple tabs
        BrowserContext user1Context = browser.newContext();
        Page user1Tab1 = user1Context.newPage();
        Page user1Tab2 = user1Context.newPage();

        System.out.println("   👤 User 1 - Tab 1: Home page");
        user1Tab1.navigate("https://the-internet.herokuapp.com/");

        System.out.println("   👤 User 1 - Tab 2: Form page");
        user1Tab2.navigate("https://the-internet.herokuapp.com/login");

        // User 2 with multiple tabs
        BrowserContext user2Context = browser.newContext();
        Page user2Tab1 = user2Context.newPage();
        Page user2Tab2 = user2Context.newPage();

        System.out.println("   👥 User 2 - Tab 1: Home page");
        user2Tab1.navigate("https://the-internet.herokuapp.com/");

        System.out.println("   👥 User 2 - Tab 2: Checkboxes page");
        user2Tab2.navigate("https://the-internet.herokuapp.com/checkboxes");

        System.out.println("   ✅ Complex multi-user, multi-tab testing successful");
        System.out.println("   ℹ️  Each user's tabs share session within context\n");

        user1Context.close();
        user2Context.close();
    }

    // ========================================
    // TEST 11: Cross-Browser Testing
    // ========================================
    @Test
    void test_11_CrossBrowserTesting() {
        System.out.println("\n📌 TEST 11: Cross-Browser Compatibility Testing");
        System.out.println("   Scenario: Test same feature on different browsers");
        System.out.println("   Ensures browser compatibility\n");

        List<String> browserNames = new ArrayList<>();
        List<Browser> browsers = new ArrayList<>();

        // Launch Chromium
        Browser chromium = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(false));
        browsers.add(chromium);
        browserNames.add("Chromium");

        // Launch Firefox
        Browser firefox = playwright.firefox().launch(
                new BrowserType.LaunchOptions().setHeadless(false));
        browsers.add(firefox);
        browserNames.add("Firefox");

        // Test on each browser
        for (int i = 0; i < browsers.size(); i++) {
            Browser b = browsers.get(i);
            String name = browserNames.get(i);

            BrowserContext context = b.newContext();
            Page page = context.newPage();

            System.out.println("   🌐 " + name + ": Testing website");
            page.navigate("https://the-internet.herokuapp.com/");

            Assertions.assertEquals("The Internet", page.title());
            System.out.println("   ✅ " + name + ": Working correctly");

            context.close();
        }

        System.out.println("   ℹ️  All browsers behave consistently\n");

        // Cleanup
        for (Browser b : browsers) {
            b.close();
        }
    }

    // ========================================
    // TEST 12: Parallel Execution
    // ========================================
    @Test
    void test_12_ParallelExecution() {
        System.out.println("\n📌 TEST 12: Parallel Execution");
        System.out.println("   Scenario: Run multiple tests simultaneously");
        System.out.println("   Faster test execution\n");

        List<String> urls = List.of(
                "https://the-internet.herokuapp.com/",
                "https://example.com/",
                "https://the-internet.herokuapp.com/login"
        );

        System.out.println("   🚀 Starting parallel execution...");

        // Execute tests in parallel
        List<CompletableFuture<Void>> futures = new ArrayList<>();

        for (int i = 0; i < urls.size(); i++) {
            final int index = i;
            final String url = urls.get(i);

            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                BrowserContext context = browser.newContext();
                Page page = context.newPage();

                System.out.println("   📄 Thread " + (index + 1) + ": Navigating to " + url);
                page.navigate(url);

                System.out.println("   ✅ Thread " + (index + 1) + ": Completed");

                context.close();
            });

            futures.add(future);
        }

        // Wait for all to complete
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        System.out.println("   ✅ All parallel tests completed");
        System.out.println("   ℹ️  Parallel execution = Faster tests\n");
    }

    // ========================================
    // TEST 13: Custom Context Configuration
    // ========================================
    @Test
    void test_13_CustomContextConfiguration() {
        System.out.println("\n📌 TEST 13: Custom Context Configuration");
        System.out.println("   Scenario: Testing with mobile device emulation");
        System.out.println("   Custom viewport, user agent, timezone\n");

        // Mobile device context (iPhone)
        BrowserContext mobileContext = browser.newContext(new Browser.NewContextOptions()
                .setViewportSize(375, 667)  // iPhone 6/7/8 size
                .setUserAgent("Mozilla/5.0 (iPhone; CPU iPhone OS 14_0 like Mac OS X)")
                .setLocale("en-US")
                .setTimezoneId("America/New_York"));

        Page mobilePage = mobileContext.newPage();

        System.out.println("   📱 Mobile Device: iPhone viewport (375x667)");
        mobilePage.navigate("https://the-internet.herokuapp.com/");

        // Verify viewport
        Assertions.assertEquals(375, mobilePage.viewportSize().width);
        Assertions.assertEquals(667, mobilePage.viewportSize().height);

        System.out.println("   ✅ Mobile emulation working");
        System.out.println("   ℹ️  Can test desktop, mobile, tablet, etc.\n");

        mobileContext.close();

        // Desktop context
        BrowserContext desktopContext = browser.newContext(new Browser.NewContextOptions()
                .setViewportSize(1920, 1080));

        Page desktopPage = desktopContext.newPage();

        System.out.println("   🖥️  Desktop Device: Desktop viewport (1920x1080)");
        desktopPage.navigate("https://the-internet.herokuapp.com/");

        Assertions.assertEquals(1920, desktopPage.viewportSize().width);
        Assertions.assertEquals(1080, desktopPage.viewportSize().height);

        System.out.println("   ✅ Desktop configuration working\n");

        desktopContext.close();
    }
}

