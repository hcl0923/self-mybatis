package com.yc.mybatis;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class DBHelper {
	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;
	private DataSource dataSource;
	
	public DBHelper(DataSource dataSource) {
		this.dataSource=dataSource;
		try {
			Class.forName(dataSource.getDriver());
		}catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	public Connection getConn()throws SQLException{
		conn=DriverManager.getConnection(dataSource.getUrl(),dataSource.getUser(),dataSource.getPassword());
		return conn;
	}
	public void closeAll(Connection conn, Statement stmt, ResultSet rs) {
		try {
			// 关闭结果集
			if (null != rs) {
				rs.close();
			}
			// 关闭编译对象
			if (null != stmt) {
				stmt.close();
			}
			// 关闭连接对象
			if (null != conn) {
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public <T> List<T> findMutipl(String sql,List<Object> params,Class<T> cls)throws Exception{
		List<T> list=new ArrayList<T>();
		T t;
		try {
			conn=getConn();
			pstmt=conn.prepareStatement(sql);
			setParams(pstmt,params);
			rs=pstmt.executeQuery();
			//获取所有的方法和字段名
			Method []methods=cls.getDeclaredMethods();
			List<String> columnNames=getColumnNames(rs);
			while(rs.next()) {
				//创建对象  根据反射创建
				t=cls.newInstance();
				for(Method m:methods) {
					for(String name:columnNames) {
						//set与字段名拼接  称为setName  与方法名进行比较
						if(("set"+name).equalsIgnoreCase(m.getName())) {
							String type=m.getParameterTypes()[0].getName();
							if("int".equals(type)||"java.lang.Integer".equals(type)) {
								m.invoke(t, rs.getInt(name));//数据库的字段名要和对象属性field一致
							}else if("double".equals(type)||"java.lang.Double".equals(type)){
								m.invoke(t, rs.getDouble(name));
							}else if("float".equals(type)||"java.lang.Float".equals(type)){
								m.invoke(t, rs.getFloat(name));
							}else if("long".equals(type)||"java.lang.Long".equals(type)){
								m.invoke(t, rs.getLong(name));
							}else if("java.lang.String".equals(type)){
								m.invoke(t, rs.getString(name));
							}else {
								//后期拓展
							}
						}
					}
				}
				list.add(t);//对象添加到list集合中去
			}
		}finally {
			closeAll(conn, pstmt, rs);
		}
		return list;
	}
	private void setParams(PreparedStatement pstmt, List<Object> params) throws SQLException {
		if (null == params || params.size() <= 0) {
			return;
		}
		for (int i = 0; i < params.size(); i++) {
			pstmt.setObject(i + 1, params.get(i));// 问号索引号是从1开始的
		}
	}
	private List<String> getColumnNames(ResultSet rs) throws SQLException {
		List<String> columnNames = new ArrayList<String>();
		ResultSetMetaData data = rs.getMetaData();// 列数据
		int count = data.getColumnCount();
		// 获取列名
		for (int i = 1; i <= count; i++) {
			columnNames.add(data.getColumnLabel(i));
		}
		return columnNames;
	}
}
