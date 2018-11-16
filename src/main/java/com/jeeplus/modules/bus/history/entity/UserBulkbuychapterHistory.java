/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bus.history.entity;

import org.hibernate.validator.constraints.Length;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 用户多章购买Entity
 * @author zhangsc
 * @version 2017-11-03
 */
public class UserBulkbuychapterHistory extends DataEntity<UserBulkbuychapterHistory> {
	
	private static final long serialVersionUID = 1L;
	private String bookId;		// 书籍id
	private Integer beginChapter;		// 开始章节
	private Integer endChapter;		// 结束章节
	private Integer buyChapterCount;		// 购买章节的数量(因为多章购买可能有已经购买的章节或免费章节，所以计算的时候要把这些章节减掉)
	private Double originalprice;		// 原价（单位：阅读币）
	private Double discount;		// 折扣
	private Double payCoin;		// 支付的阅读币
	private Double payTicket;		// 支付的阅读券
	
	public UserBulkbuychapterHistory() {
		super();
	}

	public UserBulkbuychapterHistory(String id){
		super(id);
	}

	@Length(min=0, max=64, message="书籍id长度必须介于 0 和 64 之间")
	@ExcelField(title="书籍id", align=2, sort=1)
	public String getBookId() {
		return bookId;
	}

	public void setBookId(String bookId) {
		this.bookId = bookId;
	}
	
	@ExcelField(title="开始章节", align=2, sort=2)
	public Integer getBeginChapter() {
		return beginChapter;
	}

	public void setBeginChapter(Integer beginChapter) {
		this.beginChapter = beginChapter;
	}
	
	@ExcelField(title="结束章节", align=2, sort=3)
	public Integer getEndChapter() {
		return endChapter;
	}

	public void setEndChapter(Integer endChapter) {
		this.endChapter = endChapter;
	}
	
	@ExcelField(title="购买章节的数量(因为多章购买可能有已经购买的章节或免费章节，所以计算的时候要把这些章节减掉)", align=2, sort=4)
	public Integer getBuyChapterCount() {
		return buyChapterCount;
	}

	public void setBuyChapterCount(Integer buyChapterCount) {
		this.buyChapterCount = buyChapterCount;
	}
	
	@ExcelField(title="原价（单位：阅读币）", align=2, sort=5)
	public Double getOriginalprice() {
		return originalprice;
	}

	public void setOriginalprice(Double originalprice) {
		this.originalprice = originalprice;
	}
	
	@ExcelField(title="折扣", align=2, sort=6)
	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}
	
	@ExcelField(title="支付的阅读币", align=2, sort=7)
	public Double getPayCoin() {
		return payCoin;
	}

	public void setPayCoin(Double payCoin) {
		this.payCoin = payCoin;
	}
	
	@ExcelField(title="支付的阅读券", align=2, sort=8)
	public Double getPayTicket() {
		return payTicket;
	}

	public void setPayTicket(Double payTicket) {
		this.payTicket = payTicket;
	}
	
}