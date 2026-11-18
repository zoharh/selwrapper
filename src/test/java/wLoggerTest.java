import selwrapper.wLogger;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * Unit tests for wLogger class
 * Tests logging functionality, constructor, and all log levels
 */
public class wLoggerTest {
    
    private wLogger logger;
    private ByteArrayOutputStream outContent;
    private ByteArrayOutputStream errContent;
    private PrintStream originalOut;
    private PrintStream originalErr;
    
    @BeforeEach
    public void setUp() {
        // Capture System.out and System.err for testing
        outContent = new ByteArrayOutputStream();
        errContent = new ByteArrayOutputStream();
        originalOut = System.out;
        originalErr = System.err;
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
        
        // Create logger instance
        logger = new wLogger("test-log4j2.xml");
    }
    
    @AfterEach
    public void tearDown() {
        // Restore original System.out and System.err
        System.setOut(originalOut);
        System.setErr(originalErr);
    }
    
    @Test
    public void testConstructor() {
        // Test that constructor doesn't throw exception
        assertNotNull(logger, "Logger should be created successfully");
    }
    
    @Test
    public void testLogLevelsConstants() {
        // Test that all log level constants are defined correctly
        assertEquals("info", wLogger.INFO, "INFO level should be 'info'");
        assertEquals("debug", wLogger.DEBUG, "DEBUG level should be 'debug'");
        assertEquals("error", wLogger.ERROR, "ERROR level should be 'error'");
        assertEquals("warn", wLogger.WARN, "WARN level should be 'warn'");
        assertEquals("fatal", wLogger.FATAL, "FATAL level should be 'fatal'");
    }
    
    @Test
    public void testLogIssueInfo() {
        // Test INFO level logging
        try {
            logger.logIssue(wLogger.INFO, "testFunction", "test message");
            // Should not throw exception
            assertTrue(true, "INFO logging should complete without exception");
        } catch (Exception e) {
            fail("INFO logging should not throw exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testLogIssueDebug() {
        // Test DEBUG level logging
        try {
            logger.logIssue(wLogger.DEBUG, "testFunction", "test message");
            // Should not throw exception
            assertTrue(true, "DEBUG logging should complete without exception");
        } catch (Exception e) {
            fail("DEBUG logging should not throw exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testLogIssueWarn() {
        // Test WARN level logging
        try {
            logger.logIssue(wLogger.WARN, "testFunction", "test message");
            // Should not throw exception
            assertTrue(true, "WARN logging should complete without exception");
        } catch (Exception e) {
            fail("WARN logging should not throw exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testLogIssueError() {
        // Test ERROR level logging
        try {
            logger.logIssue(wLogger.ERROR, "testFunction", "test message");
            // Should not throw exception
            assertTrue(true, "ERROR logging should complete without exception");
        } catch (Exception e) {
            fail("ERROR logging should not throw exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testLogIssueFatal() {
        // Test FATAL level logging (maps to ERROR in Log4j 2.x)
        try {
            logger.logIssue(wLogger.FATAL, "testFunction", "test message");
            // Should not throw exception
            assertTrue(true, "FATAL logging should complete without exception");
        } catch (Exception e) {
            fail("FATAL logging should not throw exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testLogIssueInvalidLevel() {
        // Test that invalid log level throws IllegalArgumentException
        assertThrows(IllegalArgumentException.class,
                () -> logger.logIssue("invalid", "testFunction", "test message"));
    }
    
    @Test
    public void testLogIssueWithNullFunction() {
        // Test logging with null function name
        try {
            logger.logIssue(wLogger.INFO, null, "test message");
            // Should not throw exception
            assertTrue(true, "Logging with null function should complete without exception");
        } catch (Exception e) {
            fail("Logging with null function should not throw exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testLogIssueWithNullMessage() {
        // Test logging with null message
        try {
            logger.logIssue(wLogger.INFO, "testFunction", null);
            // Should not throw exception
            assertTrue(true, "Logging with null message should complete without exception");
        } catch (Exception e) {
            fail("Logging with null message should not throw exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testLogIssueWithEmptyStrings() {
        // Test logging with empty strings
        try {
            logger.logIssue(wLogger.INFO, "", "");
            // Should not throw exception
            assertTrue(true, "Logging with empty strings should complete without exception");
        } catch (Exception e) {
            fail("Logging with empty strings should not throw exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testFindClassMethod() {
        // Test that findClass method executes without exception
        // This is a private method, but we can test it indirectly through logIssue
        try {
            logger.logIssue(wLogger.DEBUG, "testFunction", "test message");
            // Should not throw exception
            assertTrue(true, "findClass execution should not throw exception");
        } catch (Exception e) {
            fail("findClass execution should not throw exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testMultipleLogCalls() {
        // Test multiple consecutive log calls
        try {
            logger.logIssue(wLogger.INFO, "function1", "message1");
            logger.logIssue(wLogger.DEBUG, "function2", "message2");
            logger.logIssue(wLogger.WARN, "function3", "message3");
            logger.logIssue(wLogger.ERROR, "function4", "message4");
            logger.logIssue(wLogger.FATAL, "function5", "message5");
            
            // All should complete without exception
            assertTrue(true, "Multiple log calls should complete without exception");
        } catch (Exception e) {
            fail("Multiple log calls should not throw exception: " + e.getMessage());
        }
    }
}
