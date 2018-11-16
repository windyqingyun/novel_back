package com.jeeplus.modules.bus.enums;

public enum RoleEnum {
	ADMIN("system", "管理员"),
	LOCAL_DEPT_ADMIN("c2", "本部门管理员1"),
	FLOW_ADMIN("liuliangshang", "管理员"),
	DEPT_ADMIN("depart", "部门管理员"),
	CONTENT_PROVIDE("neirongtigong", "内容提供商管理员")
	
	;
	private RoleEnum(String enname, String desc) {
		this.enname = enname;
		this.desc = desc;
	}
	
	private String enname;
	private String desc;
	
	/**
	 * @return the enname
	 */
	public String getEnname() {
		return enname;
	}
	/**
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}
	
	
}
