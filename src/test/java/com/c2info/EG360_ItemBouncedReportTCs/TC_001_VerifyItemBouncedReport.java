package com.c2info.EG360_ItemBouncedReportTCs;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
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
	
	@Test(priority=1)
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
	
	@Test(priority=2)
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
			String actualValue = values.get("Bounced Qty");
			actualValue = actualValue.replaceAll(".00", "");
			String query = "select sum(n_qty) from item_bounce_list where c_item_code='"+itemCode+"' and d_ldate='"+today+"' ";
			ResultSet data = db.getData(query);
			while(data.next()){
				String expectedValue = data.getString(1);
				expectedValue = expectedValue.replace(".000", "").trim();
				Assert.assertEquals(actualValue, expectedValue);
			}
		}
	}
	
	@Test(priority=3)
	public void verifyBranchName() throws SQLException{
		Database db = new Database();
		ItemBouncedReport ibr = new ItemBouncedReport();
		LocalDate today = LocalDate.now();
		String itemCode ;
		Set<String> set = ibr.getBranchWiseDetails().keySet();
		HashMap<String, String> values = new HashMap<String, String>();
		for(String str : set){
			itemCode = str ;
			values.putAll(ibr.getBranchWiseDetails().get(itemCode));
			String actualValue = values.get("Branch Name");
			String brCode = values.get("Branch Code");
			String expectedValue=null ;
			String query = "select act_mst.c_name "
					+ "from act_mst,item_bounce_list "
					+ "where item_bounce_list.d_ldate='"+today+"' "
					+ "and item_bounce_list.c_item_code='"+itemCode+"' and c_br_code='"+brCode+"' "
					+ "and item_bounce_list.c_br_code=act_mst.c_code; ";
			ResultSet data = db.getData(query);
			while(data.next()){
				expectedValue = data.getString(1);
			}
			Assert.assertEquals(actualValue, expectedValue);
		}
	}
	
	@Test(priority=4)
	public void verifyDate(){
		Database db = new Database();
		ItemBouncedReport ibr = new ItemBouncedReport();
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-YYYY");
		String tdy = formatter.format(cal.getTime());
		String itemCode ;
		Set<String> set = ibr.getBranchWiseDetails().keySet();
		HashMap<String, String> values = new HashMap<String, String>();
		for(String str : set){
			itemCode = str ;
			values.putAll(ibr.getBranchWiseDetails().get(itemCode));
			String actualValue = values.get("Date");
			Assert.assertEquals(actualValue, tdy);
		}
	}
	
	@Test(priority=5)
	public void VerifyItemMfacName() throws SQLException{
		Database db = new Database();
		ItemBouncedReport ibr = new ItemBouncedReport();
		LocalDate today = LocalDate.now();
		String itemCode ;
		Set<String> set = ibr.getBranchWiseDetails().keySet();
		HashMap<String,String> values = new HashMap<String, String>();
		for(String str : set){
			itemCode = str ;
			values.putAll(ibr.getBranchWiseDetails().get(itemCode));
			String actualValue = values.get("Manufacturer Name");
			String brCode = values.get("Branch Code");
			String query = "select mfac_mst.c_name  from mfac_mst,item_mst,item_bounce_list "
					+ "where item_bounce_list.d_ldate='"+today+"' and "
					+ "item_bounce_list.c_item_code='"+itemCode+"' and item_bounce_list.c_br_code='"+brCode+"'"
					+ " and item_mst.c_code=item_bounce_list.c_item_code  and "
					+ "item_mst.c_mfac_code=mfac_mst.c_code ";
			ResultSet data = db.getData(query);
			String expectedValue = null  ;
			while(data.next()){
				expectedValue = data.getString(1);
			}
			Assert.assertEquals(actualValue, expectedValue);
		}
	}
	
	@Test(priority=6)
	public void verifyItemCode() throws SQLException{
		Database db = new Database();
		ItemBouncedReport ibr = new ItemBouncedReport();
		LocalDate today = LocalDate.now();
		String itemCode ;
		Set<String> set = ibr.getBranchWiseDetails().keySet();
		HashMap<String, String> values = new HashMap<String, String>();
		for(String str : set){
			itemCode = str ;
			//values.putAll(ibr.getBranchWiseDetails().get(itemCode));
			String actualValue = itemCode ;
			String brCode = values.get("Branch Code");
			String query = "select c_item_code from item_bounce_list where d_ldate= '"+today+"' and c_br_code='"+brCode+"'";
			ResultSet data = db.getData(query);
			while(data.next()){
				Assert.assertEquals(actualValue, data.getString(1));
			}
		}
	}
	
	@Test(priority=7)
	public void verifyItemName() throws SQLException{
		Database db = new Database();
		ItemBouncedReport ibr = new ItemBouncedReport();
		LocalDate today = LocalDate.now();
		String itemCode ;
		Set<String> set = ibr.getBranchWiseDetails().keySet();
		HashMap<String, String> values = new HashMap<String, String>();
		for(String str : set){
			itemCode = str ;
			values.putAll(ibr.getBranchWiseDetails().get(itemCode));
			String actualValue = values.get("Item Name");
			String brCode = values.get("Branch Code");
			String query = " select item_mst.c_name from item_mst,item_bounce_list"
					+ " where item_bounce_list.d_ldate='"+today+"' and item_bounce_list.c_item_code='"+itemCode+"'"
							+ " and item_mst.c_code=item_bounce_list.c_item_code " ;
			ResultSet data = db.getData(query);
			
			while(data.next()){
				Assert.assertEquals(actualValue, data.getString(1));
			}
		}
		
	}
	
	@Test(priority=8)
	public void verifyMRP() throws SQLException{
		Database db = new Database();
		ItemBouncedReport ibr = new ItemBouncedReport();
		LocalDate today = LocalDate.now();
		String itemCode ;
		Set<String> set = ibr.getBranchWiseDetails().keySet();
		HashMap<String, String> values = new HashMap<String, String>();
		for(String str : set){
			itemCode = str ;
			values.putAll(ibr.getBranchWiseDetails().get(itemCode));
			String actualValue = values.get("Mrp");
			String brCode = values.get("Branch Code");
			String query = "select n_mrp from item_bounce_list "
					+ " where d_ldate='"+today+"' and c_item_code='"+itemCode+"'  ";
			ResultSet data = db.getData(query);
			String expectedValue = null ;
			while(data.next()){
				expectedValue = data.getString(1);
			}
			Assert.assertEquals(actualValue, expectedValue);
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
