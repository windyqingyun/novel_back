package com.jeeplus.modules.bus.config.utils;

import com.jeeplus.common.utils.SpringContextHolder;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.bus.entity.OfficeCoinConfig;
import com.jeeplus.modules.bus.service.OfficeCoinConfigService;

public class OfficeConfigUtils {
	private static OfficeCoinConfigService officeCoinConfigService = SpringContextHolder.getBean(OfficeCoinConfigService.class);
	
	/**
	 * 根据机构获取阅读币名称
	 * @param officeId 机构id
	 * @param defaultName  默认阅读币名称
	 * @return
	 */
	public static String getCoinNameByOffice(String officeId, String defaultName){
		OfficeCoinConfig config = officeCoinConfigService.getOfficeCoinConfigByOffice(officeId);
		if(config != null && StringUtils.isNotBlank(config.getCoinname())){
			return config.getCoinname();
		}
		return defaultName;
	}
	
	/**
	 * 根据机构名称获取阅读券名称
	 * @param officeId
	 * @param defaultName 默认的阅读券名称
	 * @return
	 */
	public static String getTicketNameByOffice(String officeId, String defaultName){
		OfficeCoinConfig config = officeCoinConfigService.getOfficeCoinConfigByOffice(officeId);
		if(config != null && StringUtils.isNotBlank(config.getTicketname())){
			return config.getTicketname();
		}
		return defaultName;
	}
}
