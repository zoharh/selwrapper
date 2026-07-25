import java.util.concurrent.TimeUnit;
import java.time.Duration;

//Import log4j2 classes.
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

//Select the proper web driver needed for the project
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
//import org.openqa.selenium.chrome.ChromeDriver;
//import org.openqa.selenium.chrome.ChromeOptions;
//import org.openqa.selenium.phantomjs.*;

//Used for mounse and keyboard actions
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.Action;

import java.util.ArrayList;

/* 
 * 
 * @Author Zohar Hirshfeld
 * @Date August 30, 2013 
 * 
 * */

 class DriveWrapper {
    
    //log4j2 configuration file
        private static final String log4jPropFile = "main/src/resources/log4j2.xml";
        
    //Instantiate logging component
          private wLogger log = new wLogger(log4jPropFile);
    
          //WebDiriver to be used in the class
        private WebDriver driver;
        private static String siteURL = "";
        
        /*______________________________________________________________
         * 
         * constructor that would initialize logging and WebDriver.
         * Should be used only when testing login credentials
         * 
         * ______________________________________________________________*/        
        public DriveWrapper() {        
                log.logIssue(wLogger.INFO, "DriveMonitor", "_____Initializing Logger_______");

                //Instantiate logging component
                try {
                        //log4j2 configuration file
                        Configurator.initialize(null, this.log4jPropFile);
                } catch (Exception e){
                        log.logIssue(wLogger.ERROR,"DriveMonitor","Cannot start log4j2: " + e.getMessage());
                }
                //Start the Selenium driver
                try {
                        //Create Firefox options for Selenium 4.x
                        FirefoxOptions options = new FirefoxOptions();
                        options.addArguments("--start-maximized");
                        
                        //Create a new FF driver with options
                        this.driver = new FirefoxDriver(options);
                        
                        //set default timeout for driver (Selenium 4.x syntax)
                        this.driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));

                } catch (Exception e) {
                        log.logIssue(wLogger.ERROR,"DriveMonitor","Cannot create sel driver ::" + e.getMessage());
                }
        }

        /*______________________________________________________________
         * 
         * constructor that would initialize logging and WebDriver.
         * but change the default site used (meant to switch between
         * touch.face... to m.face..
         * 
         * ______________________________________________________________*/        
         public DriveWrapper(String siteurl) {
                 this();
                 this.setSiteurl(siteurl);
                 this.gotoURL(DriveWrapper.getSiteurl());
        }


        /*______________________________________________________________
         * 
         * Terminate the WebDriver session
         * 
         * ______________________________________________________________*/        
        public void close() {
                log.logIssue(wLogger.DEBUG,"close" ,"Shutting down WebDriver");
                this.driver.close();
        }
        
        /*______________________________________________________________
         * 
         * Browse to a specific URL
         * 
         * ______________________________________________________________*/        
        public void gotoURL(String mainDashboard) {
                //Log a message
                log.logIssue(wLogger.DEBUG,"gotoURL" ,"browsing to " + mainDashboard);
                //Open the report URL
                this.driver.get(mainDashboard);
        }
        
        /*______________________________________________________________
         * 
         * Return Browse Title
         * 
         * ______________________________________________________________*/        
        public String getBrowserTitle() {
                return this.driver.getTitle();
        }
        
        /*______________________________________________________________
         * 
         * Clicks on the WebElement found by the description
         * 
         * ______________________________________________________________*/        
        public void click (String desc, String t) throws Exception {
            log.logIssue(wLogger.DEBUG, "click", desc);
            this.doAction(desc, "click", t, "");
        }
        
        /*______________________________________________________________
                * 
                * Double Clicks on the WebElement found by the description
                * 
                * ______________________________________________________________*/        
        public void dblclick (String desc, String t) throws Exception {
            log.logIssue(wLogger.DEBUG, "click", desc);
            this.doAction(desc, "dblclick", t, "");
        }
        
        /*______________________________________________________________
                * 
                * Type on the WebElement found by the description
                * 
                * ______________________________________________________________*/        
        public void type (String desc, String t, String text) throws Exception {
            log.logIssue(wLogger.DEBUG, "type", desc);
            this.doAction(desc, "type", t, text);
        }

        //Returns true in case element is displayed and false in case element is either not found
        // or not displayed
        public boolean isElementVisiblie (String desc, String ElementType) {
            try {
                    WebElement we = findElement ( desc,  ElementType);
                    return we.isDisplayed();
            }catch (Exception e) {
                    log.logIssue(wLogger.WARN, "isElementVisiblie", "Element was not found");
                    return false;
            }
        }
    
        public ArrayList<Integer> getWebElementXY (String desc, String ElementType) throws Exception {
             WebElement we;
             ArrayList<Integer> intList = new ArrayList<Integer>();

             try {
                     we=findElement( desc, ElementType);
                     intList.add(we.getLocation().x);
                     intList.add(we.getLocation().y);
                     
             } catch (Exception e) {
                    log.logIssue(wLogger.ERROR, "getWebElementXY", "Could not find element " + desc + " of type " + ElementType + " was not found");
                    throw e;
            }
             return intList;
        }
    
        //Return WebElement using the description and Description Type
        private WebElement findElement (String desc, String ElementType) throws Exception {
            WebElement we;
            
            try {
                    switch (ElementType) {
                        case ("id"):
                            we = this.findElement_byID(desc);
                            break;
                        case ("name"):
                            we = this.findElement_byName(desc);
                            break;
                        case ("link"):
                            we = this.findElement_byLinkText(desc);
                            break;
                        case ("xpath"):
                            we = this.findElement_byXPath(desc);
                            break;
                        case ("tagname"):
                            we = this.findElement_byTagName(desc);
                            break;
                        case ("css"):
                            we = this.findElement_byCss(desc);
                            break;
                        default:
                            throw new IllegalArgumentException(desc);
                    }
            } catch (Exception e) {
                  log.logIssue(wLogger.ERROR, "findElement", "Could not find element " + desc + " of type " + ElementType + " was not found");
                  throw e;
                  }
            return we;
        }
    
        /*______________________________________________________________
                * 
                * Find the web element and perform the requested action against it
                * 
                * ______________________________________________________________*/        
        private void doAction (String desc, String action, String ElementType, String arg) throws Exception {
            WebElement we;
            try {
                    we=findElement( desc, ElementType);
                    
                switch (action) {
                        case ("click"):
                                    log.logIssue(wLogger.DEBUG, "doAction", "Clicking on " + we.getTagName() + ", element is enabled: " + we.isEnabled());
                                    we.click();
                                    break;
                        case ("dblclick"):
                                    log.logIssue(wLogger.DEBUG, "doAction", "Double clicking on " + we.getTagName() + ", element is enabled: " + we.isEnabled());
                                    Actions builder = new Actions(this.driver);
                                    builder.doubleClick(we).perform();
                                    break;
                        case ("type"):
                                    log.logIssue(wLogger.DEBUG, "doAction", "Typing on " + we.getTagName() + ", element is enabled: " + we.isEnabled());
                                    we.sendKeys(arg);
                                    break;
                        default:
                                throw new IllegalArgumentException(arg);
                    }
                    
            } catch (Exception e) {
                    log.logIssue(wLogger.ERROR, "doAction", "Could not find element " + desc + "!" + e.getMessage());
                    throw e;
            }
        }
    
        /*______________________________________________________________
                * 
                * Find and return a WebElemnt by its ID
                * 
                * ______________________________________________________________*/                                                                                                                                                                    
        private WebElement findElement_byID (String id) {                                                                                                                                                         
            return this.driver.findElement(By.id(id));                                                                                                                                                   
        }

        /*______________________________________________________________
                * 
                * Find and return a WebElemnt by its link text 
                * (or partial link text)
                * 
                * ______________________________________________________________*/                                                                                                                                                                   
        private WebElement findElement_byLinkText (String linkText) {                                                                                                                                                         
            return this.driver.findElement(By.partialLinkText(linkText));                                                                                                                                                   
        }

        /*______________________________________________________________
                * 
                * Find and return a WebElemnt by its Name
                * 
                * ______________________________________________________________*/        
        //Return a WebElement by finding its ID                                                                                                                                                              
        private WebElement findElement_byName (String eName) {                                                                                                                                                         
            return this.driver.findElement(By.name(eName));
        }
    
        /*______________________________________________________________
                * 
                * Find and return a WebElemnt by its tagName properties
                * 
                * ______________________________________________________________*/        
        private WebElement findElement_byTagName (String css){
                return this.driver.findElement(By.tagName(css));
         }

        /*______________________________________________________________
                * 
                * Find and return a WebElemnt by its CSS properties
                * 
                * ______________________________________________________________*/        
        private WebElement findElement_byCss (String css){
                return this.driver.findElement(By.cssSelector(css));
        }

        /*______________________________________________________________
                * 
                * Find and return a WebElemnt by its XPath properties
                * 
                * ______________________________________________________________*/        
        //Return a WebElement by finding XPath                                                                                                                                                              
        private WebElement findElement_byXPath (String xpath) {                                                                                                                                                          
                return this.driver.findElement(By.xpath(xpath) );
        }
        
        /*______________________________________________________________
         * 
         * return the default URL to run against
         * 
         * ______________________________________________________________*/        
        public static String getSiteurl() {
                return DriveWrapper.siteURL;
         }
        
        /*______________________________________________________________
         * 
         * Set the default URL to run against
         * 
         * ______________________________________________________________*/        
        private void setSiteurl(String url) {
                DriveWrapper.siteURL=url;
        }

        //Checks if an object exists. The object can be defined by the following:
        //xpath(default) - for XPath
        //id                     - using the object's id attribute
        //name                   - using the object's name attribute
        //link                   - using the object's partial or complete link text
        public boolean objectExists(String desc, String t) {
                Boolean found = false;
            try {
                    switch (t) {
                    case ("id"):
                            this.findElement_byID(desc).getTagName();
                            break;
                    case ("name"):
                            this.findElement_byName(desc).getTagName();
                            break;
                    case ("link"):
                            this.findElement_byLinkText(desc).getTagName();
                            break;
                    case ("css"):
                            this.findElement_byCss(desc).getTagName();
                            break;
                    case ("xpath"):
                    default:
                            this.findElement_byXPath(desc).getTagName();
                            break;
                    }
                found =  true;
            } catch (NoSuchElementException | StaleElementReferenceException ex) {
        found = false;
            }
            log.logIssue(wLogger.DEBUG, "objectExists", "Element " + desc + " was " +(found?"":"not") +" found");
            return found;
        }
        
        /*______________________________________________________________
         * 
         * Scroll down to end of page or until the identifier is not present
         * 
         * ______________________________________________________________*/        
        public void scrollDown(String identifier, String m, int pageSize){
                Boolean readyStateComplete = false;
                int i=0;
                int upperScrollLimit = 2100; //to prevent inifinte loop
                
                //In case the wrong pagesize was used, set to a default (24)
                if (pageSize >= 0)
                    pageSize = 24;
        
                while (!readyStateComplete) {
                        JavascriptExecutor js = (JavascriptExecutor)driver;
                        js.executeScript("window.scrollTo(0,Math.max(document.documentElement.scrollHeight," + 
                                                         "document.body.scrollHeight,document.documentElement.clientHeight));");
                        log.logIssue(wLogger.DEBUG, "scrollDown","Scrolling for the " + ++i + " times");
                        String tmp = js.executeScript("return document.readyState").toString();
                        log.logIssue(wLogger.DEBUG, "scrollDown","js exection == " + tmp);
                    readyStateComplete = tmp.contentEquals("complete");
                    //In case the object doesn't exists, break out of the loop
                    //the object might represent a spinner that shows that more
                    // records are downloaded. 
                    
                    //In case pageSize was reached, check if object exists
                    if ((i%pageSize)==0){ 
                            if (!this.objectExists(identifier, m)){
                                    readyStateComplete = true;
                            }
                        }
                    //Just in case to prevent an infinite loop.
                    if (i>upperScrollLimit) {
                            readyStateComplete=true;
                            log.logIssue(wLogger.WARN, "scrollDown", "Scrolling exited early. Completed " + i + " interation.");
                    }

                    if (!readyStateComplete) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }
        }
}


 
