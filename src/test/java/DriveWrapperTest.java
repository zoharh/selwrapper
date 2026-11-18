import selwrapper.DriveWrapper;
import selwrapper.wLogger;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.mockito.MockedConstruction;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Point;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for DriveWrapper. WebDriver is a mocked ChromeDriver so the
 * locator, action, and scroll paths can run without launching a browser.
 */
public class DriveWrapperTest {

    private MockedConstruction<ChromeDriver> chromeDrivers;
    private ChromeDriver driver;
    private WebDriver.Timeouts timeouts;
    private WebElement element;

    @AfterEach
    public void tearDown() {
        if (chromeDrivers != null) {
            chromeDrivers.close();
            chromeDrivers = null;
        }
    }

    private DriveWrapper newWrapper() {
        timeouts = mock(WebDriver.Timeouts.class);
        WebDriver.Options options = mock(WebDriver.Options.class);
        when(options.timeouts()).thenReturn(timeouts);
        chromeDrivers = mockConstruction(ChromeDriver.class, (mock, context) -> {
            when(mock.manage()).thenReturn(options);
        });
        DriveWrapper created = new DriveWrapper();
        driver = chromeDrivers.constructed().get(0);
        element = mock(WebElement.class);
        when(element.isDisplayed()).thenReturn(true);
        when(element.isEnabled()).thenReturn(true);
        when(element.getTagName()).thenReturn("div");
        when(element.getLocation()).thenReturn(new Point(12, 34));
        when(driver.findElement(any(By.class))).thenReturn(element);
        when(driver.getTitle()).thenReturn("Example Domain");
        when(driver.executeScript(anyString())).thenReturn("complete");
        return created;
    }
    
    @Test
    public void testStaticMethods() {
        // Test static methods that don't require WebDriver
        String siteURL = DriveWrapper.getSiteurl();
        assertNotNull(siteURL, "Site URL should not be null");
    }
    
    @Test
    public void testConstants() {
        // Test that wLogger constants are accessible
        assertNotNull(wLogger.INFO, "wLogger.INFO should not be null");
        assertNotNull(wLogger.DEBUG, "wLogger.DEBUG should not be null");
        assertNotNull(wLogger.ERROR, "wLogger.ERROR should not be null");
        assertNotNull(wLogger.WARN, "wLogger.WARN should not be null");
        assertNotNull(wLogger.FATAL, "wLogger.FATAL should not be null");
        
        assertEquals("info", wLogger.INFO, "INFO level should be 'info'");
        assertEquals("debug", wLogger.DEBUG, "DEBUG level should be 'debug'");
        assertEquals("error", wLogger.ERROR, "ERROR level should be 'error'");
        assertEquals("warn", wLogger.WARN, "WARN level should be 'warn'");
        assertEquals("fatal", wLogger.FATAL, "FATAL level should be 'fatal'");
    }
    
