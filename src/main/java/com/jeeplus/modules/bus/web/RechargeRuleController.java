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
import com.jeeplus.modules.bus.entity.RechargeRule;
import com.jeeplus.modules.bus.service.RechargeRuleService;

/**
 * 充值规则Controller
 * @author zhangsc
 * @version 2017-11-29
 */
@Controller
@RequestMapping(value = "${adminPath}/bus/rechargeRule")
public class RechargeRuleController extends BaseController {

	@Autowired
	private RechargeRuleService rechargeRuleService;
	
	@ModelAttribute
	public RechargeRule get(@RequestParam(required=false) String id) {
		RechargeRule entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = rechargeRuleService.get(id);
		}
		if (entity == null){
			entity = new RechargeRule();
		}
		return entity;
	}
	
	/**
	 * 充值规则列表页面
	 */
	@RequiresPermissions("bus:rechargeRule:list")
	@RequestMapping(value = {"list", ""})
	public String list(RechargeRule rechargeRule, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<RechargeRule> page = rechargeRuleService.findPage(new Page<RechargeRule>(request, response), rechargeRule); 
		
		model.addAttribute("page", page);
		return "modules/bus/rechargeRuleList";
	}

	/**
	 * 查看，增加，编辑充值规则表单页面
	 */
	@RequiresPermissions(value={"bus:rechargeRule:view","bus:rechargeRule:add","bus:rechargeRule:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(RechargeRule rechargeRule, Model model) {
		model.addAttribute("rechargeRule", rechargeRule);
		return "modules/bus/rechargeRuleForm";
	}

	/**
	 * 保存充值规则
	 */
	@RequiresPermissions(value={"bus:rechargeRule:add","bus:rechargeRule:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(RechargeRule rechargeRule, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, rechargeRule)){
			return form(rechargeRule, model);
		}
		if(!rechargeRule.getIsNewRecord()){//编辑表单保存
			RechargeRule t = rechargeRuleService.get(rechargeRule.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(rechargeRule, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			rechargeRuleService.save(t);//保存
		}else{//新增表单保存
			rechargeRuleService.save(rechargeRule);//保存
		}
		addMessage(redirectAttributes, "保存充值规则成功");
		return "redirect:"+Global.getAdminPath()+"/bus/rechargeRule/?repage";
	}
	
	/**
	 * 删除充值规则
	 */
	@RequiresPermissions("bus:rechargeRule:del")
	@RequestMapping(value = "delete")
	public String delete(RechargeRule rechargeRule, RedirectAttributes redirectAttributes) {
		rechargeRuleService.delete(rechargeRule);
		addMessage(redirectAttributes, "删除充值规则成功");
		return "redirect:"+Global.getAdminPath()+"/bus/rechargeRule/?repage";
	}
	
	/**
	 * 批量删除充值规则
	 */
	@RequiresPermissions("bus:rechargeRule:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			rechargeRuleService.delete(rechargeRuleService.get(id));
		}
		addMessage(redirectAttributes, "删除充值规则成功");
		return "redirect:"+Global.getAdminPath()+"/bus/rechargeRule/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("bus:rechargeRule:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(RechargeRule rechargeRule, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "充值规则"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<RechargeRule> page = rechargeRuleService.findPage(new Page<RechargeRule>(request, response, -1), rechargeRule);
    		new ExportExcel("充值规则", RechargeRule.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出充值规则记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/bus/rechargeRule/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("bus:rechargeRule:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<RechargeRule> list = ei.getDataList(RechargeRule.class);
			for (RechargeRule rechargeRule : list){
				try{
					rechargeRuleService.save(rechargeRule);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条充值规则记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条充值规则记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入充值规则失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/bus/rechargeRule/?repage";
    }
	
	/**
	 * 下载导入充值规则数据模板
	 */
	@RequiresPermissions("bus:rechargeRule:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "充值规则数据导入模板.xlsx";
    		List<RechargeRule> list = Lists.newArrayList(); 
    		new ExportExcel("充值规则数据", RechargeRule.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/bus/rechargeRule/?repage";
    }
	
	
	

}