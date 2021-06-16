package com.yc.mybatis;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.XPath;
import org.dom4j.io.SAXReader;

/**
 * 配置信息 
 * @author hp
 *
 */
public class MybatisConfig {
	//数据源信息
	private DataSource dataSource;
	
	private String config;//配置文件名称
	//映射文件名称集合
	private List<String> mappers=new ArrayList<String>();
	
	
	public MybatisConfig(String config) {
		this.config=config;
		try {
			parseXml();
		}catch(DocumentException e) {
			e.printStackTrace();
		}
	}
	public MybatisConfig() {
		this.config="mybatis-config.xml";
		try {
			parseXml();
		}catch(DocumentException e) {
			e.printStackTrace();
		}
	}
	//解析xml文件mybatis-config.xml
	public void parseXml()throws DocumentException {
		SAXReader saxReader=new SAXReader();
		Document document=saxReader.read(this.getClass().getClassLoader().getResourceAsStream(config));
		/**
		 * nodename 选取此节点的所有字节点
		 *	/从根据选择
		 *	//从匹配选择 的当前节点选择文档中的节点，而不考虑他们的位置
		 *	@选取属性。
		 *	/ /datasource/property
		 *	选择datasource下的所有的property元素
		 */
		XPath xPath=document.createXPath("//datasource/property");
		List<Element> properties=xPath.selectNodes(document);
		//创建数据源对象
		dataSource =new DataSource();
		String pname="";
		for(Element e:properties) {
			pname=e.attributeValue("name");
			if("driver".equals(pname)) {
				dataSource.setDriver(e.attributeValue("value"));
			}else if("url".equals(pname)) {
				dataSource.setUrl(e.attributeValue("value"));
			}else if("user".equals(pname)) {
				dataSource.setUser(e.attributeValue("value"));
			}else if("password".equals(pname)) {
				dataSource.setPassword(e.attributeValue("value"));
			}
		}
		xPath=document.createXPath("//mappers/mapper");
		List<Element> list=xPath.selectNodes(document);
		for(Element e:list) {
			String value=e.attributeValue("resource");
			mappers.add(value);//读取所有映射文件添加到mappers集合中去
		}
		//System.out.println(mappers);
	}
	//解析xml文件mybatis-config.xml
	public void parseXml(String config)throws DocumentException {
		this.config=config;
		parseXml();
	}


	public DataSource getDataSource() {
		return dataSource;
	}


	public List<String> getMappers() {
		return mappers;
	}
}
