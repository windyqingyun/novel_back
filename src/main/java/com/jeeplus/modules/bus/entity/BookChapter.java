/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bus.entity;

import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotNull;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 小说章节内容Entity
 * @author zhangsc
 * @version 2017-11-09
 */
public class BookChapter extends DataEntity<BookChapter> {
	
	private static final long serialVersionUID = 1L;
	private String title;		// 标题
	private Integer chapter;		// 章节
	private String content;		// 章节内容
	private String bookId;		// 对应的书籍id
	private String isvip;		// 是否是vip章节：0:否 1:是
	private String price;		// 价格（单位是阅读币）
	
	public BookChapter() {
		super();
	}

	public BookChapter(String id){
		super(id);
	}

	@Length(min=1, max=64, message="标题长度必须介于 1 和 64 之间")
	@ExcelField(title="标题", align=2, sort=1)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	@NotNull(message="章节不能为空")
	@ExcelField(title="章节", align=2, sort=2)
	public Integer getChapter() {
		return chapter;
	}

	public void setChapter(Integer chapter) {
		this.chapter = chapter;
	}
	
	@ExcelField(title="章节内容", align=2, sort=3)
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	@Length(min=1, max=64, message="对应的书籍id长度必须介于 1 和 64 之间")
	@ExcelField(title="对应的书籍id", align=2, sort=4)
	public String getBookId() {
		return bookId;
	}

	public void setBookId(String bookId) {
		this.bookId = bookId;
	}
	
	@Length(min=1, max=2, message="是否是vip章节：0:否 1:是长度必须介于 1 和 2 之间")
	@ExcelField(title="是否是vip章节：0:否 1:是", dictType="yes_no", align=2, sort=5)
	public String getIsvip() {
		return isvip;
	}

	public void setIsvip(String isvip) {
		this.isvip = isvip;
	}
	
	@ExcelField(title="价格（单位是阅读币）", align=2, sort=6)
	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}
	
}