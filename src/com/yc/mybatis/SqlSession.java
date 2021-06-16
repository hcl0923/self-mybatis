package com.yc.mybatis;

import java.util.List;

/**
 * 会话对象
 * @author hp
 *
 */
public class SqlSession {
	private SqlSessionFactory factory;
	private DBHelper db;
	
	public SqlSession(SqlSessionFactory factory) {
		this.factory=factory;
		db=new DBHelper(factory.getConfig().getDataSource());
	}
	public <T> List<T> selectList(String sqlId,Object...params){
		MapperInfo info=factory.getMapperInfos().get(sqlId);
		if(null==info) {
			return null;
		}
		try {
			String sql=info.getSql();
			if(info.isUpdate()) {
				return null;
			}
			//如果是查询操作
			String className=info.getResultType();
			System.out.println(className);
			//类的全路径名称获取class实例对象
			Class c=Class.forName(className);
			return db.findMutipl(sql,null,c);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
