package com.example.quranapp;

import java.sql.*;

public class DbController {
	private String dbname = "jdbc:sqlite:quran.db";
	private static Connection DbConnection = null;
	
	private DbController() {
		if (DbConnection == null) DbConnection = getConnection();
	  }

  public static Connection getInstance() {
   if (DbConnection == null) new DbController();
   return DbConnection;
 }
  
  private Connection getConnection() {
	Connection connection = null;
	try {
	   Class.forName("org.sqlite.JDBC");
	   connection = DriverManager.getConnection(dbname);
	   return connection;
	} catch (Exception e) {
		System.out.println("Error on getConnectin: "+ e);
		return null;
	}
  }
}
