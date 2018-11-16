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
import com.jeeplus.modules.bus.history.entity.UserFavorites;
import com.jeeplus.modules.bus.history.service.UserFavoritesService;

/**
 * 用户收藏记录Controller
 * @author zhangsc
 * @version 2017-11-03
 */
@Controller
@RequestMapping(value = "${adminPath}/history/userFavorites")
public class UserFavoritesController extends BaseController {

	@Autowired
	private UserFavoritesService userFavoritesService;
	
	@ModelAttribute
	public UserFavorites get(@RequestParam(required=false) String id) {
		UserFavorites entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = userFavoritesService.get(id);
		}
		if (entity == null){
			entity = new UserFavorites();
		}
		return entity;
	}
	
	/**
	 * 用户收藏记录列表页面
	 */
	@RequiresPermissions("history:userFavorites:list")
	@RequestMapping(value = {"list", ""})
	public String list(UserFavorites userFavorites, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<UserFavorites> page = userFavoritesService.findPage(new Page<UserFavorites>(request, response), userFavorites); 
		model.addAttribute("page", page);
		return "bus/history/userFavoritesList";
	}

	/**
	 * 查看，增加，编辑用户收藏记录表单页面
	 */
	@RequiresPermissions(value={"history:userFavorites:view","history:userFavorites:add","history:userFavorites:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(UserFavorites userFavorites, Model model) {
		model.addAttribute("userFavorites", userFavorites);
		return "bus/history/userFavoritesForm";
	}

	/**
	 * 保存用户收藏记录
	 */
	@RequiresPermissions(value={"history:userFavorites:add","history:userFavorites:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(UserFavorites userFavorites, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, userFavorites)){
			return form(userFavorites, model);
		}
		if(!userFavorites.getIsNewRecord()){//编辑表单保存
			UserFavorites t = userFavoritesService.get(userFavorites.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(userFavorites, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			userFavoritesService.save(t);//保存
		}else{//新增表单保存
			userFavoritesService.save(userFavorites);//保存
		}
		addMessage(redirectAttributes, "保存用户收藏记录成功");
		return "redirect:"+Global.getAdminPath()+"/history/userFavorites/?repage";
	}
	
	/**
	 * 删除用户收藏记录
	 */
	@RequiresPermissions("history:userFavorites:del")
	@RequestMapping(value = "delete")
	public String delete(UserFavorites userFavorites, RedirectAttributes redirectAttributes) {
		userFavoritesService.delete(userFavorites);
		addMessage(redirectAttributes, "删除用户收藏记录成功");
		return "redirect:"+Global.getAdminPath()+"/history/userFavorites/?repage";
	}
	
	/**
	 * 批量删除用户收藏记录
	 */
	@RequiresPermissions("history:userFavorites:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			userFavoritesService.delete(userFavoritesService.get(id));
		}
		addMessage(redirectAttributes, "删除用户收藏记录成功");
		return "redirect:"+Global.getAdminPath()+"/history/userFavorites/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("history:userFavorites:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(UserFavorites userFavorites, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "用户收藏记录"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<UserFavorites> page = userFavoritesService.findPage(new Page<UserFavorites>(request, response, -1), userFavorites);
    		new ExportExcel("用户收藏记录", UserFavorites.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出用户收藏记录记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/history/userFavorites/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("history:userFavorites:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<UserFavorites> list = ei.getDataList(UserFavorites.class);
			for (UserFavorites userFavorites : list){
				try{
					userFavoritesService.save(userFavorites);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条用户收藏记录记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条用户收藏记录记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入用户收藏记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/history/userFavorites/?repage";
    }
	
	/**
	 * 下载导入用户收藏记录数据模板
	 */
	@RequiresPermissions("history:userFavorites:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "用户收藏记录数据导入模板.xlsx";
    		List<UserFavorites> list = Lists.newArrayList(); 
    		new ExportExcel("用户收藏记录数据", UserFavorites.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/history/userFavorites/?repage";
    }
	
	
	

}