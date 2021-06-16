package com.yc.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.XPath;
import org.dom4j.io.SAXReader;

public class SqlSessionFactory {
	private MybatisConfig config;
	
	//id作为键  快速找到对应的MapperInfo对象
	private Map<String,MapperInfo> mapperInfos=new HashMap<String,MapperInfo>();
	
	public SqlSessionFactory(MybatisConfig config) {
		this.config=config;
		try {
			//config.parseXml();//解析mybatis-config.xml
			readXml();//解析映射文件
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	//解析映射文件
	public void readXml()throws DocumentException {
		//获取所有的映射文件
		List<String> mappers=config.getMappers();
		if(null==mappers||mappers.isEmpty()) {
			return;
		}
		SAXReader saxReader=new SAXReader();
		for(String mapper:mappers) {
			Document document=saxReader.read(this.getClass().getClassLoader().getResourceAsStream(mapper));
			XPath xPath=document.createXPath("//mapper/*");
			List<Element> list=xPath.selectNodes(document);
			String opname=null;
			MapperInfo info=null;
			for(Element e:list) {
				info=new MapperInfo();
				opname=e.getName();//获取元素名称
				if("select".equals(opname)) {
					info.setUpdate(false);
				}
				info.setParamterType(e.attributeValue("paramterType"));
				info.setResultType(e.attributeValue("resultType"));
				info.setSql(e.getTextTrim());
				String id=e.attributeValue("id");
				mapperInfos.put(id,info);
			}
		}
		
	}
	public MybatisConfig getConfig() {
		return config;
	}
	public Map<String, MapperInfo> getMapperInfos() {
		return mapperInfos;
	}
	
}
