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

public class TC_019_VerifySalesAndSalesReturnReportForLastMonth extends TestBase{

	@BeforeClass
	public void setup() throws IOException{
		init();
		Dashboard db = new Dashboard();
		db.login(OR.getProperty("MobileNum"), OR.getProperty("otp"));
	}
	
	@Test(priority=236)
	public void verifyBranchName() throws InterruptedException, SQLException{
		SalesReports sr = new SalesReports();
		Database db = new Database();
		Dashboard dashboard = new Dashboard();
		String brCode ;
		dashboard.clickOnMainMenu("Reports");
		dashboard.clickOnReportsSubMenu("Sales Report");
		waitforPageToLoad();
		sr.selectTypeDropdown("Sales & Sales Return");
		sr.selectDateDropdown("Last Week");
		sr.clickOnViewReportButton();
		waitforPageToLoad();
		Set<String> set = sr.getBranchWiseSalesDetails().keySet();
		for(String str : set){
			brCode = str ;
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
	
	@Test(priority=237)
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
	
	@Test (priority=238)
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
			String query = "select count(*) from inv_mst where c_br_code='"+brCode+"' and n_cancel_flag=0 and d_date>='"+getLastMonthStartDate()+"' and d_date<='"+getLastMonthEndDate()+"'";
			System.out.println(query);
			ResultSet val = db.getData(query);
			String actualValue = null ;
			while(val.next()){
				actualValue = val.getString(1);
			}
			Assert.assertEquals(actualValue, expectedValue);
		}
	}

	
	@Test(priority=239)
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
			String query = "select sum(t.invCust)-sum(t.crntCust) from "
					+ "(SELECT sum(distinct c_cust_code) as invCust,0 as crntCust FROM inv_mst where d_date>='"+getLastMonthStartDate()+"' and d_date<='"+getLastMonthEndDate()+"' and c_br_code='"+brCode+"'  and n_cancel_flag=0 "
							+ "union "
							+ "SELECT 0 as invCust,sum(distinct c_cust_code) as crntCust FROM crnt_mst where d_date>='"+getLastMonthStartDate()+"' and d_date<='"+getLastMonthEndDate()+"' and c_br_code='"+brCode+"' and n_cancel_flag=0)t";
			ResultSet val = db.getData(query);
			String actualValue = null ;
			while(val.next()){
				actualValue = val.getString(1);
			}
			System.out.println("ActualValue = "+actualValue+" ====== ExpectedValue="+expectedValue);
			Assert.assertEquals(actualValue, expectedValue);
		}
	}
	
	@Test(priority=240)
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
			String query = "select sum(t.invDisc)-sum(t.crntDisc) from "
					+ "(SELECT sum(n_discount) as invDisc,0 as crntDisc FROM inv_mst where d_date>='"+getLastMonthStartDate()+"' and d_date<='"+getLastMonthEndDate()+"' and c_br_code='"+brCode+"' and n_cancel_flag=0 "
					+ "union "
					+ "SELECT 0 as invDisc,sum(n_discount) as crntDisc FROM crnt_mst where d_date>='"+getLastMonthStartDate()+"' and d_date<='"+getLastMonthEndDate()+"' and c_br_code='"+brCode+"' and n_cancel_flag=0)t";
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
	
	@Test(priority=241)
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
			String query = "select sum(t.invVal)-sum(t.crntVal) from "
					+ "(SELECT sum(n_taxable_amt) as invVal,0 as crntVal FROM inv_mst where d_date>='"+getLastMonthStartDate()+"' and d_date<='"+getLastMonthEndDate()+"'  and c_br_code='"+brCode+"' n_cancel_flag=0 "
					+ "union "
					+ "SELECT 0 as invVal,sum(n_taxable_amt) as crntVal FROM crnt_mst where d_date>='"+getLastMonthStartDate()+"' and d_date<='"+getLastMonthEndDate()+"'  and c_br_code='"+brCode+"' n_cancel_flag=0)t";
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
	
	@Test(priority=242)
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
			String query ="select sum(t.invVal)-sum(t.crntVal) from "
					+ "(SELECT sum(n_total) as invVal,0 as crntVal FROM inv_mst where d_date>='"+getLastMonthStartDate()+"' and d_date<='"+getLastMonthEndDate()+"'  and c_br_code='"+brCode+"' n_cancel_flag=0 "
					+ "union "
					+ "SELECT 0 as invVal,sum(n_total) as crntVal FROM crnt_mst where d_date>='"+getLastMonthStartDate()+"' and d_date<='"+getLastMonthEndDate()+"'  and c_br_code='"+brCode+"' n_cancel_flag=0)t";
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
	
	@Test(priority=243)
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
			String query = "select sum(t.invTax)-sum(t.crntTax) from "
					+ "(SELECT sum(n_cgst_amt + n_sgst_amt + n_igst_amt + n_cess_amt) as invTax,0 as crntTax FROM inv_mst where d_date>='"+getLastMonthStartDate()+"' and d_date<='"+getLastMonthEndDate()+"'  and c_br_code='"+brCode+"' n_cancel_flag=0 "
					+ "union "
					+ "SELECT 0 as invTax,sum(n_cgst_amt + n_sgst_amt + n_igst_amt + n_cess_amt) as crntTax FROM crnt_mst where d_date>='"+getLastMonthStartDate()+"' and d_date<='"+getLastMonthEndDate()+"'  and c_br_code='"+brCode+"' n_cancel_flag=0)t";
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
	
	@Test(priority=244)
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
			String query = "select sum(t.invTax)-sum(t.crntTax) from "
					+ "(SELECT sum(n_cgst_amt) as invTax,0 as crntTax FROM inv_mst where d_date>='"+getLastMonthStartDate()+"' and d_date<='"+getLastMonthEndDate()+"'  and c_br_code='"+brCode+"' n_cancel_flag=0 "
					+ "union "
					+ "SELECT 0 as invTax,sum(n_cgst_amt) as crntTax FROM crnt_mst where d_date>='"+getLastMonthStartDate()+"' and d_date<='"+getLastMonthEndDate()+"' and c_br_code='"+brCode+"' n_cancel_flag=0)t";
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
	
	@Test(priority=245)
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
			String query = "select sum(t.invTax)-sum(t.crntTax) from "
					+ "(SELECT sum(n_sgst_amt) as invTax,0 as crntTax FROM inv_mst where d_date>='"+getLastMonthStartDate()+"' and d_date<='"+getLastMonthEndDate()+"' and c_br_code='"+brCode+"' and n_cancel_flag=0 "
					+ "union "
					+ "SELECT 0 as invTax,sum(n_sgst_amt) as crntTax FROM crnt_mst where d_date>='"+getLastMonthStartDate()+"' and d_date<='"+getLastMonthEndDate()+"' and c_br_code='"+brCode+"' and n_cancel_flag=0)t";
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
	
	@Test(priority=246)
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
			String query = "select sum(t.invTax)-sum(t.crntTax) from "
					+ "(SELECT sum(n_igst_amt) as invTax,0 as crntTax FROM inv_mst where d_date>='"+getLastMonthStartDate()+"' and d_date<='"+getLastMonthEndDate()+"'  and c_br_code='"+brCode+"' and n_cancel_flag=0 "
					+ "union "
					+ "SELECT 0 as invTax,sum(n_igst_amt) as crntTax FROM crnt_mst where d_date>='"+getLastMonthStartDate()+"' and d_date<='"+getLastMonthEndDate()+"'  and c_br_code='"+brCode+"' and n_cancel_flag=0)t";
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
	
	@Test(priority=247)
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
			String query = "select sum(t.invTax)-sum(t.crntTax) from "
					+ "(SELECT sum(n_cess_amt) as invTax,0 as crntTax FROM inv_mst where d_date>='"+getLastMonthStartDate()+"' and d_date<='"+getLastMonthEndDate()+"'  and c_br_code='"+brCode+"' and n_cancel_flag=0 "
					+ "union "
					+ "SELECT 0 as invTax,sum(n_cess_amt) as crntTax FROM crnt_mst where d_date>='"+getLastMonthStartDate()+"' and d_date<='"+getLastMonthEndDate()+"'  and c_br_code='"+brCode+"' and n_cancel_flag=0)t";
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
	
	@Test(priority=248)
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
			String query = "select sum(t.invTax)-sum(t.crntTax) from "
					+ "(SELECT sum(n_service_chg) as invTax,0 as crntTax FROM inv_mst where d_date>='"+getLastMonthStartDate()+"' and d_date<='"+getLastMonthEndDate()+"'  and c_br_code='"+brCode+"' and n_cancel_flag=0 "
					+ "union "
					+ "SELECT 0 as invTax,sum(n_service_chg) as crntTax FROM crnt_mst where d_date>='"+getLastMonthStartDate()+"' and d_date<='"+getLastMonthEndDate()+"'  and c_br_code='"+brCode+"' and n_cancel_flag=0)t";
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
