package com.jeeplus.modules.bus.dto;

import java.util.Date;

import com.jeeplus.common.persistence.BaseEntity;

public class BookClickDto extends BaseEntity<BookClickDto> {

	private static final long serialVersionUID = 1L;

	// dto
	private String bookTitle;
	private String nickName;
	private String count;
	private String bookOffice;
	private String originOffice;
	private Date createTime;

	public String getBookTitle() {
		return bookTitle;
	}

	public void setBookTitle(String bookTitle) {
		this.bookTitle = bookTitle;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getBookOffice() {
		return bookOffice;
	}

	public void setBookOffice(String bookOffice) {
		this.bookOffice = bookOffice;
	}

	public String getOriginOffice() {
		return originOffice;
	}

	public void setOriginOffice(String originOffice) {
		this.originOffice = originOffice;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
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
