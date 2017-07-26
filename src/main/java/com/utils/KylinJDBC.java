package com.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class KylinJDBC {

	String url="jdbc:kylin://10.10.184.215:7070/ninemax";
	String username="ADMIN";
	String password="KYLIN"; 
	
	public  List<Map<String, String>> findToListMap(String sql) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Connection con = null;
		List<Map<String, String>> maplist = new ArrayList<Map<String, String>>();
		try {
			Class.forName("org.apache.kylin.jdbc.Driver");
			con =DriverManager.getConnection(url,username,password);
			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();
			ResultSetMetaData data = rs.getMetaData();
			while (rs.next()) {
				Map<String, String> map = new HashMap<String, String>();
				for (int i = 1; i <= data.getColumnCount(); i++) {
					String columnName = data.getColumnLabel(i);
					String columnValue = rs.getString(i);
					map.put(columnName, columnValue);
				}
				maplist.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(stmt!=null){
					stmt.close();					
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if(con!=null){
					con.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			try {
				if(rs!=null){
					rs.close();
				}
			} catch (Exception e3) {
				e3.printStackTrace();
			}
		}
		return maplist;
	}
	
	public  List<Map<String, String>> findToListMap1(String sql) {
		Statement stmt = null;
		ResultSet rs = null;
		Connection con = null;
		List<Map<String, String>> maplist = new ArrayList<Map<String, String>>();
		try {
			Class.forName("org.apache.kylin.jdbc.Driver");
			con =DriverManager.getConnection(url,username,password);
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			ResultSetMetaData data = rs.getMetaData();
			while (rs.next()) {
				Map<String, String> map = new HashMap<String, String>();
				for (int i = 1; i <= data.getColumnCount(); i++) {
					String columnName = data.getColumnLabel(i);
					String columnValue = rs.getString(i);
					map.put(columnName, columnValue);
				}
				maplist.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(stmt!=null){
					stmt.close();					
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if(con!=null){
					con.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			try {
				if(rs!=null){
					rs.close();
				}
			} catch (Exception e3) {
				e3.printStackTrace();
			}
		}
		return maplist;
	}
	
	public  List<String> findToList(String sql) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Connection con = null;
		List<String> list = new ArrayList<String>();
		try {
			Class.forName("org.apache.kylin.jdbc.Driver");
			con =DriverManager.getConnection(url,username,password);
			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();
			ResultSetMetaData data = rs.getMetaData();
			while (rs.next()) {
				for (int i = 1; i <= data.getColumnCount(); i++) {
					String columnValue = rs.getString(i);
					list.add(columnValue);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(stmt!=null){
					stmt.close();					
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if(con!=null){
					con.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			try {
				if(rs!=null){
					rs.close();
				}
			} catch (Exception e3) {
				e3.printStackTrace();
			}
		}
		return list;
	}
	public  List<String> findToList1(String sql) {
		Statement stmt = null;
		ResultSet rs = null;
		Connection con = null;
		List<String> list = new ArrayList<String>();
		try {
			Class.forName("org.apache.kylin.jdbc.Driver");
			con =DriverManager.getConnection(url,username,password);
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			ResultSetMetaData data = rs.getMetaData();
			while (rs.next()) {
				for (int i = 1; i <= data.getColumnCount(); i++) {
					String columnValue = rs.getString(i);
					list.add(columnValue);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(stmt!=null){
					stmt.close();					
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if(con!=null){
					con.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			try {
				if(rs!=null){
					rs.close();
				}
			} catch (Exception e3) {
				e3.printStackTrace();
			}
		}
		return list;
	}
}
