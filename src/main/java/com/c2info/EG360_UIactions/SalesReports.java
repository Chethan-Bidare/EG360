package com.c2info.EG360_UIactions;

import java.util.HashMap;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

import com.c2info.EG360_TestBase.TestBase;

public class SalesReports extends TestBase{

	public static final Logger log = Logger.getLogger(SalesReports.class.getName());
	
	@FindBy(xpath="//div[@class='sidebar-toggler']")
	WebElement sideBar ;
	
	@FindBy(id="period")
	WebElement period ;
	
	@FindBy(id="brlevel")
	WebElement branchLevel ;
	
	@FindBy(id="types")
	WebElement type ;
	
	@FindBy(id="datetype")
	WebElement date ;
	
	@FindBy(id="viewreport")
	WebElement viewreportButton ;
	
	public SalesReports(){
		PageFactory.initElements(driver, this);
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
	
	public void clickOnSavingsReportsSubMenu(String subMenu){
		int size = driver.findElements(By.xpath("html/body/div[3]/div/ul/li[6]/ul/li")).size();
		for(int i=1; i<=size; i++){
			WebElement temp = driver.findElement(By.xpath("html/body/div[3]/div/ul/li[6]/ul/li["+i+"]/a"));
			if(temp.getText().trim().equalsIgnoreCase(subMenu)){
				temp.click();
				break ;
			}
		}
	}
	
	public void selectPeriodDropdown(String periodType){
		Select periodDropdown = new Select(period);
		periodDropdown.selectByVisibleText(periodType);
	}
	
	public void selectBranchLevelDropdown(String branchLevel){
		Select brLevelDropdown = new Select(this.branchLevel);
		brLevelDropdown.deselectByVisibleText(branchLevel);
	}
	
	public void selectTypeDropdown(String type){
		Select typeDropdown = new Select(this.type);
		typeDropdown.selectByVisibleText(type);
	}
	
	public void selectDateDropdown(String date){
		Select dateDropdown = new Select(this.date);
		dateDropdown.selectByVisibleText(date);
	}
	
	public void clickOnViewReportButton(){
		viewreportButton.click();
	}
	
	public HashMap<String,HashMap<String,String>> getBranchWiseSalesDetails(){
		
		HashMap<String, HashMap<String,String>> branchWiseSalesDetails = new HashMap<String, HashMap<String,String>>();
		int rowSize =3 ; 
				//driver.findElements(By.xpath(".//*[@id='example']/tbody/tr")).size();
		for(int i=1; i<=rowSize; i++){
			String brCode = driver.findElement(By.xpath(".//*[@id='example']/tbody/tr["+i+"]/td[1]")).getText();
			int colSize = driver.findElements(By.xpath(".//*[@id='example']/tbody/tr[1]/td")).size();
			HashMap<String, String> tempDetails = new HashMap<String, String>();
			for(int j=2; j<=colSize; j++){
				String colName =driver.findElement(By.xpath(".//*[@id='trid']/th["+j+"]")).getText(); 
				String cellValue =driver.findElement(By.xpath(".//*[@id='example']/tbody/tr["+i+"]/td["+j+"]")).getText(); 
				tempDetails.put(colName, cellValue);
			}
			branchWiseSalesDetails.put(brCode, tempDetails);
		}
		return branchWiseSalesDetails ;
	}
	
}
