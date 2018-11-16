/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bus.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.bus.entity.Book;
import com.jeeplus.modules.bus.dao.BookDao;

/**
 * 小数Service
 * @author zhangsc
 * @version 2017-11-09
 */
@Service
@Transactional(readOnly = true)
public class BookService extends CrudService<BookDao, Book> {

	public Book get(String id) {
		return super.get(id);
	}
	
	public List<Book> findList(Book book) {
		dataScopeFilterByOffice(book, "dsf", null, null);
		return super.findList(book);
	}
	
	public Page<Book> findPage(Page<Book> page, Book book) {
		dataScopeFilterByOffice(book, "dsf", null, null);
		return super.findPage(page, book);
	}
	
	@Transactional(readOnly = false)
	public void save(Book book) {
		super.save(book);
	}
	
	@Transactional(readOnly = false)
	public void delete(Book book) {
		super.delete(book);
	}
	
}