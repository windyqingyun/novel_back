/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bus.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.bus.entity.UserWallet;
import com.jeeplus.modules.bus.dao.UserWalletDao;

/**
 * 用户钱包Service
 * @author zhangsc
 * @version 2017-11-29
 */
@Service
@Transactional(readOnly = true)
public class UserWalletService extends CrudService<UserWalletDao, UserWallet> {

	public UserWallet get(String id) {
		return super.get(id);
	}
	
	public List<UserWallet> findList(UserWallet userWallet) {
		return super.findList(userWallet);
	}
	
	public Page<UserWallet> findPage(Page<UserWallet> page, UserWallet userWallet) {
		return super.findPage(page, userWallet);
	}
	
	@Transactional(readOnly = false)
	public void save(UserWallet userWallet) {
		super.save(userWallet);
	}
	
	@Transactional(readOnly = false)
	public void delete(UserWallet userWallet) {
		super.delete(userWallet);
	}
	
	
	
	
}