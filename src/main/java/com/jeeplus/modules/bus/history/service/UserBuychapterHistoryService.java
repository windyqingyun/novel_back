/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bus.history.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.bus.dto.StatisticsDto;
import com.jeeplus.modules.bus.entity.Book;
import com.jeeplus.modules.bus.enums.OfficeEnum;
import com.jeeplus.modules.bus.enums.RoleEnum;
import com.jeeplus.modules.bus.history.dao.UserBuychapterHistoryDao;
import com.jeeplus.modules.bus.history.entity.UserBuychapterHistory;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.service.OfficeService;
import com.jeeplus.modules.sys.utils.UserUtils;

/**
 * 用户章节购买记录Service
 * @author zhangsc
 * @version 2017-11-03
 */
@Service
@Transactional(readOnly = true)
public class UserBuychapterHistoryService extends CrudService<UserBuychapterHistoryDao, UserBuychapterHistory> {

	@Autowired
	private OfficeService officeService;
	
	public UserBuychapterHistory get(String id) {
		return super.get(id);
	}
	
	public List<UserBuychapterHistory> findList(UserBuychapterHistory userBuychapterHistory) {
		return super.findList(userBuychapterHistory);
	}
	
	public Page<UserBuychapterHistory> findPage(Page<UserBuychapterHistory> page, UserBuychapterHistory userBuychapterHistory) {
		return super.findPage(page, userBuychapterHistory);
	}
	
	@Transactional(readOnly = false)
	public void save(UserBuychapterHistory userBuychapterHistory) {
		super.save(userBuychapterHistory);
	}
	
	@Transactional(readOnly = false)
	public void delete(UserBuychapterHistory userBuychapterHistory) {
		super.delete(userBuychapterHistory);
	}
	
	/**
	 * 用户是否购买了该章节
	 * @param bookId
	 * @param chapter
	 * @return
	 */
	public Boolean isUserBuyBookChapter(String userId,String bookId, int chapter) {
		UserBuychapterHistory userBuychapterHistory = new UserBuychapterHistory();
		userBuychapterHistory.setCreateBy(new User(userId));
		userBuychapterHistory.setBook(new Book(bookId));
		userBuychapterHistory.setChapter(chapter);
		
		List<UserBuychapterHistory> buyChapterHistoryList = findList(userBuychapterHistory);
		if(buyChapterHistoryList != null && !buyChapterHistoryList.isEmpty()){
			return true;
		}
		return false;
	}
	
	
	/**
	 * 统计不同作品的收入
	 * @param statistics
	 * @return
	 */
	public Page<StatisticsDto> countRevenueByBook(Page<StatisticsDto> page, StatisticsDto statistics){
		// 如果不是超级管理员，过滤数据
		if(!UserUtils.getSubject().hasRole(RoleEnum.ADMIN.getEnname())){
			if(UserUtils.getSubject().hasRole(RoleEnum.FLOW_ADMIN.getEnname())) {
				statistics.setOriginOffice(statistics.getCurrentUser().getOffice());
			}else if(UserUtils.getSubject().hasRole(RoleEnum.CONTENT_PROVIDE.getEnname())) {
				statistics.setProviderOffice(statistics.getCurrentUser().getOffice());
			}
		}
		
		Office o = statistics.getOffice();
		Map<String, Office> map = officeService.getAllIdListFromCache();
		if(statistics != null && statistics.getOffice() != null) {
			Office office = map.get(statistics.getOffice().getId());
			String parentId = null;
			if (office != null) {
				parentId = office.getParentId();
			}
			if(StringUtils.equals(parentId, OfficeEnum.ORIGIN_OFFICE.getCode())) {
				statistics.setChannelOffice(o);
				statistics.setOffice(null);
			}else if(StringUtils.equals(parentId, OfficeEnum.PROVIDE_OFFICE.getCode())) {
				
			}
		}
//		return dao.countRevenueBySupply(statistics);
		statistics.setPage(page);
		page.setList(dao.countRevenueByBookPlus(statistics));
		
		statistics.setOffice(o);
		return page;
	}
	
}