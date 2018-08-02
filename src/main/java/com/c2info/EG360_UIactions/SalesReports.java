package com.c2info.EG360_UIactions;

import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.c2info.EG360_TestBase.TestBase;

public class SalesReports extends TestBase{

	public static final Logger log = Logger.getLogger(SalesReports.class.getName());
	
	@FindBy(xpath="//div[@class='sidebar-toggler']")
	WebElement sideBar ;
	
	public SalesReports(){
		PageFactory.initElements(driver, this);
	}
	
	public void clickOnSideBar(){
		sideBar.click();
	}
	
	public void clickOnMainMenu(String menuName){
		List<WebElement> menus = driver.findElements(By.tagName("li"));
		for(WebElement we : menus){
			if(we.getText().equalsIgnoreCase(menuName)){
				we.click();
			}
		}
	}
	
	public void clickOnSubMenu(String subMenu){
		List<WebElement> subMenus = driver.findElements(By.tagName("li"));
		for(WebElement we : subMenus){
			if(we.getText().equalsIgnoreCase(subMenu)){
				we.click();
			}
		}
	}
	
	public HashMap<String,HashMap<String,String>> getBranchWiseSalesDetails(){
		
		HashMap<String, HashMap<String,String>> branchWiseSalesDetails = new HashMap<String, HashMap<String,String>>();
		int rowSize = driver.findElements(By.xpath(".//*[@id='example']/tbody/tr")).size();
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
