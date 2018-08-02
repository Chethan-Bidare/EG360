package com.c2info.EG360_SalesReportsTCs;

import java.io.IOException;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.c2info.EG360_TestBase.TestBase;
import com.c2info.EG360_UIactions.Dashboard;
import com.c2info.EG360_UIactions.SalesReports;

public class VerifySalesReport extends TestBase{

	@BeforeClass
	public void setup() throws IOException{
		init();
		Dashboard db = new Dashboard();
		db.login(OR.getProperty("MobileNum"), OR.getProperty("otp"));
	}
	
	@Test
	public void verifyBranchWiseNumberOfInvoices() throws InterruptedException{
		SalesReports sr = new SalesReports();
		sr.clickOnMainMenu("Reports");
		sr.clickOnSubMenu("Sales Report");
		Thread.sleep(60000);
		System.out.println(sr.getBranchWiseSalesDetails());
	}
	

}
