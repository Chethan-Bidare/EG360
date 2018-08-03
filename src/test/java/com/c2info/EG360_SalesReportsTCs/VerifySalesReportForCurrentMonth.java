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

public class VerifySalesReportForCurrentMonth extends TestBase{

	@BeforeClass
	public void setup() throws IOException{
		init();
		Dashboard db = new Dashboard();
		db.login(OR.getProperty("MobileNum"), OR.getProperty("otp"));
	}
	
	@Test(priority=1)
	public void verifyBranchName() throws InterruptedException, SQLException{
		SalesReports sr = new SalesReports();
		Database db = new Database();
		String brCode ;
		sr.clickOnMainMenu("Reports");
		sr.clickOnReportsSubMenu("Sales Report");
		waitforPageToLoad();
		sr.selectDateDropdown("Current Month");
		sr.clickOnViewReportButton();
		waitforPageToLoad();
		Set<String> set = sr.getBranchWiseSalesDetails().keySet();
		for(String str : set){
			brCode= str ;
			HashMap<String, String> values = new HashMap<String, String>();
			values.putAll(sr.getBranchWiseSalesDetails().get(str));
			String expectedValue = values.get("Branch Name");
			String query = "select act_mst.c_name,inv_mst.c_br_code from inv_mst, act_mst where inv_mst.c_br_code=act_mst.c_code and  c_br_code='"+brCode+"'";
			ResultSet val = db.getData(query);
			String actualValue = null ;
			while(val.next()){
				actualValue = val.getString(1);
			}
			Assert.assertEquals(actualValue, expectedValue);
		}
	}
	
	@Test(priority=2)
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
			String query = "select act_mst.c_sh_name,inv_mst.c_br_code from inv_mst, act_mst where inv_mst.c_br_code=act_mst.c_code and  c_br_code='"+brCode+"'";
			ResultSet val = db.getData(query);
			String actualValue = null ;
			while(val.next()){
				actualValue = val.getString(1);
			}
			System.out.println("ActualValue = "+actualValue+" ====== ExpectedValue="+expectedValue);
			Assert.assertEquals(actualValue, expectedValue);
		}
	}
	
	@Test (priority=3)
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
			String query = "select count(*) from inv_mst where c_br_code='"+brCode+"' and n_cancel_flag=0 and d_date>='"+getMonthStartDate()+"' and d_date<='"+getMonthEndDate()+"'";
			System.out.println(query);
			ResultSet val = db.getData(query);
			String actualValue = null ;
			while(val.next()){
				actualValue = val.getString(1);
			}
			Assert.assertEquals(actualValue, expectedValue);
		}
	}

	
	@Test(priority=4)
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
			String query = "select count(distinct c_cust_code) from inv_mst where c_br_code='"+brCode+"' and d_date>='"+getMonthStartDate()+"' and d_date<='"+getMonthEndDate()+"' and n_cancel_flag=0";
			ResultSet val = db.getData(query);
			String actualValue = null ;
			while(val.next()){
				actualValue = val.getString(1);
			}
			System.out.println("ActualValue = "+actualValue+" ====== ExpectedValue="+expectedValue);
			Assert.assertEquals(actualValue, expectedValue);
		}
	}
	
	@Test(priority=5)
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
			double expectedValue = Double.parseDouble(expectValue);
			String query = "select sum(n_discount) from inv_mst where c_br_code='"+brCode+"' and d_date>='"+getMonthStartDate()+"' and d_date<='"+getMonthEndDate()+"' and n_cancel_flag=0";
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
	
	@Test(priority=6)
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
			double expectedValue = Double.parseDouble(expectValue);
			String query = "select sum(n_taxable_amt) from inv_mst where c_br_code='"+brCode+"' and d_date>='"+getMonthStartDate()+"' and d_date<='"+getMonthEndDate()+"' and n_cancel_flag=0";
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
	
	@Test(priority=7)
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
			double expectedValue = Double.parseDouble(expectValue);
			String query = "select sum(n_total) from inv_mst where c_br_code='"+brCode+"' and d_date>='"+getMonthStartDate()+"' and d_date<='"+getMonthEndDate()+"' and n_cancel_flag=0";
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
	
	@Test(priority=8)
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
			double expectedValue = Double.parseDouble(expectValue);
			String query = "select sum(n_cgst_amt + n_sgst_amt + n_igst_amt + n_cess_amt) from inv_mst where c_br_code='"+brCode+"' and d_date>='"+getMonthStartDate()+"' and d_date<='"+getMonthEndDate()+"'  and n_cancel_flag=0";
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
	
	@Test(priority=9)
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
			double expectedValue = Double.parseDouble(expectValue);
			String query = "select sum(n_cgst_amt) from inv_mst where c_br_code='"+brCode+"' and d_date>='"+getMonthStartDate()+"' and d_date<='"+getMonthEndDate()+"'  and n_cancel_flag=0";
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
	
	@Test(priority=10)
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
			double expectedValue = Double.parseDouble(expectValue);
			String query = "select sum(n_sgst_amt) from inv_mst where c_br_code='"+brCode+"' and d_date>='"+getMonthStartDate()+"' and d_date<='"+getMonthEndDate()+"'  and n_cancel_flag=0";
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
	
	@Test(priority=11)
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
			double expectedValue = Double.parseDouble(expectValue);
			String query = "select sum(n_igst_amt) from inv_mst where c_br_code='"+brCode+"' d_date>='"+getMonthStartDate()+"' and d_date<='"+getMonthEndDate()+"'  and n_cancel_flag=0";
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
	
	@Test(priority=12)
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
			double expectedValue = Double.parseDouble(expectValue);
			String query = "select sum(n_cess_amt) from inv_mst where c_br_code='"+brCode+"' and d_date>='"+getMonthStartDate()+"' and d_date<='"+getMonthEndDate()+"'  and n_cancel_flag=0";
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
	
	@Test(priority=13)
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
			double expectedValue = Double.parseDouble(expectValue);
			String query = "select sum(n_service_chg) from inv_mst where c_br_code='"+brCode+"' and d_date>='"+getMonthStartDate()+"' and d_date<='"+getMonthEndDate()+"'  and n_cancel_flag=0";
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
