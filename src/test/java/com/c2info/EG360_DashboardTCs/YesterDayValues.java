package com.c2info.EG360_DashboardTCs;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.c2info.EG360_Database.Database;
import com.c2info.EG360_TestBase.TestBase;
import com.c2info.EG360_UIactions.Dashboard;

public class YesterDayValues extends TestBase {

	public static final Logger log = Logger.getLogger(YesterDayValues.class.getName());
	
	
	@BeforeClass
	public void setup() throws IOException{
		init();
		Dashboard db = new Dashboard();
		db.login(OR.getProperty("MobileNum"), OR.getProperty("otp"));
	}
	
	
	@Test
	public void verifyYesterDaySalesValue() throws SQLException{
		Dashboard dashboard = new Dashboard();
		Database db = new Database();
		String dbValue= null ;
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, -1);
		SimpleDateFormat formatter = new SimpleDateFormat("YYYY-MM-dd");
		String yestDate = formatter.format(cal.getTime());
		String query = "select sum(t.invtot)-sum(t.crnttot) from (SELECT sum(n_total) as invtot,0 as crnttot FROM inv_mst where d_date='"+yestDate+"' union SELECT 0 as invtot,sum(n_total) as crnttot FROM crnt_mst where d_date='"+yestDate+"')t " ;
		ResultSet data = db.getData(query);
		double value = dashboard.getYesterdaySales();
		while(data.next()){
			 dbValue = data.getString(1);
		}
		double dbVal = Double.parseDouble(dbValue);
		dbVal = dbVal/100000 ;
		double actualValue = Math.round(dbVal*100)/100d ;
		System.out.println(" value = "+value);
		System.out.println("actual value = "+actualValue);
		Assert.assertEquals(value, actualValue);
	}
	
	@Test
	public void verifyYesterdayPurchaseValue() throws SQLException{
		Dashboard dashboard = new Dashboard();
		Database db = new Database();
		String dbValue= null ;
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, -1);
		SimpleDateFormat formatter = new SimpleDateFormat("YYYY-MM-dd");
		String yestDate = formatter.format(cal.getTime());
		String query = "select sum(t.purtot)-sum(t.purRetTot) from (SELECT sum(n_total) as purtot,0 as purRetTot FROM pur_mst where d_date='"+yestDate+"' and c_prefix='K' union SELECT 0 as purtot,sum(n_total) as purRetTot FROM pur_mst where d_date='"+yestDate+"' and c_prefix='I')t" ;
		ResultSet data = db.getData(query);
		while(data.next()){
			 dbValue = data.getString(1);
		}
		double dbVal = Double.parseDouble(dbValue);
		dbVal = dbVal / 100000 ;
		double actualValue = Math.round(dbVal*100)/100d ;
		double value = dashboard.getYesterdayPurchase();
		System.out.println(" value = "+value);
		System.out.println("actual value = "+actualValue);
		Assert.assertEquals(value, actualValue);
	}
	
	
	/*select sum(t.purtot)-sum(t.purRetTot) 
	from (SELECT sum(n_total) as purtot,0 as purRetTot 
			FROM pur_mst 
			where d_date='"+yestDate+"' and c_prefix='K' 
			union
			SELECT 0 as purtot,sum(n_total) as purRetTot 
			FROM pur_mst 
			where d_date='"+yestDate+"' and c_prefix='I')t*/
}
