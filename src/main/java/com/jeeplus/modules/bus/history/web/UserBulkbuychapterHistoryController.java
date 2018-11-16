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
import com.jeeplus.modules.bus.history.entity.UserBulkbuychapterHistory;
import com.jeeplus.modules.bus.history.service.UserBulkbuychapterHistoryService;

/**
 * 用户多章购买Controller
 * @author zhangsc
 * @version 2017-11-03
 */
@Controller
@RequestMapping(value = "${adminPath}/history/userBulkbuychapterHistory")
public class UserBulkbuychapterHistoryController extends BaseController {

	@Autowired
	private UserBulkbuychapterHistoryService userBulkbuychapterHistoryService;
	
	@ModelAttribute
	public UserBulkbuychapterHistory get(@RequestParam(required=false) String id) {
		UserBulkbuychapterHistory entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = userBulkbuychapterHistoryService.get(id);
		}
		if (entity == null){
			entity = new UserBulkbuychapterHistory();
		}
		return entity;
	}
	
	/**
	 * 用户多章购买列表页面
	 */
	@RequiresPermissions("history:userBulkbuychapterHistory:list")
	@RequestMapping(value = {"list", ""})
	public String list(UserBulkbuychapterHistory userBulkbuychapterHistory, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<UserBulkbuychapterHistory> page = userBulkbuychapterHistoryService.findPage(new Page<UserBulkbuychapterHistory>(request, response), userBulkbuychapterHistory); 
		model.addAttribute("page", page);
		return "bus/history/userBulkbuychapterHistoryList";
	}

	/**
	 * 查看，增加，编辑用户多章购买表单页面
	 */
	@RequiresPermissions(value={"history:userBulkbuychapterHistory:view","history:userBulkbuychapterHistory:add","history:userBulkbuychapterHistory:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(UserBulkbuychapterHistory userBulkbuychapterHistory, Model model) {
		model.addAttribute("userBulkbuychapterHistory", userBulkbuychapterHistory);
		return "bus/history/userBulkbuychapterHistoryForm";
	}

	/**
	 * 保存用户多章购买
	 */
	@RequiresPermissions(value={"history:userBulkbuychapterHistory:add","history:userBulkbuychapterHistory:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(UserBulkbuychapterHistory userBulkbuychapterHistory, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, userBulkbuychapterHistory)){
			return form(userBulkbuychapterHistory, model);
		}
		if(!userBulkbuychapterHistory.getIsNewRecord()){//编辑表单保存
			UserBulkbuychapterHistory t = userBulkbuychapterHistoryService.get(userBulkbuychapterHistory.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(userBulkbuychapterHistory, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			userBulkbuychapterHistoryService.save(t);//保存
		}else{//新增表单保存
			userBulkbuychapterHistoryService.save(userBulkbuychapterHistory);//保存
		}
		addMessage(redirectAttributes, "保存用户多章购买成功");
		return "redirect:"+Global.getAdminPath()+"/history/userBulkbuychapterHistory/?repage";
	}
	
	/**
	 * 删除用户多章购买
	 */
	@RequiresPermissions("history:userBulkbuychapterHistory:del")
	@RequestMapping(value = "delete")
	public String delete(UserBulkbuychapterHistory userBulkbuychapterHistory, RedirectAttributes redirectAttributes) {
		userBulkbuychapterHistoryService.delete(userBulkbuychapterHistory);
		addMessage(redirectAttributes, "删除用户多章购买成功");
		return "redirect:"+Global.getAdminPath()+"/history/userBulkbuychapterHistory/?repage";
	}
	
	/**
	 * 批量删除用户多章购买
	 */
	@RequiresPermissions("history:userBulkbuychapterHistory:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			userBulkbuychapterHistoryService.delete(userBulkbuychapterHistoryService.get(id));
		}
		addMessage(redirectAttributes, "删除用户多章购买成功");
		return "redirect:"+Global.getAdminPath()+"/history/userBulkbuychapterHistory/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("history:userBulkbuychapterHistory:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(UserBulkbuychapterHistory userBulkbuychapterHistory, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "用户多章购买"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<UserBulkbuychapterHistory> page = userBulkbuychapterHistoryService.findPage(new Page<UserBulkbuychapterHistory>(request, response, -1), userBulkbuychapterHistory);
    		new ExportExcel("用户多章购买", UserBulkbuychapterHistory.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出用户多章购买记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/history/userBulkbuychapterHistory/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("history:userBulkbuychapterHistory:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<UserBulkbuychapterHistory> list = ei.getDataList(UserBulkbuychapterHistory.class);
			for (UserBulkbuychapterHistory userBulkbuychapterHistory : list){
				try{
					userBulkbuychapterHistoryService.save(userBulkbuychapterHistory);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条用户多章购买记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条用户多章购买记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入用户多章购买失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/history/userBulkbuychapterHistory/?repage";
    }
	
	/**
	 * 下载导入用户多章购买数据模板
	 */
	@RequiresPermissions("history:userBulkbuychapterHistory:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "用户多章购买数据导入模板.xlsx";
    		List<UserBulkbuychapterHistory> list = Lists.newArrayList(); 
    		new ExportExcel("用户多章购买数据", UserBulkbuychapterHistory.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/history/userBulkbuychapterHistory/?repage";
    }
	
	
	

}