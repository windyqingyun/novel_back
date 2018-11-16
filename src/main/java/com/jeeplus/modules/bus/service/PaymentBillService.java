/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bus.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.bus.dao.PaymentBillDao;
import com.jeeplus.modules.bus.dto.StatisticsDto;
import com.jeeplus.modules.bus.entity.PaymentBill;
import com.jeeplus.modules.bus.enums.OfficeEnum;
import com.jeeplus.modules.bus.enums.RoleEnum;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.service.OfficeService;
import com.jeeplus.modules.sys.utils.UserUtils;

/**
 * 支付单Service
 * @author zhangsc
 * @version 2017-11-03
 */
@Service
@Transactional(readOnly = true)
public class PaymentBillService extends CrudService<PaymentBillDao, PaymentBill> {
	
	@Autowired
	private OfficeService officeService;

	public PaymentBill get(String id) {
		return super.get(id);
	}
	
	public List<PaymentBill> findList(PaymentBill paymentBill) {
		return super.findList(paymentBill);
	}
	
	public Page<PaymentBill> findPage(Page<PaymentBill> page, PaymentBill paymentBill) {
		return super.findPage(page, paymentBill);
	}
	
	@Transactional(readOnly = false)
	public void save(PaymentBill paymentBill) {
		super.save(paymentBill);
	}
	
	@Transactional(readOnly = false)
	public void delete(PaymentBill paymentBill) {
		super.delete(paymentBill);
	}
	
	/**
	 * 总收入
	 * @param statistics
	 * @return
	 */
	public List<StatisticsDto> getSumRevenue(StatisticsDto statistics){
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
		List<StatisticsDto> sumRevenuePlus = dao.getSumRevenuePlus(statistics);
		statistics.setOffice(o);
		return sumRevenuePlus;
	}
	
	/**
	 * 内容提供商收入
	 * @param statistics
	 * @return
	 */
	public List<StatisticsDto> countRevenueBySupply(StatisticsDto statistics){
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
		List<StatisticsDto> countRevenueBySupplyPlus = dao.countRevenueBySupplyPlus(statistics);
		statistics.setOffice(o);
		return countRevenueBySupplyPlus;
	}
	
	/**
	 * 渠道收入
	 * @param statistics
	 * @return
	 */
	public List<StatisticsDto> coutRevenueByChannel(StatisticsDto statistics){
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
		List<StatisticsDto> countRevenueByChannelPlus = dao.countRevenueByChannelPlus(statistics);
		statistics.setOffice(o);
		return countRevenueByChannelPlus;
	}
}