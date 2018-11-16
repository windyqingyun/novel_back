/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bus.entity;

import org.hibernate.validator.constraints.Length;

import com.jeeplus.modules.sys.entity.Office;

import javax.validation.constraints.NotNull;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 小数Entity
 * @author zhangsc
 * @version 2017-11-06
 */
public class Book extends DataEntity<Book> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 名称
	private String title;		// title
	private Integer condition;	// 分类 0:女频; 1:男频
	private String author;		// 作者
	private String image;		// 图片链接
	private String tags;		// 标签，以','分割
	private Office office;		// 来源机构
	private String originalId;		// 原始id
	private Date publishDate;		// publish_date
	private Integer viewcount;		// 浏览次数
	
	private List<BookChapter> chapterList; //小说章节list
	
	public Book() {
		super();
	}

	public Book(String id){
		super(id);
	}

	@Length(min=1, max=64, message="名称长度必须介于 1 和 64 之间")
	@ExcelField(title="名称", align=2, sort=1)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Length(min=1, max=300, message="title长度必须介于 1 和 300之间")
	@ExcelField(title="title", align=2, sort=2)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	
	@ExcelField(title="分类", align=2, sort=9)
	public Integer getCondition() {
		return condition;
	}

	public void setCondition(Integer condition) {
		this.condition = condition;
	}

	@Length(min=0, max=64, message="作者长度必须介于 0 和64 之间")
	@ExcelField(title="作者", align=2, sort=3)
	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}
	
	@Length(min=0, max=255, message="图片链接长度必须介于 0 和 255 之间")
	@ExcelField(title="图片链接", align=2, sort=4)
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
	
	@Length(min=0, max=200, message="标签，以','分割长度必须介于 0 和 200 之间")
	@ExcelField(title="标签，以','分割", align=2, sort=5)
	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}
	
	@NotNull(message="来源机构不能为空")
	@ExcelField(title="来源机构", fieldType=Office.class, value="office.name", align=2, sort=6)
	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}
	
	@Length(min=1, max=64, message="原始id长度必须介于 1 和 64 之间")
	@ExcelField(title="原始id", align=2, sort=7)
	public String getOriginalId() {
		return originalId;
	}

	public void setOriginalId(String originalId) {
		this.originalId = originalId;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="publish_date不能为空")
	@ExcelField(title="publish_date", align=2, sort=8)
	public Date getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}
	
	@ExcelField(title="浏览次数", align=2, sort=9)
	public Integer getViewcount() {
		return viewcount;
	}

	public void setViewcount(Integer viewcount) {
		this.viewcount = viewcount;
	}

	public List<BookChapter> getChapterList() {
		return chapterList;
	}

	public void setChapterList(List<BookChapter> chapterList) {
		this.chapterList = chapterList;
	}
	
}