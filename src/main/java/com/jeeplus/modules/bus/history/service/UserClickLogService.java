package com.jeeplus.modules.bus.history.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.bus.dto.BookClickDto;
import com.jeeplus.modules.bus.enums.GroupTypeEnum;
import com.jeeplus.modules.bus.enums.RoleEnum;
import com.jeeplus.modules.bus.form.StatisticsForm;
import com.jeeplus.modules.bus.history.dao.UserClickLogDao;
import com.jeeplus.modules.bus.history.entity.UserClickLog;
import com.jeeplus.modules.bus.history.entity.example.UserClickLogExample;
import com.jeeplus.modules.sys.utils.UserUtils;

@Service
@Transactional(readOnly = true)
public class UserClickLogService {
	
	@Autowired
	private UserClickLogDao userClickLogDao;

	public int countByExample(UserClickLogExample example) {
		return userClickLogDao.countByExample(example);
	}

//	@Transactional(readOnly = false)
	public int deleteByExample(UserClickLogExample example) {
		return userClickLogDao.deleteByExample(example);
	}

//	@Transactional(readOnly = false)
	public int deleteByPrimaryKey(Long id) {
		return userClickLogDao.deleteByPrimaryKey(id);
	}

	@Transactional(readOnly = false)
	public int insert(UserClickLog record) {
		return userClickLogDao.insert(record);
	}

	@Transactional(readOnly = false)
	public int insertSelective(UserClickLog record) {
		return userClickLogDao.insertSelective(record);
	}

	
	public List<UserClickLog> selectByExample(UserClickLogExample example) {
		return userClickLogDao.selectByExample(example);
	}

	
	public UserClickLog selectByPrimaryKey(Long id) {
		return userClickLogDao.selectByPrimaryKey(id);
	}

	@Transactional(readOnly = false)
	public int updateByExampleSelective(UserClickLog record, UserClickLogExample example) {
		return userClickLogDao.updateByExampleSelective(record, example);
	}

	@Transactional(readOnly = false)
	public int updateByExample(UserClickLog record, UserClickLogExample example) {
		return userClickLogDao.updateByExample(record, example);
	}

	@Transactional(readOnly = false)
	public int updateByPrimaryKeySelective(UserClickLog record) {
		return userClickLogDao.updateByPrimaryKeySelective(record);
	}

	@Transactional(readOnly = false)
	public int updateByPrimaryKey(UserClickLog record) {
		return userClickLogDao.updateByPrimaryKey(record);
	}

	@Transactional(readOnly = false)
	public Page<BookClickDto> findClikCountPage(Page<BookClickDto> page, StatisticsForm statisticsForm) {
		// 如果不是超级管理员，过滤数据
		if(!UserUtils.getSubject().hasRole(RoleEnum.ADMIN.getEnname())){
			if(UserUtils.getSubject().hasRole(RoleEnum.FLOW_ADMIN.getEnname())) {
				statisticsForm.setChannelOffice(UserUtils.getOffce());
			}else if(UserUtils.getSubject().hasRole(RoleEnum.CONTENT_PROVIDE.getEnname())) {
				statisticsForm.setOffice(UserUtils.getOffce());
			}
		}
		
		// 分组查询
		if (StringUtils.equals(GroupTypeEnum.ALL_DATA.getCode(), statisticsForm.getGroupType())){
			statisticsForm.setGroupBy("l.id");
		}else if (StringUtils.equals(GroupTypeEnum.BY_USER.getCode(), statisticsForm.getGroupType())){
			statisticsForm.setGroupBy("c.id");
		}else if (StringUtils.equals(GroupTypeEnum.BY_RESOURCES.getCode(), statisticsForm.getGroupType())){
			statisticsForm.setGroupBy("b.id");
		}else {
			statisticsForm.setGroupBy("l.id");
		}

		if(statisticsForm.getStartRow() == null) {
			statisticsForm.setPageNo(page.getPageNo());
			statisticsForm.setPageSize(page.getPageSize());
		}
		page.setCount(userClickLogDao.findClikCountByCount(statisticsForm));
		page.setList(userClickLogDao.findClikCount(statisticsForm));
		return page;
	}
}
