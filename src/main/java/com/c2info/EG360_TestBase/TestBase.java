package com.c2info.EG360_TestBase;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;
import java.util.concurrent.TimeUnit;



import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.io.FileHandler;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;

import org.testng.annotations.BeforeMethod;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class TestBase {
	
	public static final Logger log = Logger.getLogger(TestBase.class.getName());

	public static WebDriver driver ;
	public static ITestResult result ;
	public static ExtentTest test ;
	public static ExtentReports extent ;
	
	public Properties OR = new Properties();
	
	static{
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("DD_MM_YYYY_HH_MM_SS");
		extent = new ExtentReports(System.getProperty("user.dir")+"//src//main//java//com//c2info//EG360_Reports//"+formatter.format(calendar.getTime())+".html",false);
	}
	
	public void loadFromORproperties() throws IOException{
		File path = new File(System.getProperty("user.dir")+"//src//main//java//com//c2info//EG360_Config//OR.properties");
		FileInputStream fis = new FileInputStream(path);
		OR.load(fis);
		log.info("OR property file loaded successfully");
	}
	
	
	public void selectBrowser(String BrowserName){
		if(BrowserName.equalsIgnoreCase("firefox")){
			System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir")+"//Drivers//geckodriver.exe");
			driver = new FirefoxDriver();
			waitForElementToLoad();
			log.info(BrowserName+" browser launched successfully");
		
		}
		else if(BrowserName.equalsIgnoreCase("chrome")){
			System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")+"//Drivers//chromedriver.exe");
			driver = new ChromeDriver();
			waitForElementToLoad();
			log.info(BrowserName+" browser launched successfully");
		}
		else if(BrowserName.equalsIgnoreCase("htmlunit")){
			//driver = new HtmlUnitDriver() ;
			waitForElementToLoad();
			log.info(BrowserName+" browser launched successfully");
		}
	}
	
	public void waitForElementToLoad(){
		driver.manage().timeouts().implicitlyWait(60,TimeUnit.SECONDS);
	}
	
	public void init() throws IOException{
		loadFromORproperties();
		selectBrowser(OR.getProperty("BrowserName"));
		waitForElementToLoad();
		clearHistory();
		getBaseUrl(OR.getProperty("BaseURL"));
		PropertyConfigurator.configure(System.getProperty("user.dir")+"//log4j.properties");
		log.info("========== Initialiazation Completed =================");
	}
	
	public void getBaseUrl(String BaseUrl){
		driver.get(BaseUrl);
		try {
			driver.manage().window().maximize();
			log.info("URL loaded and window maximized");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void waitforPageToLoad(){
		driver.manage().timeouts().pageLoadTimeout(60,TimeUnit.SECONDS);
	}
	
	public void clearHistory(){
		driver.manage().deleteAllCookies();
	}
	
public String getScreenshot(String methodName){
		
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("DD_MM_YYYY_HH_MM_SS");
		
		try {
			File srcFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			String ReportDirectory = System.getProperty("user.dir")+"//src//main//java//com//c2info//RMS_Screenshots//";
			String destination = ReportDirectory+"_"+methodName+"_"+formatter.format(calendar.getTime())+".png";
			File destFile = new File(destination);
			FileHandler.copy(srcFile, destFile);
			
			return destination ;
		} catch (WebDriverException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null ;
	}
	
	public void getResult(ITestResult result){
		if(result.getStatus()==ITestResult.SUCCESS){
			test.log(LogStatus.PASS, result.getName()+" Test is Passed");
		}
		else if(result.getStatus()==ITestResult.FAILURE){
			test.log(LogStatus.FAIL, result.getName()+" Test is Failed");
			test.addScreenCapture(getScreenshot(Thread.currentThread().getStackTrace()[1].getMethodName()));
		}
		else if(result.getStatus()==ITestResult.SKIP){
			test.log(LogStatus.SKIP, result.getName()+ "Test is skipped");
		}
		else if(result.getStatus()==ITestResult.STARTED){
			test.log(LogStatus.INFO, result.getName()+ " Test is Started");
		}
	}
	
	
	@BeforeMethod
	public void beforeMethod(Method result){
		test = extent.startTest(result.getName());
		test.log(LogStatus.INFO, result.getName()+" Test is Started");
	}
	
	@AfterMethod
	public void afterMethod(ITestResult result){
		
	}
	
	@AfterClass(alwaysRun=true)
	public void endTest(){
		try {
			driver.close();
			extent.endTest(test);
			extent.flush();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
