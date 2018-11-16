package com.jeeplus.modules.bus.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.jeeplus.common.persistence.BaseEntity;
import com.jeeplus.modules.bus.entity.Book;
import com.jeeplus.modules.sys.entity.Office;

public class StatisticsDto extends BaseEntity<StatisticsDto>{
	private static final long serialVersionUID = 1L;
	
	private Date beginDate;			//结束日期
	private Date endDate;			//开始日期
	private Office office;			//内容提供端
	private Office channelOffice;	//流量端
	
	// 权限
	private Office providerOffice;	// 内容端权限
	private Office originOffice;	// 流量端权限
	
	private BigDecimal sumRevenue;	//总金额
	private BigDecimal sumCoin;		//总阅读币
	private BigDecimal sumTicket;
	private Book book;
	
	
	public Date getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Office getOffice() {
		return office;
	}
	public void setOffice(Office office) {
		this.office = office;
	}
	public Office getChannelOffice() {
		return channelOffice;
	}
	public void setChannelOffice(Office channelOffice) {
		this.channelOffice = channelOffice;
	}
	public Office getProviderOffice() {
		return providerOffice;
	}
	public void setProviderOffice(Office providerOffice) {
		this.providerOffice = providerOffice;
	}
	public Office getOriginOffice() {
		return originOffice;
	}
	public void setOriginOffice(Office originOffice) {
		this.originOffice = originOffice;
	}
	public BigDecimal getSumRevenue() {
		return sumRevenue;
	}
	public void setSumRevenue(BigDecimal sumRevenue) {
		this.sumRevenue = sumRevenue;
	}
	public BigDecimal getSumCoin() {
		return sumCoin;
	}
	public void setSumCoin(BigDecimal sumCoin) {
		this.sumCoin = sumCoin;
	}
	public BigDecimal getSumTicket() {
		return sumTicket;
	}
	public void setSumTicket(BigDecimal sumTicket) {
		this.sumTicket = sumTicket;
	}
	public Book getBook() {
		return book;
	}
	public void setBook(Book book) {
		this.book = book;
	}
	@Override
	public void preInsert() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void preUpdate() {
		// TODO Auto-generated method stub
		
	}

	
	
}
