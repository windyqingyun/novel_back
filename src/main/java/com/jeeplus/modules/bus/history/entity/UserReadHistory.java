/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bus.history.entity;

import org.hibernate.validator.constraints.Length;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 用户阅读记录Entity
 * @author zhangsc
 * @version 2017-11-03
 */
public class UserReadHistory extends DataEntity<UserReadHistory> {
	
	private static final long serialVersionUID = 1L;
	private String fodderId;		// 素材id
	private String bookId;		// 点击次数
	private Integer chapter;		// chapter
	
	public UserReadHistory() {
		super();
	}

	public UserReadHistory(String id){
		super(id);
	}

	@Length(min=0, max=64, message="素材id长度必须介于 0 和 64 之间")
	@ExcelField(title="素材id", align=2, sort=1)
	public String getFodderId() {
		return fodderId;
	}

	public void setFodderId(String fodderId) {
		this.fodderId = fodderId;
	}
	
	@Length(min=0, max=64, message="点击次数长度必须介于 0 和 64 之间")
	@ExcelField(title="点击次数", align=2, sort=2)
	public String getBookId() {
		return bookId;
	}

	public void setBookId(String bookId) {
		this.bookId = bookId;
	}
	
	@ExcelField(title="chapter", align=2, sort=3)
	public Integer getChapter() {
		return chapter;
	}

	public void setChapter(Integer chapter) {
		this.chapter = chapter;
	}
	
}