package com.c2info.EG360_Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

	public Connection conn ;
	public Statement statement ;
	
	public Statement getStatement(){
		try {
			String driver = "com.mysql.cj.jdbc.Driver" ;
			String connection = "jdbc:mysql://egcloud.cluster-ro-chjsglnbt4kn.ap-south-1.rds.amazonaws.com:3306/04M000";
			String userName = "root" ;
			String password = "EgCloud123!" ;
			Class.forName(driver);
			conn = DriverManager.getConnection(connection, userName, password);
			statement = conn.createStatement();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return statement ;
	}
	
	public ResultSet getData(String query) throws SQLException{
		ResultSet data = getStatement().executeQuery(query);
		return data ;
	}
	
	public void updateData(String query) throws SQLException{
		getStatement().executeUpdate(query);
	}
	
	public void insertData(String query) throws SQLException{
		getStatement().executeUpdate(query);
	}
	
	public void deleteData(String query) throws SQLException{
		getStatement().executeUpdate(query);
	}
	
}
