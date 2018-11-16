/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bus.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.bus.entity.ProviderUser;
import com.jeeplus.modules.bus.dao.ProviderUserDao;

/**
 * 流量提供端用户Service
 * @author zhangsc
 * @version 2017-12-22
 */
@Service
@Transactional(readOnly = true)
public class ProviderUserService extends CrudService<ProviderUserDao, ProviderUser> {

	public ProviderUser get(String id) {
		return super.get(id);
	}
	
	public List<ProviderUser> findList(ProviderUser providerUser) {
		return super.findList(providerUser);
	}
	
	public Page<ProviderUser> findPage(Page<ProviderUser> page, ProviderUser providerUser) {
		return super.findPage(page, providerUser);
	}
	
	@Transactional(readOnly = false)
	public void save(ProviderUser providerUser) {
		super.save(providerUser);
	}
	
	@Transactional(readOnly = false)
	public void delete(ProviderUser providerUser) {
		super.delete(providerUser);
	}
	
	/**
	 * 根据用户原id和机构id获取用户
	 * @param officeId
	 * @param originalId
	 * @return
	 */
	public ProviderUser getUserByOfficeAndOriginalId(String officeId, String originalId){
		ProviderUser searchBean = new ProviderUser(officeId, originalId);
		
		List<ProviderUser> list = findList(searchBean);
		if(list != null && list.size() > 0){
			return list.get(0);
		}
		return null;
	}
	
	/**
	 * 根据用户原id和机构id获取用户
	 * @param officeId
	 * @param originalId
	 * @return
	 */
	public String getUserIdByOfficeAndOriginalId(String officeId, String originalId){
		ProviderUser searchBean = new ProviderUser(officeId, originalId);
		
		List<ProviderUser> list = findList(searchBean);
		if(list != null && list.size() > 0){
			return list.get(0).getId();
		}
		return null;
	}
	
}