/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bus.web;

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
import com.jeeplus.modules.bus.entity.OfficeInterfaceConfig;
import com.jeeplus.modules.bus.service.OfficeInterfaceConfigService;

/**
 * 机构接口配置Controller
 * @author zhangsc
 * @version 2017-11-07
 */
@Controller
@RequestMapping(value = "${adminPath}/bus/officeInterfaceConfig")
public class OfficeInterfaceConfigController extends BaseController {

	@Autowired
	private OfficeInterfaceConfigService officeInterfaceConfigService;
	
	@ModelAttribute
	public OfficeInterfaceConfig get(@RequestParam(required=false) String id) {
		OfficeInterfaceConfig entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = officeInterfaceConfigService.get(id);
		}
		if (entity == null){
			entity = new OfficeInterfaceConfig();
		}
		return entity;
	}
	
	/**
	 * 机构接口配置列表页面
	 */
	@RequiresPermissions("bus:officeInterfaceConfig:list")
	@RequestMapping(value = {"list", ""})
	public String list(OfficeInterfaceConfig officeInterfaceConfig, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<OfficeInterfaceConfig> page = officeInterfaceConfigService.findPage(new Page<OfficeInterfaceConfig>(request, response), officeInterfaceConfig); 
		model.addAttribute("page", page);
		return "modules/bus/officeInterfaceConfigList";
	}

	/**
	 * 查看，增加，编辑机构接口配置表单页面
	 */
	@RequiresPermissions(value={"bus:officeInterfaceConfig:view","bus:officeInterfaceConfig:add","bus:officeInterfaceConfig:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(OfficeInterfaceConfig officeInterfaceConfig, Model model) {
		model.addAttribute("officeInterfaceConfig", officeInterfaceConfig);
		return "modules/bus/officeInterfaceConfigForm";
	}

	/**
	 * 保存机构接口配置
	 */
	@RequiresPermissions(value={"bus:officeInterfaceConfig:add","bus:officeInterfaceConfig:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(OfficeInterfaceConfig officeInterfaceConfig, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, officeInterfaceConfig)){
			return form(officeInterfaceConfig, model);
		}
		if(!officeInterfaceConfig.getIsNewRecord()){//编辑表单保存
			OfficeInterfaceConfig t = officeInterfaceConfigService.get(officeInterfaceConfig.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(officeInterfaceConfig, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			officeInterfaceConfigService.save(t);//保存
		}else{//新增表单保存
			officeInterfaceConfigService.save(officeInterfaceConfig);//保存
		}
		addMessage(redirectAttributes, "保存机构接口配置成功");
		return "redirect:"+Global.getAdminPath()+"/bus/officeInterfaceConfig/?repage";
	}
	
	/**
	 * 删除机构接口配置
	 */
	@RequiresPermissions("bus:officeInterfaceConfig:del")
	@RequestMapping(value = "delete")
	public String delete(OfficeInterfaceConfig officeInterfaceConfig, RedirectAttributes redirectAttributes) {
		officeInterfaceConfigService.delete(officeInterfaceConfig);
		addMessage(redirectAttributes, "删除机构接口配置成功");
		return "redirect:"+Global.getAdminPath()+"/bus/officeInterfaceConfig/?repage";
	}
	
	/**
	 * 批量删除机构接口配置
	 */
	@RequiresPermissions("bus:officeInterfaceConfig:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			officeInterfaceConfigService.delete(officeInterfaceConfigService.get(id));
		}
		addMessage(redirectAttributes, "删除机构接口配置成功");
		return "redirect:"+Global.getAdminPath()+"/bus/officeInterfaceConfig/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("bus:officeInterfaceConfig:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(OfficeInterfaceConfig officeInterfaceConfig, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "机构接口配置"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<OfficeInterfaceConfig> page = officeInterfaceConfigService.findPage(new Page<OfficeInterfaceConfig>(request, response, -1), officeInterfaceConfig);
    		new ExportExcel("机构接口配置", OfficeInterfaceConfig.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出机构接口配置记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/bus/officeInterfaceConfig/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("bus:officeInterfaceConfig:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<OfficeInterfaceConfig> list = ei.getDataList(OfficeInterfaceConfig.class);
			for (OfficeInterfaceConfig officeInterfaceConfig : list){
				try{
					officeInterfaceConfigService.save(officeInterfaceConfig);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条机构接口配置记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条机构接口配置记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入机构接口配置失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/bus/officeInterfaceConfig/?repage";
    }
	
	/**
	 * 下载导入机构接口配置数据模板
	 */
	@RequiresPermissions("bus:officeInterfaceConfig:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "机构接口配置数据导入模板.xlsx";
    		List<OfficeInterfaceConfig> list = Lists.newArrayList(); 
    		new ExportExcel("机构接口配置数据", OfficeInterfaceConfig.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/bus/officeInterfaceConfig/?repage";
    }
	
	
	

}