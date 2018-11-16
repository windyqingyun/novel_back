/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bus.history.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.bus.history.entity.UserReadHistory;
import com.jeeplus.modules.bus.history.dao.UserReadHistoryDao;

/**
 * 用户阅读记录Service
 * @author zhangsc
 * @version 2017-11-03
 */
@Service
@Transactional(readOnly = true)
public class UserReadHistoryService extends CrudService<UserReadHistoryDao, UserReadHistory> {

	public UserReadHistory get(String id) {
		return super.get(id);
	}
	
	public List<UserReadHistory> findList(UserReadHistory userReadHistory) {
		return super.findList(userReadHistory);
	}
	
	public Page<UserReadHistory> findPage(Page<UserReadHistory> page, UserReadHistory userReadHistory) {
		return super.findPage(page, userReadHistory);
	}
	
	@Transactional(readOnly = false)
	public void save(UserReadHistory userReadHistory) {
		super.save(userReadHistory);
	}
	
	@Transactional(readOnly = false)
	public void delete(UserReadHistory userReadHistory) {
		super.delete(userReadHistory);
	}
	
	
	
	
}