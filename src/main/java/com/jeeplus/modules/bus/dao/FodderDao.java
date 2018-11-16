/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bus.dao;

import com.jeeplus.modules.bus.entity.Book;
import java.util.List;
import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.bus.entity.Fodder;

/**
 * 素材DAO接口
 * @author zhangsc
 * @version 2017-11-09
 */
@MyBatisDao
public interface FodderDao extends CrudDao<Fodder> {

	public List<Book> findListBybook(Book book);
	
}