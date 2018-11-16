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
import com.jeeplus.modules.bus.entity.OfficeCoinConfig;
import com.jeeplus.modules.bus.service.OfficeCoinConfigService;

/**
 * 机构货币配置Controller
 * @author zhangsc
 * @version 2017-11-15
 */
@Controller
@RequestMapping(value = "${adminPath}/bus/officeCoinConfig")
public class OfficeCoinConfigController extends BaseController {

	@Autowired
	private OfficeCoinConfigService officeCoinConfigService;
	
	@ModelAttribute
	public OfficeCoinConfig get(@RequestParam(required=false) String id) {
		OfficeCoinConfig entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = officeCoinConfigService.get(id);
		}
		if (entity == null){
			entity = new OfficeCoinConfig();
		}
		return entity;
	}
	
	/**
	 * 机构货币配置列表页面
	 */
	@RequiresPermissions("bus:officeCoinConfig:list")
	@RequestMapping(value = {"list", ""})
	public String list(OfficeCoinConfig officeCoinConfig, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<OfficeCoinConfig> page = officeCoinConfigService.findPage(new Page<OfficeCoinConfig>(request, response), officeCoinConfig); 
		model.addAttribute("page", page);
		return "modules/bus/officeCoinConfigList";
	}

	/**
	 * 查看，增加，编辑机构货币配置表单页面
	 */
	@RequiresPermissions(value={"bus:officeCoinConfig:view","bus:officeCoinConfig:add","bus:officeCoinConfig:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(OfficeCoinConfig officeCoinConfig, Model model) {
		model.addAttribute("officeCoinConfig", officeCoinConfig);
		return "modules/bus/officeCoinConfigForm";
	}

	/**
	 * 保存机构货币配置
	 */
	@RequiresPermissions(value={"bus:officeCoinConfig:add","bus:officeCoinConfig:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(OfficeCoinConfig officeCoinConfig, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, officeCoinConfig)){
			return form(officeCoinConfig, model);
		}
		if(!officeCoinConfig.getIsNewRecord()){//编辑表单保存
			OfficeCoinConfig t = officeCoinConfigService.get(officeCoinConfig.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(officeCoinConfig, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			officeCoinConfigService.save(t);//保存
		}else{//新增表单保存
			officeCoinConfigService.save(officeCoinConfig);//保存
		}
		addMessage(redirectAttributes, "保存机构货币配置成功");
		return "redirect:"+Global.getAdminPath()+"/bus/officeCoinConfig/?repage";
	}
	
	/**
	 * 删除机构货币配置
	 */
	@RequiresPermissions("bus:officeCoinConfig:del")
	@RequestMapping(value = "delete")
	public String delete(OfficeCoinConfig officeCoinConfig, RedirectAttributes redirectAttributes) {
		officeCoinConfigService.delete(officeCoinConfig);
		addMessage(redirectAttributes, "删除机构货币配置成功");
		return "redirect:"+Global.getAdminPath()+"/bus/officeCoinConfig/?repage";
	}
	
	/**
	 * 批量删除机构货币配置
	 */
	@RequiresPermissions("bus:officeCoinConfig:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			officeCoinConfigService.delete(officeCoinConfigService.get(id));
		}
		addMessage(redirectAttributes, "删除机构货币配置成功");
		return "redirect:"+Global.getAdminPath()+"/bus/officeCoinConfig/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("bus:officeCoinConfig:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(OfficeCoinConfig officeCoinConfig, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "机构货币配置"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<OfficeCoinConfig> page = officeCoinConfigService.findPage(new Page<OfficeCoinConfig>(request, response, -1), officeCoinConfig);
    		new ExportExcel("机构货币配置", OfficeCoinConfig.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出机构货币配置记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/bus/officeCoinConfig/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("bus:officeCoinConfig:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<OfficeCoinConfig> list = ei.getDataList(OfficeCoinConfig.class);
			for (OfficeCoinConfig officeCoinConfig : list){
				try{
					officeCoinConfigService.save(officeCoinConfig);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条机构货币配置记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条机构货币配置记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入机构货币配置失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/bus/officeCoinConfig/?repage";
    }
	
	/**
	 * 下载导入机构货币配置数据模板
	 */
	@RequiresPermissions("bus:officeCoinConfig:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "机构货币配置数据导入模板.xlsx";
    		List<OfficeCoinConfig> list = Lists.newArrayList(); 
    		new ExportExcel("机构货币配置数据", OfficeCoinConfig.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/bus/officeCoinConfig/?repage";
    }
	
	
	

}