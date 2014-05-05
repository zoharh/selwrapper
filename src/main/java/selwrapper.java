import java.util.concurrent.TimeUnit;

//Import log4j classes.
import org.apache.log4j.Logger;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.PropertyConfigurator;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
//import org.openqa.selenium.chrome.ChromeDriver;
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
    
    //log4j properties file
	private static final String log4jPropFile = "main/src/resources/log4j.properties";
	
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
		log.logIssue(wLogger.INFO, "DriveMonitor", "______________________________________________________________");

		//Instantiate logging component
		try {
			//log4j property file
			PropertyConfigurator.configure(this.log4jPropFile);
			BasicConfigurator.configure();
		} catch (Exception e){
			log.logIssue(wLogger.ERROR,"DriveMonitor","Cannot start log4j" + e.getMessage());
		}
		//Start the Selenium driver
		try {
			//Create a new FF driver
			this.driver = new FirefoxDriver();
			//set default timeout for driver
			this.driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			//Maximize the browser's window
			this.driver.manage().window().maximize();

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
    
    //Return the X & Y values for a WebElement
    // intList.get(0) = X value
    //intList.get(1) = Y value
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
		    		log.logIssue(wLogger.DEBUG, "doAction", "Clicking on "+we.getTagName() + ", element is enabled: " + we.isEnabled());
		    		we.click();
		    		break;
		    	case ("dblclick"):
		    		log.logIssue(wLogger.DEBUG, "doAction", "Double clicking on "+we.getTagName() + ", element is enabled: " + we.isEnabled());
		    		Actions builder = new Actions(this.driver);
		    		builder.doubleClick(we).perform();
		    		break;
			case ("type"):
		    		log.logIssue(wLogger.DEBUG, "doAction", "Typing on "+we.getTagName() + ", element is enabled: " + we.isEnabled());
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
	//id    		 - using the object's id attribute
	//name  		 - using the object's name attribute
	//link  		 - using the object's partial or complete link text
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
	public void scrollDown(String identifier, String m){
		Boolean readyStateComplete = false;
		int i=0;
		
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
		    if ((i%24)==0){ //24 is page size
			    if (!this.objectExists(identifier, m)){
			    	readyStateComplete = true;
			    }
			}
		    //Just in case to prevent an infinite loop.
		    if (i>2100) {
		    	readyStateComplete=true;
		    	log.logIssue(wLogger.WARN, "scrollDown", "Scrolling exited early. Completed " + i + " interation.");
		    }
		}
	}
}

 //Wrapper for Log4J to ensure standardized logging across all project
 class wLogger {
	private static Logger log = Logger.getLogger(wLogger.class);

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
			//log4j property file
			PropertyConfigurator.configure(log4jPropFile);
			BasicConfigurator.configure();
		} catch (Exception e){
			System.out.print("Cannot start log4j" + e.getMessage());
		}
	}
	private void findClass (String s) {
		for (int i=0;;i++) {
			System.err.println ("FIND " + s + " => " + java.lang.Thread.currentThread().getStackTrace()[i].getMethodName() + "\n");
			if (java.lang.Thread.currentThread().getStackTrace()[i].getMethodName().compareTo("<init>")==0) {
				System.out.print ("System function is :: " + java.lang.Thread.currentThread().getStackTrace()[i-1].getMethodName() + "\n");
				break;
			}
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
				log.fatal(finalMsg);
				return;
				default:
					throw new IllegalArgumentException(type);
		}
	}
 }
 
