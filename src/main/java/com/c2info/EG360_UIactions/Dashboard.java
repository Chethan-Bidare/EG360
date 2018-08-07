package com.c2info.EG360_UIactions;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.c2info.EG360_TestBase.TestBase;

public class Dashboard extends TestBase{
//git push check
	
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
	
	@FindBy(xpath="//div[3]/div/div[2]/div[1]/div[3]/span")
	WebElement todaySale ;
	
	@FindBy(xpath="//div[4]/div/div[2]/div[1]/div[3]/span")
	WebElement todayPurchase ;
	
	@FindBy(xpath="//div[5]/div/div[2]/div[1]/div[3]/span")
	WebElement todayGRN ;
	
	@FindBy(xpath="//div[6]/div/div[2]/div[1]/div[3]/span")
	WebElement todayGDN ;
	
	@FindBy(xpath="//div[3]/div/div[2]/div[2]/div[3]/span")
	WebElement yesterdaySale ;
	
	@FindBy(xpath="//div[4]/div/div[2]/div[2]/div[3]/span")
	WebElement yesterdayPurchase ;
	
	@FindBy(xpath="//div[5]/div/div[2]/div[2]/div[3]/span")
	WebElement yesterdayGRN ;
	
	@FindBy(xpath="//div[6]/div/div[2]/div[2]/div[3]/span")
	WebElement yesterdayGDN ;
	
	@FindBy(xpath="//div[3]/div/div[2]/div[3]/div[3]/span")
	WebElement weeklySale ;
	
	@FindBy(xpath="//div[4]/div/div[2]/div[3]/div[3]/span")
	WebElement weeklyPurchase ;
	
	@FindBy(xpath="//div[5]/div/div[2]/div[3]/div[3]/span")
	WebElement weeklyGRN ;
	
	@FindBy(xpath="//div[6]/div/div[2]/div[3]/div[3]/span")
	WebElement weeklyGDN ;
	
	@FindBy(xpath="//div[3]/div/div[2]/div[4]/div[3]/span")
	WebElement MonthlySales ;
	
	@FindBy(xpath="//div[4]/div/div[2]/div[4]/div[3]/span")
	WebElement MonthlyPurchase ;
	
	@FindBy(xpath="//div[5]/div/div[2]/div[4]/div[3]/span")
	WebElement MonthlyGRN ;
	
	@FindBy(xpath="//div[6]/div/div[2]/div[4]/div[3]/span")
	WebElement MonthlyGDN ;
	
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
	
	@FindBy(xpath="//div[@class='sidebar-toggler']")
	WebElement sideBar ;
	
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
	
	public void clickOnSideBar(){
		sideBar.click();
	}
	
	public void clickOnMainMenu(String menuName){	
		int size = driver.findElements(By.xpath("html/body/div[3]/div/ul/li")).size();
		for(int i=1; i<=size; i++){
			WebElement temp = driver.findElement(By.xpath("html/body/div[3]/div/ul/li["+i+"]"));
			if(temp.getText().trim().equalsIgnoreCase(menuName)){
				temp.click();
				break ;
			}
		}
	}
	
	public void clickOnReportsSubMenu(String subMenu){
		int size = driver.findElements(By.xpath("html/body/div[3]/div/ul/li[5]/ul/li")).size();
		for(int i=1; i<=size; i++){
			WebElement temp = driver.findElement(By.xpath("html/body/div[3]/div/ul/li[5]/ul/li["+i+"]/a"));
			if(temp.getText().trim().equalsIgnoreCase(subMenu)){
				temp.click();
				break ;
			}
		}
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
	
	public double getYesterdayGRN(){
		String yestGRN = yesterdayGRN.getText();
		yestGRN = yestGRN.replaceAll("L","").trim();
		double yesterGRN = Double.parseDouble(yestGRN);
		return yesterGRN ;
	}
	
	public double getYesterdayGDN(){
		String yestGDN = yesterdayGDN.getText();
		yestGDN = yestGDN.replaceAll("L","").trim();
		double yesterGDN = Double.parseDouble(yestGDN);
		return yesterGDN ;
	}
	
	public double getWeeklySales(){
		String WeeklySales = weeklySale.getText();
		WeeklySales = WeeklySales.replaceAll("L","").trim();
		double WeekSales = Double.parseDouble(WeeklySales);
		return WeekSales ;
	}
	
	public double getWeeklyPurchase(){
		String WeeklyPur = weeklyPurchase.getText();
		WeeklyPur = WeeklyPur.replaceAll("L","").trim();
		double WeekPurchase = Double.parseDouble(WeeklyPur);
		return WeekPurchase ;
	}
	
	public double getWeeklyGDN(){
		String WeeklyGDN = weeklyGDN.getText();
		WeeklyGDN = WeeklyGDN.replaceAll("L","").trim();
		double WeekGDN = Double.parseDouble(WeeklyGDN);
		return WeekGDN ;
	}
	
	public double getWeeklyGRN(){
		String WeeklyGRN = weeklyGRN.getText();
		WeeklyGRN = WeeklyGRN.replaceAll("L","").trim();
		double WeekGRN = Double.parseDouble(WeeklyGRN);
		return WeekGRN ;
	}
	
	public double getMonthlySales(){
		String monthlySales = MonthlySales.getText();
		monthlySales = monthlySales.replaceAll("L","").trim();
		double monthSales = Double.parseDouble(monthlySales);
		return monthSales ;
	}
	
	public double getMonthlyPurchase(){
		String monthlyPurchase =MonthlyPurchase.getText();
		monthlyPurchase = monthlyPurchase.replaceAll("L","").trim();
		double monthPur = Double.parseDouble(monthlyPurchase);
		return monthPur ;
	}
	
	public double getMonthlyGRN(){
		String monthlyGRN = MonthlyGRN.getText();
		monthlyGRN = monthlyGRN.replaceAll("L","").trim();
		double monthGRN = Double.parseDouble(monthlyGRN);
		return monthGRN ;
	}
	
	public double getMonthlyGDN(){
		String monthlyGDN = MonthlyGDN.getText();
		monthlyGDN = monthlyGDN.replaceAll("L","").trim();
		double monthGDN = Double.parseDouble(monthlyGDN);
		return monthGDN ;
	}
	
	public double getTodaySales(){
		String dailySales = todaySale.getText();
		dailySales = dailySales.replaceAll("L","").trim();
		double todaySales = Double.parseDouble(dailySales);
		return todaySales ;
	}
	
	public double getTodayPurchase(){
		String dailyPurchase = todayPurchase.getText();
		dailyPurchase = dailyPurchase.replaceAll("L","").trim();
		double todayPur = Double.parseDouble(dailyPurchase);
		return todayPur ;
	}
	
	public double getTodayGDN(){
		String dailyGDN = todayGDN.getText();
		dailyGDN = dailyGDN.replaceAll("L","").trim();
		double todayGDN = Double.parseDouble(dailyGDN);
		return todayGDN ;
	}
	
	public double getTodayGRN(){
		String dailyGRN = todayGRN.getText();
		dailyGRN = dailyGRN.replaceAll("L","").trim();
		double todayGRN = Double.parseDouble(dailyGRN);
		return todayGRN ;
	}
}
