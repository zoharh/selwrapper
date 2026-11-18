import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

/**
 * Test suite for SelWrapper project
 * Runs all unit tests for both wLogger and DriveWrapper classes
 */
@Suite
@SelectClasses({
    wLoggerTest.class,
    DriveWrapperTest.class
})
public class SelWrapperTestSuite {
    // This class remains empty, it is used only as a holder for the above annotations
}
