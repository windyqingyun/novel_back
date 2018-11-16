/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bus.history.web;

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
import com.jeeplus.modules.bus.history.entity.UserClickHistory;
import com.jeeplus.modules.bus.history.service.UserClickHistoryService;

/**
 * 用户点击记录Controller
 * @author zhangsc
 * @version 2017-11-03
 */
@Controller
@RequestMapping(value = "${adminPath}/history/userClickHistory")
public class UserClickHistoryController extends BaseController {

	@Autowired
	private UserClickHistoryService userClickHistoryService;
	
	@ModelAttribute
	public UserClickHistory get(@RequestParam(required=false) String id) {
		UserClickHistory entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = userClickHistoryService.get(id);
		}
		if (entity == null){
			entity = new UserClickHistory();
		}
		return entity;
	}
	
	/**
	 * 用户点击记录列表页面
	 */
	@RequiresPermissions("history:userClickHistory:list")
	@RequestMapping(value = {"list", ""})
	public String list(UserClickHistory userClickHistory, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<UserClickHistory> page = userClickHistoryService.findPage(new Page<UserClickHistory>(request, response), userClickHistory); 
		model.addAttribute("page", page);
		return "bus/history/userClickHistoryList";
	}

	/**
	 * 查看，增加，编辑用户点击记录表单页面
	 */
	@RequiresPermissions(value={"history:userClickHistory:view","history:userClickHistory:add","history:userClickHistory:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(UserClickHistory userClickHistory, Model model) {
		model.addAttribute("userClickHistory", userClickHistory);
		return "bus/history/userClickHistoryForm";
	}

	/**
	 * 保存用户点击记录
	 */
	@RequiresPermissions(value={"history:userClickHistory:add","history:userClickHistory:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(UserClickHistory userClickHistory, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, userClickHistory)){
			return form(userClickHistory, model);
		}
		if(!userClickHistory.getIsNewRecord()){//编辑表单保存
			UserClickHistory t = userClickHistoryService.get(userClickHistory.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(userClickHistory, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			userClickHistoryService.save(t);//保存
		}else{//新增表单保存
			userClickHistoryService.save(userClickHistory);//保存
		}
		addMessage(redirectAttributes, "保存用户点击记录成功");
		return "redirect:"+Global.getAdminPath()+"/history/userClickHistory/?repage";
	}
	
	/**
	 * 删除用户点击记录
	 */
	@RequiresPermissions("history:userClickHistory:del")
	@RequestMapping(value = "delete")
	public String delete(UserClickHistory userClickHistory, RedirectAttributes redirectAttributes) {
		userClickHistoryService.delete(userClickHistory);
		addMessage(redirectAttributes, "删除用户点击记录成功");
		return "redirect:"+Global.getAdminPath()+"/history/userClickHistory/?repage";
	}
	
	/**
	 * 批量删除用户点击记录
	 */
	@RequiresPermissions("history:userClickHistory:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			userClickHistoryService.delete(userClickHistoryService.get(id));
		}
		addMessage(redirectAttributes, "删除用户点击记录成功");
		return "redirect:"+Global.getAdminPath()+"/history/userClickHistory/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("history:userClickHistory:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(UserClickHistory userClickHistory, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "用户点击记录"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<UserClickHistory> page = userClickHistoryService.findPage(new Page<UserClickHistory>(request, response, -1), userClickHistory);
    		new ExportExcel("用户点击记录", UserClickHistory.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出用户点击记录记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/history/userClickHistory/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("history:userClickHistory:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<UserClickHistory> list = ei.getDataList(UserClickHistory.class);
			for (UserClickHistory userClickHistory : list){
				try{
					userClickHistoryService.save(userClickHistory);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条用户点击记录记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条用户点击记录记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入用户点击记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/history/userClickHistory/?repage";
    }
	
	/**
	 * 下载导入用户点击记录数据模板
	 */
	@RequiresPermissions("history:userClickHistory:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "用户点击记录数据导入模板.xlsx";
    		List<UserClickHistory> list = Lists.newArrayList(); 
    		new ExportExcel("用户点击记录数据", UserClickHistory.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/history/userClickHistory/?repage";
    }
	
	
	

}