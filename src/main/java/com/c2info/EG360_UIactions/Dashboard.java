package com.c2info.EG360_UIactions;

import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.c2info.EG360_TestBase.TestBase;

public class Dashboard extends TestBase{

	
	public static final Logger log = Logger.getLogger(Dashboard.class.getName());
	WebDriverWait wait = new WebDriverWait(driver,120);
	
	@FindBy(id="mobile")
	WebElement mobileNum ;
	
	@FindBy(id="mob_check")
	WebElement GObutton ;
	
	@FindBy(id="otp")
	WebElement otp ;
	
	@FindBy(id="otp_check")
	WebElement verifyOTPbutton ;
	
	@FindBy(xpath="//div[3]/div/div[2]/div[2]/div[3]/span")
	WebElement yesterdaySale ;
	
	@FindBy(xpath="//div[4]/div/div[2]/div[2]/div[3]/span")
	WebElement yesterdayPurchase ;
	
	@FindBy(xpath="html/body/div[4]/div[2]/div[1]/div/div[2]/div/div[1]/div[2]/div")
	WebElement currentMonthPurchase ;
	
	@FindBy(xpath="html/body/div[4]/div[2]/div[1]/div/div[2]/div/div[1]/div[1]/div")
	WebElement currentMonthSales ;
	
	@FindBy(xpath="html/body/div[4]/div[2]/div[1]/div/div[2]/div/div[1]/div[3]/div")
	WebElement currentMonthGRN ;
	
	@FindBy(xpath="html/body/div[4]/div[2]/div[1]/div/div[2]/div/div[1]/div[4]/div")
	WebElement currentMonthGDN ;
	
	@FindBy(xpath="html/body/div[4]/div[2]/div[1]/div/div[2]/div/div[1]/div[5]/div")
	WebElement totalExpiry ;
	
	@FindBy(xpath="html/body/div[4]/div[2]/div[1]/div/div[2]/div/div[1]/div[6]/div")
	WebElement totalWeightedAvg ;
	
	public Dashboard(){
		PageFactory.initElements(driver, this);
	}
	
	public void waitForDashboardToLoad(){
		wait.until(ExpectedConditions.titleContains("Dashboard"));
	}

	public void login(String mobileNo, String OTP){
		mobileNum.clear();
		mobileNum.sendKeys(mobileNo);
		GObutton.click();
		wait.until(ExpectedConditions.elementToBeClickable(otp));
		otp.clear();
		otp.sendKeys(OTP);
		verifyOTPbutton.click();
	}
	
	public String getCurrentMonthSales(){
		String currMonthSales = currentMonthSales.getText();
		currMonthSales = currMonthSales.replace("L","").trim();
		return currMonthSales ;
	}
	
	public double getYesterdaySales(){
		String yestSales = yesterdaySale.getText();
		yestSales = yestSales.replaceAll("L","").trim();
		double yestSale = Double.parseDouble(yestSales);
		
		return yestSale ;
	}
	
	public double getYesterdayPurchase(){
		String yestPur = yesterdayPurchase.getText();
		yestPur = yestPur.replaceAll("L","").trim();
		double yestPurchase = Double.parseDouble(yestPur);
		
		return yestPurchase ;
	}
}
