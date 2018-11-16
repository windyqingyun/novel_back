/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bus.history.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.bus.history.entity.UserBulkbuychapterHistory;
import com.jeeplus.modules.bus.history.dao.UserBulkbuychapterHistoryDao;

/**
 * 用户多章购买Service
 * @author zhangsc
 * @version 2017-11-03
 */
@Service
@Transactional(readOnly = true)
public class UserBulkbuychapterHistoryService extends CrudService<UserBulkbuychapterHistoryDao, UserBulkbuychapterHistory> {

	public UserBulkbuychapterHistory get(String id) {
		return super.get(id);
	}
	
	public List<UserBulkbuychapterHistory> findList(UserBulkbuychapterHistory userBulkbuychapterHistory) {
		return super.findList(userBulkbuychapterHistory);
	}
	
	public Page<UserBulkbuychapterHistory> findPage(Page<UserBulkbuychapterHistory> page, UserBulkbuychapterHistory userBulkbuychapterHistory) {
		return super.findPage(page, userBulkbuychapterHistory);
	}
	
	@Transactional(readOnly = false)
	public void save(UserBulkbuychapterHistory userBulkbuychapterHistory) {
		super.save(userBulkbuychapterHistory);
	}
	
	@Transactional(readOnly = false)
	public void delete(UserBulkbuychapterHistory userBulkbuychapterHistory) {
		super.delete(userBulkbuychapterHistory);
	}
	
	
	
	
}