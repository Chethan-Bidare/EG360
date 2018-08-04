package com.c2info.EG360_SalesReportsTCs;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Set;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.c2info.EG360_Database.Database;
import com.c2info.EG360_TestBase.TestBase;
import com.c2info.EG360_UIactions.Dashboard;
import com.c2info.EG360_UIactions.SalesReports;

public class TC_011_VerifySalesReportForCurrentMonth_SalesReturn extends TestBase{

	@BeforeClass
	public void setup() throws IOException{
		init();
		Dashboard db = new Dashboard();
		db.login(OR.getProperty("MobileNum"), OR.getProperty("otp"));
	}
	
	@Test(priority=132)
	public void verifyBranchName() throws InterruptedException, SQLException{
		SalesReports sr = new SalesReports();
		Database db = new Database();
		String brCode ;
		sr.clickOnMainMenu("Reports");
		sr.clickOnReportsSubMenu("Sales Report");
		waitforPageToLoad();
		sr.selectTypeDropdown("Sales Return");
		sr.selectDateDropdown("Current Month");
		sr.clickOnViewReportButton();
		waitforPageToLoad();
		Set<String> set = sr.getBranchWiseSalesDetails().keySet();
		for(String str : set){
			brCode= str ;
			HashMap<String, String> values = new HashMap<String, String>();
			values.putAll(sr.getBranchWiseSalesDetails().get(str));
			String expectedValue = values.get("Branch Name");
			String query = "select act_mst.c_name,crnt_mst.c_br_code from crnt_mst, act_mst where crnt_mst.c_br_code=act_mst.c_code and  c_br_code='"+brCode+"'";
			ResultSet val = db.getData(query);
			String actualValue = null ;
			while(val.next()){
				actualValue = val.getString(1);
			}
			Assert.assertEquals(actualValue, expectedValue);
		}
	}
	
	@Test(priority=133)
	public void verifyBranchShortName() throws InterruptedException, SQLException{
		SalesReports sr = new SalesReports();
		Database db = new Database();
		String brCode ;
		Set<String> set = sr.getBranchWiseSalesDetails().keySet();
		for(String str : set){
			brCode= str ;
			HashMap<String, String> values = new HashMap<String, String>();
			values.putAll(sr.getBranchWiseSalesDetails().get(str));
			String expectedValue = values.get("Br Short Name");
			String query = "select act_mst.c_sh_name,crnt_mst.c_br_code from crnt_mst, act_mst where crnt_mst.c_br_code=act_mst.c_code and  c_br_code='"+brCode+"'";
			ResultSet val = db.getData(query);
			String actualValue = null ;
			while(val.next()){
				actualValue = val.getString(1);
			}
			System.out.println("ActualValue = "+actualValue+" ====== ExpectedValue="+expectedValue);
			Assert.assertEquals(actualValue, expectedValue);
		}
	}
	
	@Test (priority=134)
	public void verifyBranchWiseNumberOfInvoices() throws InterruptedException, SQLException{
		SalesReports sr = new SalesReports();
		Database db = new Database();
		String brCode ;
		Set<String> set = sr.getBranchWiseSalesDetails().keySet();
		for(String str : set){
			brCode= str ;
			HashMap<String, String> values = new HashMap<String, String>();
			values.putAll(sr.getBranchWiseSalesDetails().get(str));
			String expectedValue = values.get("NoOfInvoice");
			String query = "select count(*) from crnt_mst where c_br_code='"+brCode+"' and n_cancel_flag=0 and d_date>='"+getMonthStartDate()+"' and d_date<='"+getMonthEndDate()+"'";
			System.out.println(query);
			ResultSet val = db.getData(query);
			String actualValue = null ;
			while(val.next()){
				actualValue = val.getString(1);
			}
			Assert.assertEquals(actualValue, expectedValue);
		}
	}

	
	@Test(priority=135)
	public void verifyBranchwiseNumberOfCustomers() throws InterruptedException, SQLException{
		SalesReports sr = new SalesReports();
		Database db = new Database();
		String brCode ;
		Set<String> set = sr.getBranchWiseSalesDetails().keySet();
		for(String str : set){
			brCode= str ;
			HashMap<String, String> values = new HashMap<String, String>();
			values.putAll(sr.getBranchWiseSalesDetails().get(str));
			String expectedValue = values.get("No Of Customers");
			String query = "select count(distinct c_cust_code) from crnt_mst where c_br_code='"+brCode+"' and  d_date>='"+getMonthStartDate()+"' and d_date<='"+getMonthEndDate()+"' and n_cancel_flag=0";
			ResultSet val = db.getData(query);
			String actualValue = null ;
			while(val.next()){
				actualValue = val.getString(1);
			}
			System.out.println("ActualValue = "+actualValue+" ====== ExpectedValue="+expectedValue);
			Assert.assertEquals(actualValue, expectedValue);
		}
	}
	
