package com.yc.test;

import java.util.List;

import org.junit.Test;

import com.yc.bean.UserType;
import com.yc.mybatis.MybatisConfig;
import com.yc.mybatis.SqlSession;
import com.yc.mybatis.SqlSessionFactory;

public class UserTypeDAOTest {
	@Test
	public void test01() {
		MybatisConfig config=new MybatisConfig("mybatis-config.xml");
		SqlSessionFactory factory=new SqlSessionFactory(config);
		System.out.println(factory.getMapperInfos());
		SqlSession session=new SqlSession(factory);
		List<UserType> list=session.selectList("findAll");
		System.out.println(list);
	}
	
}
