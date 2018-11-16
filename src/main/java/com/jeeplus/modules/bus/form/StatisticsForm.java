package com.jeeplus.modules.bus.form;

import java.io.Serializable;
import java.util.Date;

import com.jeeplus.modules.sys.entity.Office;

public class StatisticsForm implements Serializable {

	private static final long serialVersionUID = 1L;
	
	// form
	private String title;				// 资源名称
	private String formNickName;       //  昵称
	private String groupType;  			// 分组类型(01:全部数据， 02:按照用户分组， 03:按照资源分组 )
	private String bookOfficeId;		// 书籍所属机构
	private String customerOfficeId;	// 用户所属机构
	private Date beginDate;        		// 开始日期
	private Date endDate;          		// 结束日期
	
	// 权限
	private Office office;			// 内容提供端
	private Office channelOffice;	// 流量端
	
	// 其他
	private String groupBy;
	private String orderBy;
	private Integer pageNo = 1;
	private Integer startRow;
	private Integer pageSize = 10;
	
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getFormNickName() {
		return formNickName;
	}
	public void setFormNickName(String formNickName) {
		this.formNickName = formNickName;
	}
	public String getGroupType() {
		return groupType;
	}
	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}
	public String getBookOfficeId() {
		return bookOfficeId;
	}
	public void setBookOfficeId(String bookOfficeId) {
		this.bookOfficeId = bookOfficeId;
	}
	public String getCustomerOfficeId() {
		return customerOfficeId;
	}
	public void setCustomerOfficeId(String customerOfficeId) {
		this.customerOfficeId = customerOfficeId;
	}
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
	public String getGroupBy() {
		return groupBy;
	}
	public void setGroupBy(String groupBy) {
		this.groupBy = groupBy;
	}
	public String getOrderBy() {
		return orderBy;
	}
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
	public Integer getPageNo() {
		return pageNo;
	}
	public void setPageNo(Integer pageNo) {
		 this.pageNo=pageNo;
		 this.startRow = (pageNo-1)*this.pageSize;
	}
	public Integer getStartRow() {
		return startRow;
	}
	public void setStartRow(Integer startRow) {
		this.startRow = startRow;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		 this.pageSize=pageSize;
		 this.startRow = (pageNo-1)*this.pageSize;
	}


}
