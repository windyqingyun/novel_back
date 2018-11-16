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
import com.jeeplus.modules.bus.history.entity.UserBuychapterHistory;
import com.jeeplus.modules.bus.history.service.UserBuychapterHistoryService;

/**
 * 章节购买Controller
 * @author zhangsc
 * @version 2017-12-05
 */
@Controller
@RequestMapping(value = "${adminPath}/history/userBuychapterHistory")
public class UserBuychapterHistoryController extends BaseController {

	@Autowired
	private UserBuychapterHistoryService userBuychapterHistoryService;
	
	@ModelAttribute
	public UserBuychapterHistory get(@RequestParam(required=false) String id) {
		UserBuychapterHistory entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = userBuychapterHistoryService.get(id);
		}
		if (entity == null){
			entity = new UserBuychapterHistory();
		}
		return entity;
	}
	
	/**
	 * 章节购买列表页面
	 */
	@RequiresPermissions("history:userBuychapterHistory:list")
	@RequestMapping(value = {"list", ""})
	public String list(UserBuychapterHistory userBuychapterHistory, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<UserBuychapterHistory> page = userBuychapterHistoryService.findPage(new Page<UserBuychapterHistory>(request, response), userBuychapterHistory); 
		model.addAttribute("page", page);
		return "modules/bus/history/userBuychapterHistoryList";
	}

	/**
	 * 查看，增加，编辑章节购买表单页面
	 */
	@RequiresPermissions(value={"history:userBuychapterHistory:view","history:userBuychapterHistory:add","history:userBuychapterHistory:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(UserBuychapterHistory userBuychapterHistory, Model model) {
		model.addAttribute("userBuychapterHistory", userBuychapterHistory);
		return "modules/bus/history/userBuychapterHistoryForm";
	}

	/**
	 * 保存章节购买
	 */
	@RequiresPermissions(value={"history:userBuychapterHistory:add","history:userBuychapterHistory:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(UserBuychapterHistory userBuychapterHistory, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, userBuychapterHistory)){
			return form(userBuychapterHistory, model);
		}
		if(!userBuychapterHistory.getIsNewRecord()){//编辑表单保存
			UserBuychapterHistory t = userBuychapterHistoryService.get(userBuychapterHistory.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(userBuychapterHistory, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			userBuychapterHistoryService.save(t);//保存
		}else{//新增表单保存
			userBuychapterHistoryService.save(userBuychapterHistory);//保存
		}
		addMessage(redirectAttributes, "保存章节购买成功");
		return "redirect:"+Global.getAdminPath()+"/history/userBuychapterHistory/?repage";
	}
	
	/**
	 * 删除章节购买
	 */
	@RequiresPermissions("history:userBuychapterHistory:del")
	@RequestMapping(value = "delete")
	public String delete(UserBuychapterHistory userBuychapterHistory, RedirectAttributes redirectAttributes) {
		userBuychapterHistoryService.delete(userBuychapterHistory);
		addMessage(redirectAttributes, "删除章节购买成功");
		return "redirect:"+Global.getAdminPath()+"/history/userBuychapterHistory/?repage";
	}
	
	/**
	 * 批量删除章节购买
	 */
	@RequiresPermissions("history:userBuychapterHistory:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			userBuychapterHistoryService.delete(userBuychapterHistoryService.get(id));
		}
		addMessage(redirectAttributes, "删除章节购买成功");
		return "redirect:"+Global.getAdminPath()+"/history/userBuychapterHistory/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("history:userBuychapterHistory:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(UserBuychapterHistory userBuychapterHistory, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "章节购买"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<UserBuychapterHistory> page = userBuychapterHistoryService.findPage(new Page<UserBuychapterHistory>(request, response, -1), userBuychapterHistory);
    		new ExportExcel("章节购买", UserBuychapterHistory.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出章节购买记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/history/userBuychapterHistory/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("history:userBuychapterHistory:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<UserBuychapterHistory> list = ei.getDataList(UserBuychapterHistory.class);
			for (UserBuychapterHistory userBuychapterHistory : list){
				try{
					userBuychapterHistoryService.save(userBuychapterHistory);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条章节购买记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条章节购买记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入章节购买失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/history/userBuychapterHistory/?repage";
    }
	
	/**
	 * 下载导入章节购买数据模板
	 */
	@RequiresPermissions("history:userBuychapterHistory:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "章节购买数据导入模板.xlsx";
    		List<UserBuychapterHistory> list = Lists.newArrayList(); 
    		new ExportExcel("章节购买数据", UserBuychapterHistory.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/history/userBuychapterHistory/?repage";
    }
	
	
	

}