	@Test(priority=136)
	public void verifyBranchwiseDiscAmount() throws InterruptedException, SQLException{
		SalesReports sr = new SalesReports();
		Database db = new Database();
		String brCode ;
		Set<String> set = sr.getBranchWiseSalesDetails().keySet();
		for(String str : set){
			brCode= str ;
			HashMap<String, String> values = new HashMap<String, String>();
			values.putAll(sr.getBranchWiseSalesDetails().get(str));
			String expectValue = values.get("DisAmount");
			expectValue = expectValue.replaceAll("-","").trim();
			double expectedValue = Double.parseDouble(expectValue);
			String query = "select sum(n_discount) from crnt_mst where c_br_code='"+brCode+"' and  d_date>='"+getMonthStartDate()+"' and d_date<='"+getMonthEndDate()+"' and n_cancel_flag=0";
			ResultSet val = db.getData(query);
			double actualValue = 0;
			while(val.next()){
				actualValue = val.getDouble(1);
				actualValue = Math.round(actualValue * 100)/100d ;
			}
			System.out.println("ActualValue = "+actualValue+" ====== ExpectedValue="+expectedValue);
			Assert.assertEquals(actualValue, expectedValue);
		}
	}
	
	@Test(priority=137)
	public void verifyBranchwiseInvoiceValAfterDisc() throws InterruptedException, SQLException{
		SalesReports sr = new SalesReports();
		Database db = new Database();
		String brCode ;
		Set<String> set = sr.getBranchWiseSalesDetails().keySet();
		for(String str : set){
			brCode= str ;
			HashMap<String, String> values = new HashMap<String, String>();
			values.putAll(sr.getBranchWiseSalesDetails().get(str));
			String expectValue = values.get("InvoiceValAfterDisc");
			expectValue = expectValue.replaceAll("-","").trim();
			double expectedValue = Double.parseDouble(expectValue);
			String query = "select sum(n_taxable_amt) from crnt_mst where c_br_code='"+brCode+"' and  d_date>='"+getMonthStartDate()+"' and d_date<='"+getMonthEndDate()+"' and n_cancel_flag=0";
			ResultSet val = db.getData(query);
			double actualValue = 0;
			while(val.next()){
				actualValue = val.getDouble(1);
				actualValue = Math.round(actualValue * 100)/100d ;
			}
			System.out.println("ActualValue = "+actualValue+" ====== ExpectedValue="+expectedValue);
			Assert.assertEquals(actualValue, expectedValue);
		}
	}
	
	@Test(priority=138)
	public void verifyBranchwiseInvoiceValTax() throws InterruptedException, SQLException{
		SalesReports sr = new SalesReports();
		Database db = new Database();
		String brCode ;
		Set<String> set = sr.getBranchWiseSalesDetails().keySet();
		for(String str : set){
			brCode= str ;
			HashMap<String, String> values = new HashMap<String, String>();
			values.putAll(sr.getBranchWiseSalesDetails().get(str));
			String expectValue = values.get("InvoiceVal_Tax");
			expectValue = expectValue.replaceAll("-","").trim();
			double expectedValue = Double.parseDouble(expectValue);
			String query = "select sum(n_total) from crnt_mst where c_br_code='"+brCode+"' and  d_date>='"+getMonthStartDate()+"' and d_date<='"+getMonthEndDate()+"' and n_cancel_flag=0";
			ResultSet val = db.getData(query);
			double actualValue = 0;
			while(val.next()){
				actualValue = val.getDouble(1);
				actualValue = Math.round(actualValue * 100)/100d ;
			}
			System.out.println("ActualValue = "+actualValue+" ====== ExpectedValue="+expectedValue);
			Assert.assertEquals(actualValue, expectedValue);
		}
	}
	
