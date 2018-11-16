/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bus.entity;

import java.math.BigDecimal;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import com.jeeplus.modules.sys.entity.Office;

/**
 * 用户钱包Entity
 * @author zhangsc
 * @version 2017-11-29
 */
public class UserWallet extends DataEntity<UserWallet> {
	
	private static final long serialVersionUID = 1L;
	private Office office;		// 机构id
	private BigDecimal coin;		// 阅读币
	private BigDecimal ticket;		// 阅读券
	
	public UserWallet() {
		super();
	}

	public UserWallet(String id){
		super(id);
	}

	@ExcelField(title="机构id", fieldType=Office.class, value="office.name", align=2, sort=1)
	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}
	
	@ExcelField(title="阅读币", align=2, sort=2)
	public BigDecimal getCoin() {
		return coin;
	}

	public void setCoin(BigDecimal coin) {
		this.coin = coin;
	}
	
	@ExcelField(title="阅读券", align=2, sort=3)
	public BigDecimal getTicket() {
		return ticket;
	}

	public void setTicket(BigDecimal ticket) {
		this.ticket = ticket;
	}
	
}