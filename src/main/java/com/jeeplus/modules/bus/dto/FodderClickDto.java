package com.jeeplus.modules.bus.dto;

import java.util.Date;

import com.jeeplus.common.persistence.BaseEntity;
import com.jeeplus.modules.bus.entity.Fodder;
import com.jeeplus.modules.bus.entity.ProviderUser;

/**
 * 素材点击统计Dto
 * @author Administrator
 */
public class FodderClickDto extends BaseEntity<FodderClickDto> {
	
	private static final long serialVersionUID = 1L;

	private Fodder fodder;
	private ProviderUser providerUser;
	private Integer count;
	private String groupType;  //分组类型(01:全部数据， 02:按照用户分组， 03:按照素材分组 )
	
	private String userOfficeId;    //用户机构id
	private String userOfficeName;  //用户机构名称
	private String fodderOfficeId;    //素材机构id
	private String fodderOfficeName;  //素材机构名称
	
	private Date createDate;         //创建时间
	
	private Date beginDate;
	private Date endDate;
	
	public Fodder getFodder() {
		return fodder;
	}
	public void setFodder(Fodder fodder) {
		this.fodder = fodder;
	}
	public ProviderUser getProviderUser() {
		return providerUser;
	}
	public void setProviderUser(ProviderUser providerUser) {
		this.providerUser = providerUser;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public Date getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getGroupType() {
		return groupType;
	}
	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}
	public String getUserOfficeName() {
		return userOfficeName;
	}
	public void setUserOfficeName(String userOfficeName) {
		this.userOfficeName = userOfficeName;
	}
	public String getFodderOfficeName() {
		return fodderOfficeName;
	}
	public void setFodderOfficeName(String fodderOfficeName) {
		this.fodderOfficeName = fodderOfficeName;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getUserOfficeId() {
		return userOfficeId;
	}
	public void setUserOfficeId(String userOfficeId) {
		this.userOfficeId = userOfficeId;
	}
	public String getFodderOfficeId() {
		return fodderOfficeId;
	}
	public void setFodderOfficeId(String fodderOfficeId) {
		this.fodderOfficeId = fodderOfficeId;
	}
	@Override
	public void preInsert() {
		
	}
	@Override
	public void preUpdate() {
		
	}
}