	@Test(priority=139)
	public void verifyBranchwiseTaxAmount() throws InterruptedException, SQLException{
		SalesReports sr = new SalesReports();
		Database db = new Database();
		String brCode ;
		Set<String> set = sr.getBranchWiseSalesDetails().keySet();
		for(String str : set){
			brCode= str ;
			HashMap<String, String> values = new HashMap<String, String>();
			values.putAll(sr.getBranchWiseSalesDetails().get(str));
			String expectValue = values.get("TaxAmount");
			expectValue = expectValue.replaceAll("-","").trim();
			double expectedValue = Double.parseDouble(expectValue);
			String query = "select sum(n_cgst_amt + n_sgst_amt + n_igst_amt + n_cess_amt) from crnt_mst where c_br_code='"+brCode+"' and  d_date>='"+getMonthStartDate()+"' and d_date<='"+getMonthEndDate()+"' and n_cancel_flag=0";
			ResultSet val = db.getData(query);
			double actualValue = 0;
			while(val.next()){
				actualValue = val.getDouble(1);
				actualValue = Math.round(actualValue * 100)/100d ;
			}
			System.out.println("ActualValue = "+actualValue+" ====== ExpectedValue="+expectedValue);
			Assert.assertEquals(actualValue, expectedValue);
		}
	}
	
	@Test(priority=140)
	public void verifyBranchwiseCGSTAmount() throws InterruptedException, SQLException{
		SalesReports sr = new SalesReports();
		Database db = new Database();
		String brCode ;
		Set<String> set = sr.getBranchWiseSalesDetails().keySet();
		for(String str : set){
			brCode= str ;
			HashMap<String, String> values = new HashMap<String, String>();
			values.putAll(sr.getBranchWiseSalesDetails().get(str));
			String expectValue = values.get("CGST_Amt");
			expectValue = expectValue.replaceAll("-","").trim();
			double expectedValue = Double.parseDouble(expectValue);
			String query = "select sum(n_cgst_amt) from crnt_mst where c_br_code='"+brCode+"' and  d_date>='"+getMonthStartDate()+"' and d_date<='"+getMonthEndDate()+"' and n_cancel_flag=0";
			ResultSet val = db.getData(query);
			double actualValue = 0;
			while(val.next()){
				actualValue = val.getDouble(1);
				actualValue = Math.round(actualValue * 100)/100d ;
			}
			System.out.println("ActualValue = "+actualValue+" ====== ExpectedValue="+expectedValue);
			Assert.assertEquals(actualValue, expectedValue);
		}
	}
	
	@Test(priority=141)
	public void verifyBranchwiseSGSTAmount() throws InterruptedException, SQLException{
		SalesReports sr = new SalesReports();
		Database db = new Database();
		String brCode ;
		Set<String> set = sr.getBranchWiseSalesDetails().keySet();
		for(String str : set){
			brCode= str ;
			HashMap<String, String> values = new HashMap<String, String>();
			values.putAll(sr.getBranchWiseSalesDetails().get(str));
			String expectValue = values.get("SGST_Amt");
			expectValue = expectValue.replaceAll("-","").trim();
			double expectedValue = Double.parseDouble(expectValue);
			String query = "select sum(n_sgst_amt) from crnt_mst where c_br_code='"+brCode+"' and  d_date>='"+getMonthStartDate()+"' and d_date<='"+getMonthEndDate()+"' and n_cancel_flag=0";
			ResultSet val = db.getData(query);
			double actualValue = 0;
			while(val.next()){
				actualValue = val.getDouble(1);
				actualValue = Math.round(actualValue * 100)/100d ;
			}
			System.out.println("ActualValue = "+actualValue+" ====== ExpectedValue="+expectedValue);
			Assert.assertEquals(actualValue, expectedValue);
		}
	}
	
