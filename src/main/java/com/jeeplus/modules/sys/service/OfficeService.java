/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sys.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.service.TreeService;
import com.jeeplus.common.utils.CacheUtils;
import com.jeeplus.modules.sys.dao.OfficeDao;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.utils.UserUtils;

/**
 * 机构Service
 * @author jeeplus
 * @version 2014-05-16
 */
@Service
@Transactional(readOnly = true)
public class OfficeService extends TreeService<OfficeDao, Office> {

	/** 内容提供端口的机构id列表  只放已启用的 **/
	public static final String CACHE_PROVIDER_OFFICE_IDS = "cache_provider_office_ids";
	
	/**  所有officeid **/
	public static final String CACHE_OFFICE_IDS = "cache_office_ids";
	
	public List<Office> findAll(){
		return UserUtils.getOfficeList();
	}

	public List<Office> findList(Boolean isAll){
		if (isAll != null && isAll){
			return UserUtils.getOfficeAllList();
		}else{
			return UserUtils.getOfficeList();
		}
	}
	
	@Transactional(readOnly = true)
	public List<Office> findList(Office office){
		office.setParentIds(office.getParentIds()+"%");
		return dao.findByParentIdsLike(office);
	}
	
	@Transactional(readOnly = true)
	public Office getByCode(String code){
		return dao.getByCode(code);
	}
	
	
	@Transactional(readOnly = false)
	public void save(Office office) {
		super.save(office);
		UserUtils.removeCache(UserUtils.CACHE_OFFICE_LIST);
		CacheUtils.remove(CACHE_PROVIDER_OFFICE_IDS);
		CacheUtils.remove(CACHE_OFFICE_IDS);
	}
	
	@Transactional(readOnly = false)
	public void delete(Office office) {
		super.delete(office);
		UserUtils.removeCache(UserUtils.CACHE_OFFICE_LIST);
		CacheUtils.remove(CACHE_PROVIDER_OFFICE_IDS);
		CacheUtils.remove(CACHE_OFFICE_IDS);
	}
	
	
	public Map<String, Office> getAllIdListFromCache() {
		Map<String, Office> map = (Map<String, Office>) CacheUtils.get(CACHE_OFFICE_IDS);
		if(map == null) {
			map = dao.findIdsAndParent();
			if(map != null && MapUtils.isNotEmpty(map)){
				CacheUtils.put(CACHE_OFFICE_IDS, map);
			}
		}
		return map;
	}
	
}
