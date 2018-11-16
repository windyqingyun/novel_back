package com.jeeplus.modules.bus.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.common.utils.CacheUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.bus.dao.OfficeCoinConfigDao;
import com.jeeplus.modules.bus.entity.OfficeCoinConfig;

/**
 * 机构货币配置Service
 * @author zhangsc
 * @version 2017-11-15
 */
@Service
@Transactional(readOnly = true)
public class OfficeCoinConfigService extends CrudService<OfficeCoinConfigDao, OfficeCoinConfig> {
	/** 机构接口配置缓存名称 */
	public static final String CACHE_OFFICE_COIN_CONFIG = "office_interface_config";
	/** 接口接口配置键值  与结构id拼接作为key获取机构接口配置 */
	public static final String OFFICE_COIN_CONFIG_KEY = "cid";
	
	/**
	 * 根据机构id获取机构货币配置
	 * @param officeId
	 * @return
	 */
	public OfficeCoinConfig getOfficeCoinConfigByOffice(String officeId){
		OfficeCoinConfig officeCoinConfig = (OfficeCoinConfig) CacheUtils.get(CACHE_OFFICE_COIN_CONFIG, OFFICE_COIN_CONFIG_KEY+officeId);
		if(officeCoinConfig == null){
			officeCoinConfig = dao.getOfficeCoinConfigByOffice(officeId);
			if(officeCoinConfig == null || StringUtils.isBlank(officeCoinConfig.getId())) {
				return null;
			}
			CacheUtils.put(CACHE_OFFICE_COIN_CONFIG, OFFICE_COIN_CONFIG_KEY+officeCoinConfig.getOffice().getId(), officeCoinConfig);
		}
		
		return officeCoinConfig;
	}
	
	public OfficeCoinConfig get(String id) {
		return super.get(id);
	}
	
	public List<OfficeCoinConfig> findList(OfficeCoinConfig officeCoinConfig) {
		dataScopeFilterByOffice(officeCoinConfig, "dsf", null, null);
		return super.findList(officeCoinConfig);
	}
	
	public Page<OfficeCoinConfig> findPage(Page<OfficeCoinConfig> page, OfficeCoinConfig officeCoinConfig) {
		dataScopeFilterByOffice(officeCoinConfig, "dsf", null, null);
		return super.findPage(page, officeCoinConfig);
	}
	
	@Transactional(readOnly = false)
	public void save(OfficeCoinConfig officeCoinConfig) {
		super.save(officeCoinConfig);
		//如果是修改操作，移除缓存
		if(!officeCoinConfig.getIsNewRecord()){
			CacheUtils.remove(CACHE_OFFICE_COIN_CONFIG, OFFICE_COIN_CONFIG_KEY+officeCoinConfig.getOffice().getId());
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(OfficeCoinConfig officeCoinConfig) {
		super.delete(officeCoinConfig);
		CacheUtils.removeByCacheName(CACHE_OFFICE_COIN_CONFIG);
	}
	
}