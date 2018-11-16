/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bus.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.bus.entity.BookChapter;

/**
 * 小说章节内容DAO接口
 * @author zhangsc
 * @version 2017-11-09
 */
@MyBatisDao
public interface BookChapterDao extends CrudDao<BookChapter> {
	List<BookChapter> findChapterListForSelectInput(@Param("bookId")String bookId);
	
	List<BookChapter> findHundredChapterList(BookChapter bookChapter);
}