package com.c2info.EG360_DashboardTCs;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.c2info.EG360_Database.Database;
import com.c2info.EG360_TestBase.TestBase;
import com.c2info.EG360_UIactions.Dashboard;

public class TodayValues extends TestBase{

	@BeforeClass
	public void setup() throws IOException{
		init();
		Dashboard db = new Dashboard();
		db.login(OR.getProperty("MobileNum"), OR.getProperty("otp"));
	}
	
	@Test(priority=0)
	public void verifyTodaySalesValue() throws SQLException{
		Dashboard dashboard = new Dashboard();
		Database db = new Database();
		String dbResult = null ;
		LocalDate today = LocalDate.now();
		String query = "select sum(t.invtot)-sum(t.crnttot) from (SELECT sum(n_total) as invtot,0 as crnttot FROM inv_mst where d_date='"+today+"' and n_cancel_flag=0 union SELECT 0 as invtot,sum(n_total) as crnttot FROM crnt_mst where d_date='"+today+"' and n_cancel_flag=0)t" ;
		ResultSet value = db.getData(query);
		while(value.next()){
			dbResult = value.getString(1);
		}
		double actualValue = Double.parseDouble(dbResult);
		actualValue = actualValue / 100000 ;
		actualValue = Math.round(actualValue * 100)/100d ;
		double expectedValue = dashboard.getTodaySales();
		Assert.assertEquals(expectedValue,actualValue);
	}
	
	@Test(priority=1)
	public void verifyTodayPurchaseValue() throws SQLException{
		Dashboard dashboard = new Dashboard();
		Database db = new Database();
		String dbResult = null ;
		LocalDate today = LocalDate.now();
		String query = "select sum(t.purtot) + sum(t.purRetTot) from (SELECT sum(n_total) as purtot,0 as purRetTot FROM pur_mst where d_date='"+today+"' and c_prefix='K' and n_cancel_flag=0 and n_post=1 union SELECT 0 as purtot,sum(n_total) as purRetTot FROM pur_mst where d_date='"+today+"' and c_prefix='I' and n_cancel_flag=0 and n_post=1)t" ;
		ResultSet value = db.getData(query);
		while(value.next()){
			dbResult = value.getString(1);
		}
		double actualValue = Double.parseDouble(dbResult);
		actualValue = actualValue / 100000 ;
		actualValue = Math.round(actualValue * 100)/100d ;
		double expectedValue = dashboard.getTodayPurchase();
		Assert.assertEquals(expectedValue,actualValue);
	}
	
	@Test(priority=2)
	public void verifyTodayGRNValue() throws SQLException{
		Dashboard dashboard = new Dashboard();
		Database db = new Database();
		String dbResult = null ;
		LocalDate today = LocalDate.now();
		String query = "select sum(n_total) from grn_mst where d_date='"+today+"' and n_cancel_flag=0 and n_post=1" ;
		ResultSet value = db.getData(query);
		while(value.next()){
			dbResult = value.getString(1);
		}
		double actualValue = Double.parseDouble(dbResult);
		actualValue = actualValue / 100000 ;
		actualValue = Math.round(actualValue * 100)/100d ;
		double expectedValue = dashboard.getTodayGRN();
		Assert.assertEquals(expectedValue,actualValue);
	}
	
	@Test(priority=3)
	public void verifyTodayGDNValue() throws SQLException{
		Dashboard dashboard = new Dashboard();
		Database db = new Database();
		String dbResult = null ;
		LocalDate today = LocalDate.now();
		String query = "select sum(n_total) from gdn_mst where d_date='"+today+"' and n_cancel_flag=0" ;
		ResultSet value = db.getData(query);
		while(value.next()){
			dbResult = value.getString(1);
		}
		double actualValue = Double.parseDouble(dbResult);
		actualValue = actualValue / 100000 ;
		actualValue = Math.round(actualValue * 100)/100d ;
		double expectedValue = dashboard.getTodayGDN();
		Assert.assertEquals(expectedValue,actualValue);
	}
}
