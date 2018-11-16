/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bus.history.dao;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.bus.history.entity.UserBulkbuychapterHistory;

/**
 * 用户多章购买DAO接口
 * @author zhangsc
 * @version 2017-11-03
 */
@MyBatisDao
public interface UserBulkbuychapterHistoryDao extends CrudDao<UserBulkbuychapterHistory> {

	
}