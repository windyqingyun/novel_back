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
import com.jeeplus.common.config.Global;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.MyBeanUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.modules.bus.entity.Book;
import com.jeeplus.modules.bus.entity.BookChapter;
import com.jeeplus.modules.bus.entity.Fodder;
import com.jeeplus.modules.bus.service.BookChapterService;
import com.jeeplus.modules.bus.service.FodderService;
import com.jeeplus.modules.sys.utils.UserUtils;

/**
 * 素材Controller
 * @author zhangsc
 * @version 2017-11-09
 */
@Controller
@RequestMapping(value = "${adminPath}/bus/fodder")
public class FodderController extends BaseController {

	@Autowired
	private FodderService fodderService;
	@Autowired
	private BookChapterService bookChapterService;

	@ModelAttribute
	public Fodder get(@RequestParam(required=false) String id) {
		Fodder entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = fodderService.get(id);
		}
		if (entity == null){
			entity = new Fodder();
		}
		return entity;
	}
	
	/**
	 * 素材列表页面
	 */
	@RequiresPermissions("bus:fodder:list")
	@RequestMapping(value = {"list", ""})
	public String list(Fodder fodder, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Fodder> page = fodderService.findPage(new Page<Fodder>(request, response), fodder); 
		model.addAttribute("page", page);
		return "modules/bus/fodderList";
	}

	/**
	 * 查看，增加，编辑素材表单页面
	 */
	@RequiresPermissions(value={"bus:fodder:view","bus:fodder:add","bus:fodder:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Fodder fodder, Model model) {
		if(fodder.getBook() != null && StringUtils.isNotBlank(fodder.getBook().getId())){
			BookChapter bookChapter = new BookChapter();
			bookChapter.setBookId(fodder.getBook().getId());
			List<BookChapter> chapterList = bookChapterService.findHundredChapterList(bookChapter);
			model.addAttribute("chapterList", chapterList);
		}
		
		model.addAttribute("fodder", fodder);
		return "modules/bus/fodderForm";
	}

	/**
	 * 保存素材
	 */
	@RequiresPermissions(value={"bus:fodder:add","bus:fodder:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(Fodder fodder, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, fodder)){
			return form(fodder, model);
		}
		//如果素材已经选择了书籍 ，那么不允许不输入章节
		if(fodder.getBook() != null && StringUtils.isNotBlank(fodder.getBook().getId()) && fodder.getChapter() == null ) {
			addMessage(model, "章节不能为空");
			return form(fodder, model);
		}else if(fodder.getBook() != null && StringUtils.isNotBlank(fodder.getBook().getId()) 
				&& fodder.getChapter() != null) {
			fodder.setLinkUrl("http://39.106.4.97/nrfx_intertem/service/interface/bookChapter/info");
		}
		
		if(!fodder.getIsNewRecord()){//编辑表单保存
			Fodder t = fodderService.get(fodder.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(fodder, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			fodderService.save(t);//保存
		}else{//新增表单保存
			//如果是新增保存，使用当前用户的机构作为素材的来源地址
			fodder.setOffice(UserUtils.getUser().getOffice());
			//新增素材默认为0浏览次数
			fodder.setViewcount(0);
			fodderService.save(fodder);//保存
		}
		addMessage(redirectAttributes, "保存素材成功");
		return "redirect:"+Global.getAdminPath()+"/bus/fodder/?repage";
	}
	
	/**
	 * 删除素材
	 */
	@RequiresPermissions("bus:fodder:del")
	@RequestMapping(value = "delete")
	public String delete(Fodder fodder, RedirectAttributes redirectAttributes) {
		fodderService.delete(fodder);
		addMessage(redirectAttributes, "删除素材成功");
		return "redirect:"+Global.getAdminPath()+"/bus/fodder/?repage";
	}
	
	/**
	 * 批量删除素材
	 */
	@RequiresPermissions("bus:fodder:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			fodderService.delete(fodderService.get(id));
		}
		addMessage(redirectAttributes, "删除素材成功");
		return "redirect:"+Global.getAdminPath()+"/bus/fodder/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("bus:fodder:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(Fodder fodder, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "素材"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Fodder> page = fodderService.findPage(new Page<Fodder>(request, response, -1), fodder);
    		new ExportExcel("素材", Fodder.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出素材记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/bus/fodder/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("bus:fodder:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Fodder> list = ei.getDataList(Fodder.class);
			for (Fodder fodder : list){
				try{
					fodderService.save(fodder);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条素材记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条素材记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入素材失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/bus/fodder/?repage";
    }
	
	/**
	 * 下载导入素材数据模板
	 */
	@RequiresPermissions("bus:fodder:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "素材数据导入模板.xlsx";
    		List<Fodder> list = Lists.newArrayList(); 
    		new ExportExcel("素材数据", Fodder.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/bus/fodder/?repage";
    }
	
	
	/**
	 * 选择书籍id
	 */
	@RequestMapping(value = "selectbook")
	public String selectbook(Book book, String url, String fieldLabels, String fieldKeys, String searchLabel, String searchKey, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Book> page = fodderService.findPageBybook(new Page<Book>(request, response),  book);
		model.addAttribute("labelNames", fieldLabels.split("\\|"));
		model.addAttribute("labelValues", fieldKeys.split("\\|"));
		model.addAttribute("fieldLabels", fieldLabels);
		model.addAttribute("fieldKeys", fieldKeys);
		model.addAttribute("url", url);
		model.addAttribute("searchLabel", searchLabel);
		model.addAttribute("searchKey", searchKey);
		model.addAttribute("obj", book);
		model.addAttribute("page", page);
		return "modules/sys/gridselect";
	}
	

}