/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bus.history.dao;



import java.util.List;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.bus.dto.FodderClickDto;
import com.jeeplus.modules.bus.history.entity.UserClickHistory;

/**
 * 用户点击记录DAO接口
 * @author zhangsc
 * @version 2017-11-03
 */
@MyBatisDao
public interface UserClickHistoryDao extends CrudDao<UserClickHistory> {

	/**
	 * 获取所有的点击数据
	 * @param fodderClickDto
	 * @return
	 */
	public List<FodderClickDto> findClikCountList(FodderClickDto fodderClick);

	public List<FodderClickDto> findClikCountListGroupByUser(FodderClickDto fodderClick);
	
	public List<FodderClickDto> findClikCountListGroupByFodder(FodderClickDto fodderClick);
}