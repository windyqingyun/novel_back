/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bus.config.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.MyBeanUtils;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.modules.bus.config.entity.UserSubscibeConfig;
import com.jeeplus.modules.bus.config.service.UserSubscibeConfigService;

/**
 * 用户订阅配置Controller
 * @author zhangsc
 * @version 2017-11-03
 */
@Controller
@RequestMapping(value = "${adminPath}/config/userSubscibeConfig")
public class UserSubscibeConfigController extends BaseController {

	@Autowired
	private UserSubscibeConfigService userSubscibeConfigService;
	
	@ModelAttribute
	public UserSubscibeConfig get(@RequestParam(required=false) String id) {
		UserSubscibeConfig entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = userSubscibeConfigService.get(id);
		}
		if (entity == null){
			entity = new UserSubscibeConfig();
		}
		return entity;
	}
	
	/**
	 * 用户订阅配置列表页面
	 */
	@RequiresPermissions("config:userSubscibeConfig:list")
	@RequestMapping(value = {"list", ""})
	public String list(UserSubscibeConfig userSubscibeConfig, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<UserSubscibeConfig> page = userSubscibeConfigService.findPage(new Page<UserSubscibeConfig>(request, response), userSubscibeConfig); 
		model.addAttribute("page", page);
		return "bus/config/userSubscibeConfigList";
	}

	/**
	 * 查看，增加，编辑用户订阅配置表单页面
	 */
	@RequiresPermissions(value={"config:userSubscibeConfig:view","config:userSubscibeConfig:add","config:userSubscibeConfig:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(UserSubscibeConfig userSubscibeConfig, Model model) {
		model.addAttribute("userSubscibeConfig", userSubscibeConfig);
		return "bus/config/userSubscibeConfigForm";
	}

	/**
	 * 保存用户订阅配置
	 */
	@RequiresPermissions(value={"config:userSubscibeConfig:add","config:userSubscibeConfig:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(UserSubscibeConfig userSubscibeConfig, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, userSubscibeConfig)){
			return form(userSubscibeConfig, model);
		}
		if(!userSubscibeConfig.getIsNewRecord()){//编辑表单保存
			UserSubscibeConfig t = userSubscibeConfigService.get(userSubscibeConfig.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(userSubscibeConfig, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			userSubscibeConfigService.save(t);//保存
		}else{//新增表单保存
			userSubscibeConfigService.save(userSubscibeConfig);//保存
		}
		addMessage(redirectAttributes, "保存用户订阅配置成功");
		return "redirect:"+Global.getAdminPath()+"/config/userSubscibeConfig/?repage";
	}
	
	/**
	 * 删除用户订阅配置
	 */
	@RequiresPermissions("config:userSubscibeConfig:del")
	@RequestMapping(value = "delete")
	public String delete(UserSubscibeConfig userSubscibeConfig, RedirectAttributes redirectAttributes) {
		userSubscibeConfigService.delete(userSubscibeConfig);
		addMessage(redirectAttributes, "删除用户订阅配置成功");
		return "redirect:"+Global.getAdminPath()+"/config/userSubscibeConfig/?repage";
	}
	
	/**
	 * 批量删除用户订阅配置
	 */
	@RequiresPermissions("config:userSubscibeConfig:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			userSubscibeConfigService.delete(userSubscibeConfigService.get(id));
		}
		addMessage(redirectAttributes, "删除用户订阅配置成功");
		return "redirect:"+Global.getAdminPath()+"/config/userSubscibeConfig/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("config:userSubscibeConfig:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(UserSubscibeConfig userSubscibeConfig, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "用户订阅配置"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<UserSubscibeConfig> page = userSubscibeConfigService.findPage(new Page<UserSubscibeConfig>(request, response, -1), userSubscibeConfig);
    		new ExportExcel("用户订阅配置", UserSubscibeConfig.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出用户订阅配置记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/config/userSubscibeConfig/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("config:userSubscibeConfig:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<UserSubscibeConfig> list = ei.getDataList(UserSubscibeConfig.class);
			for (UserSubscibeConfig userSubscibeConfig : list){
				try{
					userSubscibeConfigService.save(userSubscibeConfig);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条用户订阅配置记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条用户订阅配置记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入用户订阅配置失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/config/userSubscibeConfig/?repage";
    }
	
	/**
	 * 下载导入用户订阅配置数据模板
	 */
	@RequiresPermissions("config:userSubscibeConfig:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "用户订阅配置数据导入模板.xlsx";
    		List<UserSubscibeConfig> list = Lists.newArrayList(); 
    		new ExportExcel("用户订阅配置数据", UserSubscibeConfig.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/config/userSubscibeConfig/?repage";
    }
	
	
	

}