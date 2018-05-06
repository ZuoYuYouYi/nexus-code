package org.nexus.util;

import java.io.IOException;
import java.sql.*;
import java.util.*;

/**
 * 数据库资源工具包
 * 
 * @author ZuoYu
 * 
 */
public class JdbcUtil {

	/**
	 * 数据库连接
	 */
	private Connection connection;

	/**
	 * 结果集
	 */
	private ResultSet resultSet;

	/**
	 * SQL语句对象
	 */
	private PreparedStatement preparedStatement;
	
	private Properties properties = null;

	/**
	 * （私有化构造器）加载数据库信息
	 */
	private JdbcUtil() {
		this.properties = new Properties();
		try {
			this.properties.load(JdbcUtil.class
					.getResourceAsStream("/jdbc.properties"));
			Class.forName(this.properties.getProperty("jdbc.driverClass"));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void open(){
		try {
			if (this.connection == null || this.connection.isClosed()) {
					connection = DriverManager.getConnection(
							this.properties.getProperty("jdbc.url"),
							this.properties.getProperty("jdbc.user"),
							this.properties.getProperty("jdbc.password"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 获取数据库工具类
	 * 
	 * @return Connection
	 */
	public static JdbcUtil getJdbcUtil() {
		return new JdbcUtil();
	}

	/**
	 * 释放资源
	 */
	private void close() {
		try {
			if (this.resultSet != null) {
				this.resultSet.close();
			}
			if (this.preparedStatement != null) {
				this.preparedStatement.close();
			}
			if (this.connection != null) {
				this.connection.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 查询
	 * 
	 * @param sql
	 * @param args
	 * @return List<Map<String, Object>>
	 */
	public List<Map<String, Object>> query(String sql, String[] args) {
		open();
		List<Map<String, Object>> resultMaps = new ArrayList<Map<String, Object>>();
		try {
			preparedStatement = connection.prepareStatement(sql);
			if (args != null && args.length > 0) {
				for (int i = 0; i < args.length; i++) {
					preparedStatement.setObject(i + 1, args[i]);
				}
			}
			resultSet = preparedStatement.executeQuery();
			ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
			int count = resultSetMetaData.getColumnCount();
			while (resultSet.next()) {
				Map<String, Object> map = new HashMap<String, Object>();
				for (int i = 0; i < count; i++) {
					String columnName = resultSetMetaData.getColumnName(i + 1);
					String columnLabel = resultSetMetaData
							.getColumnLabel(i + 1);
					String columnValue = resultSet.getString(columnLabel);
					map.put(columnName, columnValue);
				}
				resultMaps.add(map);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return resultMaps;
	}

	/**
	 * 修改或删除
	 * 
	 * @param sql
	 * @param args
	 * @return Boolean
	 */
	public Boolean upDateOrDelete(String sql, String[] args) {
		open();
		try {
			preparedStatement = connection.prepareStatement(sql);
			if (args != null && args.length > 0) {
				for (int i = 0; i < args.length; i++) {
					preparedStatement.setObject(i + 1, args[i]);
				}
			}
			int result = preparedStatement.executeUpdate();
			if (result > 0) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return false;
	}

	/**
	 * 添加
	 * 
	 * @param sql
	 * @param args
	 * @return Integer
	 */
	public Integer insert(String sql, String[] args) {
		open();
		try {
			preparedStatement = connection.prepareStatement(sql,
					Statement.RETURN_GENERATED_KEYS);
			if (args != null && args.length > 0) {
				for (int i = 0; i < args.length; i++) {
					preparedStatement.setObject(i + 1, args[i]);
				}
			}
			int result = preparedStatement.executeUpdate();
			if (result > 0) {
				resultSet = preparedStatement.getGeneratedKeys();
				while (resultSet.next()) {
					return resultSet.getInt(1);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return 0;
	}
}
