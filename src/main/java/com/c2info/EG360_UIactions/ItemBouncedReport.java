package com.c2info.EG360_UIactions;

import java.util.HashMap;

import org.openqa.selenium.By;

import com.c2info.EG360_TestBase.TestBase;

public class ItemBouncedReport extends TestBase{

	public HashMap<String,HashMap<String,String>> getBranchWiseDetails(){
		
		HashMap<String, HashMap<String,String>> branchWiseSalesDetails = new HashMap<String, HashMap<String,String>>();
		int rowSize = 3 ; 
				//driver.findElements(By.xpath(".//*[@id='example']/tbody/tr")).size();
		for(int i=1; i<=rowSize; i++){
			String itemCode = driver.findElement(By.xpath(".//*[@id='example']/tbody/tr["+i+"]/td[5]")).getText();
			int colSize = driver.findElements(By.xpath(".//*[@id='example']/tbody/tr[1]/td")).size();
			HashMap<String, String> tempDetails = new HashMap<String, String>();
			for(int j=1; j<=colSize; j++){
				if(j != 5){
				String colName =driver.findElement(By.xpath(".//*[@id='trid']/th["+j+"]")).getText(); 
				String cellValue =driver.findElement(By.xpath(".//*[@id='example']/tbody/tr["+i+"]/td["+j+"]")).getText(); 
				tempDetails.put(colName, cellValue);
				}
				else{
					System.out.println("data already captured");
				}
			}
			branchWiseSalesDetails.put(itemCode, tempDetails);
		}
		return branchWiseSalesDetails ;
	}
}
