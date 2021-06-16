package com.yc.mybatis;
/**
 * 映射文件对象
 * @author hp
 *
 */
public class MapperInfo {
	private String paramterType;
	private String resultType;
	private String sql;
	private boolean isUpdate=true;
	public String getParamterType() {
		return paramterType;
	}
	public void setParamterType(String paramterType) {
		this.paramterType = paramterType;
	}
	public String getResultType() {
		return resultType;
	}
	public void setResultType(String resultType) {
		this.resultType = resultType;
	}
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
	public boolean isUpdate() {
		return isUpdate;
	}
	public void setUpdate(boolean isUpdate) {
		this.isUpdate = isUpdate;
	}
	@Override
	public String toString() {
		return "MapperInfo [paramterType=" + paramterType + ", resultType=" + resultType + ", sql=" + sql
				+ ", isUpdate=" + isUpdate + "]";
	}
	
}
