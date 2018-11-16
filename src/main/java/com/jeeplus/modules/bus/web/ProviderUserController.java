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
import com.jeeplus.modules.bus.entity.ProviderUser;
import com.jeeplus.modules.bus.service.ProviderUserService;

/**
 * 流量提供端用户Controller
 * @author zhangsc
 * @version 2017-12-22
 */
@Controller
@RequestMapping(value = "${adminPath}/bus/providerUser")
public class ProviderUserController extends BaseController {

	@Autowired
	private ProviderUserService providerUserService;
	
	@ModelAttribute
	public ProviderUser get(@RequestParam(required=false) String id) {
		ProviderUser entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = providerUserService.get(id);
		}
		if (entity == null){
			entity = new ProviderUser();
		}
		return entity;
	}
	
	/**
	 * 流量提供端用户列表页面
	 */
	@RequiresPermissions("bus:providerUser:list")
	@RequestMapping(value = {"list", ""})
	public String list(ProviderUser providerUser, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ProviderUser> page = providerUserService.findPage(new Page<ProviderUser>(request, response), providerUser); 
		model.addAttribute("page", page);
		return "modules/bus/providerUserList";
	}

	/**
	 * 查看，增加，编辑流量提供端用户表单页面
	 */
	@RequiresPermissions(value={"bus:providerUser:view","bus:providerUser:add","bus:providerUser:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(ProviderUser providerUser, Model model) {
		model.addAttribute("providerUser", providerUser);
		return "modules/bus/providerUserForm";
	}

	/**
	 * 保存流量提供端用户
	 */
	@RequiresPermissions(value={"bus:providerUser:add","bus:providerUser:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(ProviderUser providerUser, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, providerUser)){
			return form(providerUser, model);
		}
		if(!providerUser.getIsNewRecord()){//编辑表单保存
			ProviderUser t = providerUserService.get(providerUser.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(providerUser, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			providerUserService.save(t);//保存
		}else{//新增表单保存
			providerUserService.save(providerUser);//保存
		}
		addMessage(redirectAttributes, "保存流量提供端用户成功");
		return "redirect:"+Global.getAdminPath()+"/bus/providerUser/?repage";
	}
	
	/**
	 * 删除流量提供端用户
	 */
	@RequiresPermissions("bus:providerUser:del")
	@RequestMapping(value = "delete")
	public String delete(ProviderUser providerUser, RedirectAttributes redirectAttributes) {
		providerUserService.delete(providerUser);
		addMessage(redirectAttributes, "删除流量提供端用户成功");
		return "redirect:"+Global.getAdminPath()+"/bus/providerUser/?repage";
	}
	
	/**
	 * 批量删除流量提供端用户
	 */
	@RequiresPermissions("bus:providerUser:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			providerUserService.delete(providerUserService.get(id));
		}
		addMessage(redirectAttributes, "删除流量提供端用户成功");
		return "redirect:"+Global.getAdminPath()+"/bus/providerUser/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("bus:providerUser:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(ProviderUser providerUser, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "流量提供端用户"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<ProviderUser> page = providerUserService.findPage(new Page<ProviderUser>(request, response, -1), providerUser);
    		new ExportExcel("流量提供端用户", ProviderUser.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出流量提供端用户记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/bus/providerUser/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("bus:providerUser:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<ProviderUser> list = ei.getDataList(ProviderUser.class);
			for (ProviderUser providerUser : list){
				try{
					providerUserService.save(providerUser);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条流量提供端用户记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条流量提供端用户记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入流量提供端用户失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/bus/providerUser/?repage";
    }
	
	/**
	 * 下载导入流量提供端用户数据模板
	 */
	@RequiresPermissions("bus:providerUser:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "流量提供端用户数据导入模板.xlsx";
    		List<ProviderUser> list = Lists.newArrayList(); 
    		new ExportExcel("流量提供端用户数据", ProviderUser.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/bus/providerUser/?repage";
    }
}