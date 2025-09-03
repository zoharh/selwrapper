import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Test suite for SelWrapper project
 * Runs all unit tests for both wLogger and DriveWrapper classes
 */
@RunWith(Suite.class)
@SuiteClasses({
    wLoggerTest.class,
    DriveWrapperTest.class
})
public class SelWrapperTestSuite {
    // This class remains empty, it is used only as a holder for the above annotations
}
