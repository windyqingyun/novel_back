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
import com.jeeplus.modules.bus.entity.UserWallet;
import com.jeeplus.modules.bus.service.UserWalletService;

/**
 * 用户钱包Controller
 * @author zhangsc
 * @version 2017-11-29
 */
@Controller
@RequestMapping(value = "${adminPath}/bus/userWallet")
public class UserWalletController extends BaseController {

	@Autowired
	private UserWalletService userWalletService;
	
	@ModelAttribute
	public UserWallet get(@RequestParam(required=false) String id) {
		UserWallet entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = userWalletService.get(id);
		}
		if (entity == null){
			entity = new UserWallet();
		}
		return entity;
	}
	
	/**
	 * 用户钱包列表页面
	 */
	@RequiresPermissions("bus:userWallet:list")
	@RequestMapping(value = {"list", ""})
	public String list(UserWallet userWallet, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<UserWallet> page = userWalletService.findPage(new Page<UserWallet>(request, response), userWallet); 
		model.addAttribute("page", page);
		return "modules/bus/userWalletList";
	}

	/**
	 * 查看，增加，编辑用户钱包表单页面
	 */
	@RequiresPermissions(value={"bus:userWallet:view","bus:userWallet:add","bus:userWallet:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(UserWallet userWallet, Model model) {
		model.addAttribute("userWallet", userWallet);
		return "modules/bus/userWalletForm";
	}

	/**
	 * 保存用户钱包
	 */
	@RequiresPermissions(value={"bus:userWallet:add","bus:userWallet:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(UserWallet userWallet, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, userWallet)){
			return form(userWallet, model);
		}
		if(!userWallet.getIsNewRecord()){//编辑表单保存
			UserWallet t = userWalletService.get(userWallet.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(userWallet, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			userWalletService.save(t);//保存
		}else{//新增表单保存
			userWalletService.save(userWallet);//保存
		}
		addMessage(redirectAttributes, "保存用户钱包成功");
		return "redirect:"+Global.getAdminPath()+"/bus/userWallet/?repage";
	}
	
	/**
	 * 删除用户钱包
	 */
	@RequiresPermissions("bus:userWallet:del")
	@RequestMapping(value = "delete")
	public String delete(UserWallet userWallet, RedirectAttributes redirectAttributes) {
		userWalletService.delete(userWallet);
		addMessage(redirectAttributes, "删除用户钱包成功");
		return "redirect:"+Global.getAdminPath()+"/bus/userWallet/?repage";
	}
	
	/**
	 * 批量删除用户钱包
	 */
	@RequiresPermissions("bus:userWallet:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			userWalletService.delete(userWalletService.get(id));
		}
		addMessage(redirectAttributes, "删除用户钱包成功");
		return "redirect:"+Global.getAdminPath()+"/bus/userWallet/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("bus:userWallet:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(UserWallet userWallet, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "用户钱包"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<UserWallet> page = userWalletService.findPage(new Page<UserWallet>(request, response, -1), userWallet);
    		new ExportExcel("用户钱包", UserWallet.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出用户钱包记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/bus/userWallet/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("bus:userWallet:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<UserWallet> list = ei.getDataList(UserWallet.class);
			for (UserWallet userWallet : list){
				try{
					userWalletService.save(userWallet);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条用户钱包记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条用户钱包记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入用户钱包失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/bus/userWallet/?repage";
    }
	
	/**
	 * 下载导入用户钱包数据模板
	 */
	@RequiresPermissions("bus:userWallet:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "用户钱包数据导入模板.xlsx";
    		List<UserWallet> list = Lists.newArrayList(); 
    		new ExportExcel("用户钱包数据", UserWallet.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/bus/userWallet/?repage";
    }
	
	
	

}