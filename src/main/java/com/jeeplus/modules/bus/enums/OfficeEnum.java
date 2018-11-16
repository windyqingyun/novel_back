package com.jeeplus.modules.bus.enums;

public enum OfficeEnum {
	PROVIDE_OFFICE("3b57e95561d84d82959aaae8211d6d0a", "内容商"),
	ORIGIN_OFFICE("b75a19627f874085afae2cbdfefe9ada", "流量商")
    ;
    private String code;
    private String desc;

	private OfficeEnum(String code, String desc) {
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
