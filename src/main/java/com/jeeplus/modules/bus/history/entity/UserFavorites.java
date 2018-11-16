/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bus.history.entity;

import org.hibernate.validator.constraints.Length;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 用户收藏记录Entity
 * @author zhangsc
 * @version 2017-11-03
 */
public class UserFavorites extends DataEntity<UserFavorites> {
	
	private static final long serialVersionUID = 1L;
	private String fodderId;		// 素材id
	
	public UserFavorites() {
		super();
	}

	public UserFavorites(String id){
		super(id);
	}

	@Length(min=1, max=64, message="素材id长度必须介于 1 和 64 之间")
	@ExcelField(title="素材id", align=2, sort=1)
	public String getFodderId() {
		return fodderId;
	}

	public void setFodderId(String fodderId) {
		this.fodderId = fodderId;
	}
	
}