/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bus.dao;

import java.util.List;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.bus.dto.StatisticsDto;
import com.jeeplus.modules.bus.entity.PaymentBill;

/**
 * 支付单DAO接口
 * @author zhangsc
 * @version 2017-11-03
 */
@MyBatisDao
public interface PaymentBillDao extends CrudDao<PaymentBill> {
	/**
	 * 根据各自的officeid 查询总收入(已废弃)
	 * @param statistics
	 * @return
	 */
	public List<StatisticsDto> getSumRevenue(StatisticsDto statistics);
	
	/**
	 * 根据各自的officeid 查询总收入
	 * @param statistics
	 * @return
	 */
	public List<StatisticsDto> getSumRevenuePlus(StatisticsDto statistics);
	
	
	/**
	 * 内容提供端收入(已废弃)
	 * @param statistics
	 * @return
	 */
	public List<StatisticsDto> countRevenueBySupply(StatisticsDto statistics);
	
	/**
	 * 根据officeid 查询各自的收入 显示提供端收入
	 * @param statistics
	 * @return
	 */
	public List<StatisticsDto> countRevenueBySupplyPlus(StatisticsDto statistics);
	
	/**
	 * 渠道收入 (已废弃)
	 * @param statistics
	 * @return
	 */
	public List<StatisticsDto> coutRevenueByChannel(StatisticsDto statistics);
	
	/**
	 * 根据officeid 查询各自的收入 显示流量端收入
	 * @param statistics
	 * @return
	 */
	public List<StatisticsDto> countRevenueByChannelPlus(StatisticsDto statistics);

}