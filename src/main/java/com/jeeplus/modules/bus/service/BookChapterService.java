/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bus.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.bus.entity.BookChapter;
import com.jeeplus.modules.bus.dao.BookChapterDao;

/**
 * 小说章节内容Service
 * @author zhangsc
 * @version 2017-11-09
 */
@Service
@Transactional(readOnly = true)
public class BookChapterService extends CrudService<BookChapterDao, BookChapter> {

	public BookChapter get(String id) {
		return super.get(id);
	}
	
	public List<BookChapter> findList(BookChapter bookChapter) {
		return super.findList(bookChapter);
	}
	
	public Page<BookChapter> findPage(Page<BookChapter> page, BookChapter bookChapter) {
		return super.findPage(page, bookChapter);
	}
	
	@Transactional(readOnly = false)
	public void save(BookChapter bookChapter) {
		super.save(bookChapter);
	}
	
	@Transactional(readOnly = false)
	public void delete(BookChapter bookChapter) {
		super.delete(bookChapter);
	}
	
	/**
	 * 根据bookId获取它下面所有的章节，用于select下拉框显示
	 * @param bookId
	 * @return
	 */
	public List<BookChapter> findChapterListForSelectInput(String bookId){
		return dao.findChapterListForSelectInput(bookId);
	}
	
	/**
	 * 根据bookId获取它下面所有的章节，用于select下拉框显示
	 * @param bookId
	 * @return
	 */
	public List<BookChapter> findHundredChapterList(BookChapter bookChapter){
		Page<BookChapter> page = new Page<BookChapter>(1, 100);
		
		bookChapter.setPage(page);
		return dao.findHundredChapterList(bookChapter);
	}
	
}