	@Test(priority=142)
	public void verifyBranchwiseIGSTAmount() throws InterruptedException, SQLException{
		SalesReports sr = new SalesReports();
		Database db = new Database();
		String brCode ;
		Set<String> set = sr.getBranchWiseSalesDetails().keySet();
		for(String str : set){
			brCode= str ;
			HashMap<String, String> values = new HashMap<String, String>();
			values.putAll(sr.getBranchWiseSalesDetails().get(str));
			String expectValue = values.get("IGST_Amt");
			expectValue = expectValue.replaceAll("-","").trim();
			double expectedValue = Double.parseDouble(expectValue);
			String query = "select sum(n_igst_amt) from crnt_mst where c_br_code='"+brCode+"'  d_date>='"+getMonthStartDate()+"' and d_date<='"+getMonthEndDate()+"' and n_cancel_flag=0";
			ResultSet val = db.getData(query);
			double actualValue = 0;
			while(val.next()){
				actualValue = val.getDouble(1);
				actualValue = Math.round(actualValue * 100)/100d ;
			}
			System.out.println("ActualValue = "+actualValue+" ====== ExpectedValue="+expectedValue);
			Assert.assertEquals(actualValue, expectedValue);
		}
	}
	
	@Test(priority=143)
	public void verifyBranchwiseCessAmount() throws InterruptedException, SQLException{
		SalesReports sr = new SalesReports();
		Database db = new Database();
		String brCode ;
		Set<String> set = sr.getBranchWiseSalesDetails().keySet();
		for(String str : set){
			brCode= str ;
			HashMap<String, String> values = new HashMap<String, String>();
			values.putAll(sr.getBranchWiseSalesDetails().get(str));
			String expectValue = values.get("CESS_Amt");
			expectValue = expectValue.replaceAll("-","").trim();
			double expectedValue = Double.parseDouble(expectValue);
			String query = "select sum(n_cess_amt) from crnt_mst where c_br_code='"+brCode+"' and  d_date>='"+getMonthStartDate()+"' and d_date<='"+getMonthEndDate()+"' and n_cancel_flag=0";
			ResultSet val = db.getData(query);
			double actualValue = 0;
			while(val.next()){
				actualValue = val.getDouble(1);
				actualValue = Math.round(actualValue * 100)/100d ;
			}
			System.out.println("ActualValue = "+actualValue+" ====== ExpectedValue="+expectedValue);
			Assert.assertEquals(actualValue, expectedValue);
		}
	}
	
	@Test(priority=144)
	public void verifyBranchwiseServiceChargeAmount() throws InterruptedException, SQLException{
		SalesReports sr = new SalesReports();
		Database db = new Database();
		String brCode ;
		Set<String> set = sr.getBranchWiseSalesDetails().keySet();
		for(String str : set){
			brCode= str ;
			HashMap<String, String> values = new HashMap<String, String>();
			values.putAll(sr.getBranchWiseSalesDetails().get(str));
			String expectValue = values.get("ServiceCharge");
			expectValue = expectValue.replaceAll("-","").trim();
			double expectedValue = Double.parseDouble(expectValue);
			String query = "select sum(n_service_chg) from crnt_mst where c_br_code='"+brCode+"' and  d_date>='"+getMonthStartDate()+"' and d_date<='"+getMonthEndDate()+"' and n_cancel_flag=0";
			ResultSet val = db.getData(query);
			double actualValue = 0;
			while(val.next()){
				actualValue = val.getDouble(1);
				actualValue = Math.round(actualValue * 100)/100d ;
			}
			System.out.println("ActualValue = "+actualValue+" ====== ExpectedValue="+expectedValue);
			Assert.assertEquals(actualValue, expectedValue);
		}
	}
}
