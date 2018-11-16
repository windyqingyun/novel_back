/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bus.history.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.bus.dto.FodderClickDto;
import com.jeeplus.modules.bus.enums.GroupTypeEnum;
import com.jeeplus.modules.bus.history.dao.UserClickHistoryDao;
import com.jeeplus.modules.bus.history.entity.UserClickHistory;
import com.jeeplus.modules.sys.utils.UserUtils;

/**
 * 用户点击记录Service
 * @author zhangsc
 * @version 2017-11-03
 */
@Service
@Transactional(readOnly = true)
public class UserClickHistoryService extends CrudService<UserClickHistoryDao, UserClickHistory> {

	public UserClickHistory get(String id) {
		return super.get(id);
	}
	
	public List<UserClickHistory> findList(UserClickHistory userClickHistory) {
		return super.findList(userClickHistory);
	}
	
	public Page<UserClickHistory> findPage(Page<UserClickHistory> page, UserClickHistory userClickHistory) {
		return super.findPage(page, userClickHistory);
	}
	
	@Transactional(readOnly = false)
	public void save(UserClickHistory userClickHistory) {
		super.save(userClickHistory);
	}
	
	@Transactional(readOnly = false)
	public void delete(UserClickHistory userClickHistory) {
		super.delete(userClickHistory);
	}
	
	public Page<FodderClickDto> findClikCountPage(Page<FodderClickDto> page, FodderClickDto fodderClick) {
		// 如果不是超级管理员，过滤数据
		if(!UserUtils.getSubject().hasRole("system")){
			fodderClick.setFodderOfficeId(fodderClick.getCurrentUser().getOffice().getId());
		}
		
		
		fodderClick.setPage(page);
		
		List<FodderClickDto> list = Lists.newArrayList();
		String groupType = fodderClick.getGroupType();

		if(groupType.equals(GroupTypeEnum.ALL_DATA.getCode())){
			list = dao.findClikCountList(fodderClick);
		}else if(groupType.equals(GroupTypeEnum.BY_USER.getCode())){
			list = dao.findClikCountListGroupByUser(fodderClick);
		}else if(groupType.equals(GroupTypeEnum.BY_RESOURCES.getCode())){
			list = dao.findClikCountListGroupByFodder(fodderClick);
		}
		
		page.setList(list);
		return page;
	}
	
	
}