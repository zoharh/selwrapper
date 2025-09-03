import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

//Wrapper for Log4J to ensure standardized logging across all project
public class wLogger {
    private static Logger log = LogManager.getLogger(wLogger.class);

    //Log levels
    public static final String INFO  = "info";
    public static final String DEBUG = "debug";
    public static final String ERROR = "error";
    public static final String WARN  = "warn";
    public static final String FATAL = "fatal";

    //Constructor 
    public wLogger (String log4jPropFile) {
        //Instantiate logging component
        try {
            //log4j2 configuration file
            Configurator.initialize(null, log4jPropFile);
        } catch (Exception e) {
            System.out.print("Cannot start log4j2: " + e.getMessage());
        }
    }
    
    private void findClass (String s) {
        try {
            StackTraceElement[] stackTrace = java.lang.Thread.currentThread().getStackTrace();
            for (int i = 0; i < stackTrace.length && i < 10; i++) { // Limit to first 10 elements to prevent infinite loop
                System.err.println ("FIND " + s + " => " + stackTrace[i].getMethodName() + "\n");
                if (stackTrace[i].getMethodName().compareTo("<init>")==0) {
                    if (i > 0) {
                        System.out.print ("System function is :: " + stackTrace[i-1].getMethodName() + "\n");
                    }
                    break;
                }
            }
        } catch (Exception e) {
            // Silently ignore any stack trace errors
            System.err.println("Error in findClass: " + e.getMessage());
        }
    }
    
    /*______________________________________________________________
     * 
     * In order to ensure standard logging I created a wrapper 
     * around the log4j function. It will also allow moving away 
     * from log4j anytime w/o any trouble, 
     * 
     * ______________________________________________________________
    */        
    public void logIssue (String type, String func, String msg) throws IllegalArgumentException {
        
        String sep = " :: ";
        String finalMsg = func + sep + msg;
        findClass(func);
        switch (type) {
            case "debug":
                log.debug(finalMsg);
                return;
            case "info":
                log.info(finalMsg);
                return;
            case "warn":
                log.warn(finalMsg);
                return;
            case "error":
                log.error(finalMsg);
                return;
            case "fatal":
                log.error(finalMsg); // Log4j 2.x doesn't have fatal level, using error instead
                return;
            default:
                throw new IllegalArgumentException(type);
        }
    }
}