    @Test
    public void testWLoggerCreation() {
        // Test wLogger creation
        try {
            wLogger logger = new wLogger("test-log4j2.xml");
            assertNotNull(logger, "wLogger should be created successfully");
        } catch (Exception e) {
            fail("wLogger creation should not throw exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testWLoggerLogging() {
        // Test wLogger logging functionality
        try {
            wLogger logger = new wLogger("test-log4j2.xml");
            
            // Test all log levels
            logger.logIssue(wLogger.INFO, "testFunction", "test message");
            logger.logIssue(wLogger.DEBUG, "testFunction", "test message");
            logger.logIssue(wLogger.WARN, "testFunction", "test message");
            logger.logIssue(wLogger.ERROR, "testFunction", "test message");
            logger.logIssue(wLogger.FATAL, "testFunction", "test message");
            
            // Should not throw exception
            assertTrue(true, "All logging operations should complete without exception");
        } catch (Exception e) {
            fail("wLogger logging should not throw exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testWLoggerInvalidLevel() {
        // Test that invalid log level throws IllegalArgumentException
        wLogger logger = new wLogger("test-log4j2.xml");
        assertThrows(IllegalArgumentException.class,
                () -> logger.logIssue("invalid", "testFunction", "test message"));
    }
    
    @Test
    public void testWLoggerWithNullValues() {
        // Test logging with null values
        try {
            wLogger logger = new wLogger("test-log4j2.xml");
            
            logger.logIssue(wLogger.INFO, null, "test message");
            logger.logIssue(wLogger.INFO, "testFunction", null);
            logger.logIssue(wLogger.INFO, null, null);
            
            // Should not throw exception
            assertTrue(true, "Logging with null values should complete without exception");
        } catch (Exception e) {
            fail("Logging with null values should not throw exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testWLoggerWithEmptyStrings() {
        // Test logging with empty strings
        try {
            wLogger logger = new wLogger("test-log4j2.xml");
            
            logger.logIssue(wLogger.INFO, "", "");
            logger.logIssue(wLogger.DEBUG, "", "test message");
            logger.logIssue(wLogger.WARN, "testFunction", "");
            
            // Should not throw exception
            assertTrue(true, "Logging with empty strings should complete without exception");
        } catch (Exception e) {
            fail("Logging with empty strings should not throw exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testMultipleWLoggerInstances() {
        // Test creating multiple wLogger instances
        try {
            wLogger logger1 = new wLogger("test-log4j2.xml");
            wLogger logger2 = new wLogger("test-log4j2.xml");
            
            assertNotNull(logger1, "First logger should be created successfully");
            assertNotNull(logger2, "Second logger should be created successfully");
            assertNotSame(logger1, logger2, "Loggers should be different instances");
            
            // Test logging with both instances
            logger1.logIssue(wLogger.INFO, "function1", "message1");
            logger2.logIssue(wLogger.INFO, "function2", "message2");
            
            // Should not throw exception
            assertTrue(true, "Multiple logger instances should work correctly");
        } catch (Exception e) {
            fail("Multiple logger instances should not throw exception: " + e.getMessage());
        }
    }

    @Test
    public void testConstructorAppliesImplicitWait() {
        DriveWrapper wrapper = newWrapper();
        assertNotNull(wrapper);
        verify(timeouts).implicitlyWait(Duration.ofSeconds(30));
    }

    @Test
    public void testChromeStartupFailureIsLogged() {
        WebDriver.Options options = mock(WebDriver.Options.class);
        when(options.timeouts()).thenThrow(new IllegalStateException("no timeouts"));
        try (MockedConstruction<ChromeDriver> mocked = mockConstruction(ChromeDriver.class, (mock, context) -> {
            when(mock.manage()).thenReturn(options);
        })) {
            DriveWrapper created = new DriveWrapper();
            assertNotNull(created);
            created.close();
            verify(mocked.constructed().get(0)).close();
        }
    }

    @Test
    public void testSiteUrlConstructorOpensThatUrl() {
        WebDriver.Options options = mock(WebDriver.Options.class);
        WebDriver.Timeouts localTimeouts = mock(WebDriver.Timeouts.class);
        when(options.timeouts()).thenReturn(localTimeouts);
        try (MockedConstruction<ChromeDriver> mocked = mockConstruction(ChromeDriver.class, (mock, context) -> {
            when(mock.manage()).thenReturn(options);
        })) {
            new DriveWrapper("https://example.com");
            assertEquals("https://example.com", DriveWrapper.getSiteurl());
            verify(mocked.constructed().get(0)).get("https://example.com");
        }
    }

    @Test
    public void testNavigationTitleAndClose() {
        DriveWrapper wrapper = newWrapper();
        wrapper.gotoURL("https://example.com/home");
        assertEquals("Example Domain", wrapper.getBrowserTitle());
        wrapper.close();
        verify(driver).get("https://example.com/home");
        verify(driver).close();
    }

    @Test
    public void testActionsForEveryLocator() throws Exception {
        DriveWrapper wrapper = newWrapper();
        wrapper.click("user", "id");
        wrapper.click("user", "name");
        wrapper.click("user", "link");
        wrapper.click("user", "xpath");
        wrapper.click("user", "tagname");
        wrapper.click("user", "css");
        wrapper.dblclick("user", "id");
        wrapper.type("user", "name", "hello");

        verify(driver, atLeastOnce()).findElement(By.id("user"));
        verify(driver, atLeastOnce()).findElement(By.name("user"));
        verify(driver).findElement(By.partialLinkText("user"));
        verify(driver).findElement(By.xpath("user"));
        verify(driver).findElement(By.tagName("user"));
        verify(driver).findElement(By.cssSelector("user"));
        verify(element, atLeastOnce()).click();
        verify(element).sendKeys("hello");
    }

    @Test
    public void testUnknownElementTypeAndAction() throws Exception {
        DriveWrapper wrapper = newWrapper();
        try {
            wrapper.click("user", "nope");
            fail("Unknown element type should be rejected");
        } catch (IllegalArgumentException expected) {
            assertEquals("user", expected.getMessage());
        }

        Method doAction = DriveWrapper.class.getDeclaredMethod(
                "doAction", String.class, String.class, String.class, String.class);
        doAction.setAccessible(true);
        try {
            doAction.invoke(wrapper, "user", "drag", "id", "unused");
            fail("Unknown action should be rejected");
        } catch (InvocationTargetException expected) {
            assertTrue(expected.getCause() instanceof IllegalArgumentException);
            assertEquals("unused", expected.getCause().getMessage());
        }
    }

    @Test
    public void testVisibilityAndCoordinates() throws Exception {
        DriveWrapper wrapper = newWrapper();
        assertTrue(wrapper.isElementVisiblie("box", "css"));

        when(element.isDisplayed()).thenReturn(false);
        assertFalse(wrapper.isElementVisiblie("box", "css"));

        when(driver.findElement(eq(By.cssSelector("missing")))).thenThrow(new NoSuchElementException("missing"));
        assertFalse(wrapper.isElementVisiblie("missing", "css"));

        when(element.isDisplayed()).thenReturn(true);
        ArrayList<Integer> xy = wrapper.getWebElementXY("box", "xpath");
        assertEquals(Integer.valueOf(12), xy.get(0));
        assertEquals(Integer.valueOf(34), xy.get(1));

        when(driver.findElement(eq(By.xpath("missing")))).thenThrow(new NoSuchElementException("missing"));
        try {
            wrapper.getWebElementXY("missing", "xpath");
            fail("Missing element should be reported");
        } catch (NoSuchElementException expected) {
            assertTrue(expected.getMessage().contains("missing"));
        }
    }

    @Test
    public void testObjectExistsForEachLocator() {
        DriveWrapper wrapper = newWrapper();
        assertTrue(wrapper.objectExists("user", "id"));
        assertTrue(wrapper.objectExists("user", "name"));
        assertTrue(wrapper.objectExists("user", "link"));
        assertTrue(wrapper.objectExists("user", "css"));
        assertTrue(wrapper.objectExists("user", "xpath"));
        assertTrue(wrapper.objectExists("user", "other"));

        when(driver.findElement(eq(By.id("gone")))).thenThrow(new NoSuchElementException("gone"));
        assertFalse(wrapper.objectExists("gone", "id"));

        when(driver.findElement(eq(By.name("stale")))).thenThrow(new StaleElementReferenceException("stale"));
        assertFalse(wrapper.objectExists("stale", "name"));
    }

    @Test
    public void testScrollDownReadyAndMissingIdentifier() {
        DriveWrapper wrapper = newWrapper();
        when(driver.findElement(eq(By.id("spinner")))).thenThrow(new NoSuchElementException("gone"));

        wrapper.scrollDown("spinner", "id", -1);
        wrapper.scrollDown("spinner", "id", 24);

        verify(driver, atLeastOnce()).executeScript(anyString());
    }
}
