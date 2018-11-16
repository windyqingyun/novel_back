/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bus.history.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.bus.history.entity.UserFavorites;
import com.jeeplus.modules.bus.history.dao.UserFavoritesDao;

/**
 * 用户收藏记录Service
 * @author zhangsc
 * @version 2017-11-03
 */
@Service
@Transactional(readOnly = true)
public class UserFavoritesService extends CrudService<UserFavoritesDao, UserFavorites> {

	public UserFavorites get(String id) {
		return super.get(id);
	}
	
	public List<UserFavorites> findList(UserFavorites userFavorites) {
		return super.findList(userFavorites);
	}
	
	public Page<UserFavorites> findPage(Page<UserFavorites> page, UserFavorites userFavorites) {
		return super.findPage(page, userFavorites);
	}
	
	@Transactional(readOnly = false)
	public void save(UserFavorites userFavorites) {
		super.save(userFavorites);
	}
	
	@Transactional(readOnly = false)
	public void delete(UserFavorites userFavorites) {
		super.delete(userFavorites);
	}
	
	
	
	
}