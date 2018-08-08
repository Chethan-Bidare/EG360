package com.c2info.EG360_TestBase;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
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
		SimpleDateFormat formatter = new SimpleDateFormat("dd_MM_YYYY_HH_mm_ss");
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
			//test.log(LogStatus.FAIL, test.addScreenCapture(getScreenshot(Thread.currentThread().getStackTrace()[1].getMethodName())));
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
		log.info("Test Started :"+result.getName());
	}
	
	@AfterMethod
	public void afterMethod(ITestResult result){
		getResult(result);
		log.info("Test Finished :"+result.getName());
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
	
	
	public LocalDate getWeekStartDate(){
		LocalDate today = LocalDate.now();
		LocalDate sundayDate= today ;
		
		while(sundayDate.getDayOfWeek() != DayOfWeek.SUNDAY){
			sundayDate = sundayDate.minusDays(1);
		}
		return sundayDate ;
	}
	
	public LocalDate getWeekEndDate(){
		LocalDate today = LocalDate.now();
		LocalDate saturdayDate= today ;
		while(saturdayDate.getDayOfWeek() != DayOfWeek.SATURDAY){
			saturdayDate = saturdayDate.plusDays(1);
		}
		return saturdayDate ;
	}
	
	public LocalDate getPreviousWeekStartDate(){
		LocalDate today = LocalDate.now();
		LocalDate sundayDate= today ;
		
		while(sundayDate.getDayOfWeek() != DayOfWeek.SUNDAY){
			sundayDate = sundayDate.minusDays(1);
		}
		LocalDate previousSundayDate = sundayDate.minusDays(7);
		return previousSundayDate ;
	}
	
	public LocalDate getPreviousWeekEndDate(){
		LocalDate today = LocalDate.now();
		LocalDate sundayDate= today ;
		
		while(sundayDate.getDayOfWeek() != DayOfWeek.SUNDAY){
			sundayDate = sundayDate.minusDays(1);
		}
		LocalDate previousSaturdayDate = sundayDate.minusDays(1);
		return previousSaturdayDate ;
	}
	
	public LocalDate getMonthStartDate(){
		LocalDate today = LocalDate.now();

		LocalDate startDate = today.withDayOfMonth(1);
		return startDate;
	}
	
	public LocalDate getMonthEndDate(){
		LocalDate today = LocalDate.now();
		LocalDate endDate = today.withDayOfMonth(today.lengthOfMonth());
		return endDate ;
	}
	
	public LocalDate getLastMonthStartDate(){
		LocalDate today = LocalDate.now();
		today = today.minusMonths(1);
		LocalDate lastMonthStartDate = today.withDayOfMonth(1);
		return lastMonthStartDate;
	}
	
	public LocalDate getLastMonthEndDate(){
		LocalDate today = LocalDate.now();
		today = today.minusMonths(1);
		LocalDate endDate = today.withDayOfMonth(today.lengthOfMonth());
		return endDate ;
	}
	
	public LocalDate getCurrentYearStartDate(){
		LocalDate today = LocalDate.now();
		LocalDate yearStartDate = today.withDayOfYear(1);
		return yearStartDate ;
		
	}
	
	public LocalDate getCurrentYearEndDate(){
		LocalDate today = LocalDate.now();
		LocalDate lastdayOfCurrentYear = today.withDayOfYear(today.lengthOfYear());
		return lastdayOfCurrentYear ;
	}
	
	
	public LocalDate getCurrentQuarterStartDate(){
		LocalDate today = LocalDate.now();
		int currentMonth = today.getMonthValue();
		if(currentMonth==1 || currentMonth==2 || currentMonth==3){
			LocalDate firstQuarterStartDate = today.withDayOfYear(1);
			return firstQuarterStartDate;
		}
		else if(currentMonth==4 || currentMonth==5 || currentMonth==6){
			LocalDate secondQuarterStartDate = today.withDayOfYear(1).plusMonths(3);
			return secondQuarterStartDate ;
		}
		
		else if(currentMonth==7 || currentMonth==8 || currentMonth==9){
			LocalDate thirdQuarterStartDate = today.withDayOfYear(1).plusMonths(6);
			return thirdQuarterStartDate ;
		}
		else if(currentMonth==10 || currentMonth==11 || currentMonth==13){
			LocalDate fourthQuarterStartDate = today.withDayOfYear(1).plusMonths(9);
			return fourthQuarterStartDate ;
		}
		return today ;
	}
	
	
	public LocalDate getCurrentQuarterEndDate(){
		LocalDate today = LocalDate.now();
		int currentMonth = today.getMonthValue();
		if(currentMonth<=3 && currentMonth>=1){
			System.out.println("1st Quarter");
			LocalDate firstQuarterEndDate = today.withDayOfYear(1).plusMonths(2);
			firstQuarterEndDate = firstQuarterEndDate.withDayOfMonth(firstQuarterEndDate.lengthOfMonth());
			return firstQuarterEndDate;
		}
		else if(currentMonth>3 && currentMonth<=6){
			LocalDate secondQuarterEndDate = today.withDayOfYear(1).plusMonths(5);
			secondQuarterEndDate = secondQuarterEndDate.withDayOfMonth(secondQuarterEndDate.lengthOfMonth());
			return secondQuarterEndDate ;
		}
		
		else if(currentMonth>6 && currentMonth<=9){
			LocalDate thirdQuarterEndDate = today.withDayOfYear(1).plusMonths(8);
			thirdQuarterEndDate = thirdQuarterEndDate.withDayOfMonth(thirdQuarterEndDate.lengthOfMonth());
			return thirdQuarterEndDate ;
		}
		else if(currentMonth>9 && currentMonth<=12){
			LocalDate fourthQuarterEndDate = today.withDayOfYear(1).plusMonths(11);
			fourthQuarterEndDate = fourthQuarterEndDate.withDayOfMonth(fourthQuarterEndDate.lengthOfMonth());
			return fourthQuarterEndDate ;
		}
		return today;
	}
	
	
}
