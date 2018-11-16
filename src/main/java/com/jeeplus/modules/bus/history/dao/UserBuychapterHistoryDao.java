/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bus.history.dao;

import java.util.List;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.bus.dto.StatisticsDto;
import com.jeeplus.modules.bus.history.entity.UserBuychapterHistory;

/**
 * 用户章节购买记录DAO接口
 * @author zhangsc
 * @version 2017-11-03
 */
@MyBatisDao
public interface UserBuychapterHistoryDao extends CrudDao<UserBuychapterHistory> {

	/**
	 * 统计各个作品的收入(已过时)
	 * @param statistics
	 * @return
	 */
	public List<StatisticsDto> countRevenueByBook(StatisticsDto statistics);
	
	
	/**
	 * 统计各个作品的收入
	 * @param statistics
	 * @return
	 */
	public List<StatisticsDto> countRevenueByBookPlus(StatisticsDto statistics);
}