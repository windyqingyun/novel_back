/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bus.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.modules.bus.entity.Book;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.bus.entity.Fodder;
import com.jeeplus.modules.bus.dao.FodderDao;

/**
 * 素材Service
 * @author zhangsc
 * @version 2017-11-09
 */
@Service
@Transactional(readOnly = true)
public class FodderService extends CrudService<FodderDao, Fodder> {

	public Fodder get(String id) {
		return super.get(id);
	}
	
	public List<Fodder> findList(Fodder fodder) {
		dataScopeFilterByOffice(fodder, "dsf", null, null);
		return super.findList(fodder);
	}
	
	public Page<Fodder> findPage(Page<Fodder> page, Fodder fodder) {
		dataScopeFilterByOffice(fodder, "dsf", null, null);
		return super.findPage(page, fodder);
	}
	
	@Transactional(readOnly = false)
	public void save(Fodder fodder) {
		super.save(fodder);
	}
	
	@Transactional(readOnly = false)
	public void delete(Fodder fodder) {
		super.delete(fodder);
	}
	
	public Page<Book> findPageBybook(Page<Book> page, Book book) {
		dataScopeFilterByOffice(book, "dsf", null, null);
		
		book.setPage(page);
		page.setList(dao.findListBybook(book));
		return page;
	}
	
	
	
}