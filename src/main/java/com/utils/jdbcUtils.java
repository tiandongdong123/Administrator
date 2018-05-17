package com.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xxl.conf.core.XxlConfClient;

public class jdbcUtils {
	
	public List<Map<String, String>> findToBean(Connection con, String sql) {
		Statement stmt = null;
		ResultSet rs = null;
		List<Map<String, String>> maplist = new ArrayList<Map<String, String>>();
		try {
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
				rs.close();
				stmt.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return maplist;
	}
	
	/**
	 * 获取数据库连接
	 * @param sql
	 * @return
	 */
	public static List<Map<String, String>> getConnect(String sql) {
		Statement stmt = null;
		ResultSet rs = null;
		List<Map<String, String>> maplist = new ArrayList<Map<String, String>>();
		Connection con = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String URL=XxlConfClient.get("wf-public.jdbc.adminManager.url",null);
			String USERNAME=XxlConfClient.get("wf-public.jdbc.username",null);
			String PASSWORD=XxlConfClient.get("wf-public.jdbc.password",null);
			con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
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
				rs.close();
				stmt.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return maplist;
	}
	
	
	/**
	 * 获取数据库连接
	 * @param sql
	 * @return
	 */
	public static List<Map<String, String>> getConnectStatistics(String sql) {
		Statement stmt = null;
		ResultSet rs = null;
		List<Map<String, String>> maplist = new ArrayList<Map<String, String>>();
		Connection con = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String URL=XxlConfClient.get("wf-admin.jdbc.statistics.url",null);
			String USERNAME=XxlConfClient.get("wf-admin.jdbc.statistics.username",null);
			String PASSWORD=XxlConfClient.get("wf-admin.jdbc.statistics.password",null);
			con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
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
				rs.close();
				stmt.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return maplist;
	}
	
	
	
	/**
	 * 获取数据库连接
	 * @param sql
	 * @return
	 */
	public static List<Map<String, String>> getConnectTheme(String sql) {
		Statement stmt = null;
		ResultSet rs = null;
		List<Map<String, String>> maplist = new ArrayList<Map<String, String>>();
		Connection con = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String URL=XxlConfClient.get("wf-admin.jdbc.hotword.url",null);
			String USERNAME=XxlConfClient.get("wf-admin.jdbc.hotword.username",null);
			String PASSWORD=XxlConfClient.get("wf-admin.jdbc.hotword.password",null);
			con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
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
				rs.close();
				stmt.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return maplist;
	}
	
}