/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bus.config.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.bus.config.entity.UserSubscibeConfig;
import com.jeeplus.modules.bus.config.dao.UserSubscibeConfigDao;

/**
 * 用户订阅配置Service
 * @author zhangsc
 * @version 2017-11-03
 */
@Service
@Transactional(readOnly = true)
public class UserSubscibeConfigService extends CrudService<UserSubscibeConfigDao, UserSubscibeConfig> {

	public UserSubscibeConfig get(String id) {
		return super.get(id);
	}
	
	public List<UserSubscibeConfig> findList(UserSubscibeConfig userSubscibeConfig) {
		return super.findList(userSubscibeConfig);
	}
	
	public Page<UserSubscibeConfig> findPage(Page<UserSubscibeConfig> page, UserSubscibeConfig userSubscibeConfig) {
		return super.findPage(page, userSubscibeConfig);
	}
	
	@Transactional(readOnly = false)
	public void save(UserSubscibeConfig userSubscibeConfig) {
		super.save(userSubscibeConfig);
	}
	
	@Transactional(readOnly = false)
	public void delete(UserSubscibeConfig userSubscibeConfig) {
		super.delete(userSubscibeConfig);
	}
	
	
	
	
}