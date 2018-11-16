package com.jeeplus.modules.bus.enums;

/**
 * 素材点击统计分组类型
 * @author zhangsc
 * @version 2017年11月22日
 */
public enum GroupTypeEnum {
	
	/** 全部数据 **/
	ALL_DATA("01", "全部数据"),
	/** 按照用户分组 **/
	BY_USER("02", "按照用户分组"),
	/** 按照素材分组 **/
	BY_RESOURCES("03", "按照资源分组");
	
	private String code;
	private String desc;
	
	GroupTypeEnum(String code, String desc){
		this.code = code;
		this.desc = desc;
	}

	public String getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}
}
