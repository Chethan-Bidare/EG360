package com.c2info.EG360_ItemBouncedReportTCs;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Set;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.c2info.EG360_Database.Database;
import com.c2info.EG360_TestBase.TestBase;
import com.c2info.EG360_UIactions.Dashboard;
import com.c2info.EG360_UIactions.ItemBouncedReport;

public class TC_001_VerifyItemBouncedReport extends TestBase{

	@BeforeClass
	public void setup() throws IOException{
		init();
		Dashboard db = new Dashboard();
		db.login(OR.getProperty("MobileNum"), OR.getProperty("otp"));
	}
	
	@Test
	public void verifyBranchCode() throws SQLException{
		Dashboard dashboard = new Dashboard();
		Database db = new Database();
		ItemBouncedReport ibReport = new ItemBouncedReport();
		dashboard.clickOnMainMenu("Reports");
		dashboard.clickOnReportsSubMenu("Item Bounced");
		waitforPageToLoad();
		LocalDate today = LocalDate.now();
		String itemCode ;
		Set<String> set = ibReport.getBranchWiseDetails().keySet();
		HashMap<String, String> values = new HashMap<String, String>();
		for(String str : set){
			itemCode = str ;
			values.putAll(ibReport.getBranchWiseDetails().get(itemCode));
			String actualValue = values.get("Branch Code");
			String query = "select c_br_code from item_bounce_list where c_item_code='"+itemCode+"' and d_ldate='"+today+"' ";
			ResultSet data = db.getData(query);
			while(data.next()){
				String expectedValue = data.getString(1);
				Assert.assertEquals(actualValue, expectedValue);
			}
		}
	}
	
	@Test
	public void verifyBouncedItemQty() throws SQLException{
		Dashboard dashboard = new Dashboard();
		Database db = new Database();
		ItemBouncedReport ibReport = new ItemBouncedReport();
		dashboard.clickOnMainMenu("Reports");
		dashboard.clickOnReportsSubMenu("Item Bounced");
		waitforPageToLoad();
		LocalDate today = LocalDate.now();
		String itemCode ;
		Set<String> set = ibReport.getBranchWiseDetails().keySet();
		HashMap<String, String> values = new HashMap<String, String>();
		for(String str : set){
			itemCode = str ;
			values.putAll(ibReport.getBranchWiseDetails().get(itemCode));
			String actualValue = values.get("Quantity");
			String query = "select sum(n_qty) from item_bounce_list where c_item_code='"+itemCode+"' and d_ldate='"+today+"' ";
			ResultSet data = db.getData(query);
			while(data.next()){
				String expectedValue = data.getString(1);
				Assert.assertEquals(actualValue, expectedValue);
			}
		}
	}
	
}
