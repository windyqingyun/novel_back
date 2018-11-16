/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bus.history.entity;

import com.jeeplus.modules.bus.entity.Book;
import org.hibernate.validator.constraints.Length;
import com.jeeplus.modules.bus.entity.Fodder;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 章节购买Entity
 * @author zhangsc
 * @version 2017-12-05
 */
public class UserBuychapterHistory extends DataEntity<UserBuychapterHistory> {
	
	private static final long serialVersionUID = 1L;
	private Book book;		// 书籍id
	private Integer chapter;		// 购买章节
	private Double originalprice;		// 原价（单位：阅读币）
	private Double discount;		// 折扣
	private Double payCoin;		// 支付的阅读币
	private Double payTicket;		// 支付的阅读券
	private String bulkbuychapterHistoryId;		// 多章购买的记录id
	private Fodder fodder;		// 素材id
	
	public UserBuychapterHistory() {
		super();
	}

	public UserBuychapterHistory(String id){
		super(id);
	}

	@ExcelField(title="书籍id", align=2, sort=1)
	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}
	
	@ExcelField(title="购买章节", align=2, sort=2)
	public Integer getChapter() {
		return chapter;
	}

	public void setChapter(Integer chapter) {
		this.chapter = chapter;
	}
	
	@ExcelField(title="原价（单位：阅读币）", align=2, sort=3)
	public Double getOriginalprice() {
		return originalprice;
	}

	public void setOriginalprice(Double originalprice) {
		this.originalprice = originalprice;
	}
	
	@ExcelField(title="折扣", align=2, sort=4)
	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}
	
	@ExcelField(title="支付的阅读币", align=2, sort=5)
	public Double getPayCoin() {
		return payCoin;
	}

	public void setPayCoin(Double payCoin) {
		this.payCoin = payCoin;
	}
	
	@ExcelField(title="支付的阅读券", align=2, sort=6)
	public Double getPayTicket() {
		return payTicket;
	}

	public void setPayTicket(Double payTicket) {
		this.payTicket = payTicket;
	}
	
	@Length(min=0, max=64, message="多章购买的记录id长度必须介于 0 和 64 之间")
	@ExcelField(title="多章购买的记录id", align=2, sort=7)
	public String getBulkbuychapterHistoryId() {
		return bulkbuychapterHistoryId;
	}

	public void setBulkbuychapterHistoryId(String bulkbuychapterHistoryId) {
		this.bulkbuychapterHistoryId = bulkbuychapterHistoryId;
	}
	
	@ExcelField(title="素材id", align=2, sort=8)
	public Fodder getFodder() {
		return fodder;
	}

	public void setFodder(Fodder fodder) {
		this.fodder = fodder;
	}
	
}