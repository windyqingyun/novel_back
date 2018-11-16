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
import org.springframework.web.bind.annotation.ResponseBody;
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
import com.jeeplus.modules.bus.entity.BookChapter;
import com.jeeplus.modules.bus.service.BookChapterService;

/**
 * 小说章节内容Controller
 * @author zhangsc
 * @version 2017-11-09
 */
@Controller
@RequestMapping(value = "${adminPath}/bus/bookChapter")
public class BookChapterController extends BaseController {

	@Autowired
	private BookChapterService bookChapterService;
	
	@ModelAttribute
	public BookChapter get(@RequestParam(required=false) String id) {
		BookChapter entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = bookChapterService.get(id);
		}
		if (entity == null){
			entity = new BookChapter();
		}
		return entity;
	}
	
	/**
	 * 小说章节内容列表页面
	 */
	@RequiresPermissions("bus:bookChapter:list")
	@RequestMapping(value = {"list", ""})
	public String list(BookChapter bookChapter, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<BookChapter> page = bookChapterService.findPage(new Page<BookChapter>(request, response), bookChapter); 
		model.addAttribute("page", page);
		return "modules/bus/bookChapterList";
	}

	/**
	 * 查看，增加，编辑小说章节内容表单页面
	 */
	@RequiresPermissions(value={"bus:bookChapter:view","bus:bookChapter:add","bus:bookChapter:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(BookChapter bookChapter, Model model) {
		model.addAttribute("bookChapter", bookChapter);
		return "modules/bus/bookChapterForm";
	}

	/**
	 * 保存小说章节内容
	 */
	@RequiresPermissions(value={"bus:bookChapter:add","bus:bookChapter:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(BookChapter bookChapter, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, bookChapter)){
			return form(bookChapter, model);
		}
		if(!bookChapter.getIsNewRecord()){//编辑表单保存
			BookChapter t = bookChapterService.get(bookChapter.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(bookChapter, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			bookChapterService.save(t);//保存
		}else{//新增表单保存
			bookChapterService.save(bookChapter);//保存
		}
		addMessage(redirectAttributes, "保存小说章节内容成功");
		return "redirect:"+Global.getAdminPath()+"/bus/bookChapter/?repage";
	}
	
	/**
	 * 删除小说章节内容
	 */
	@RequiresPermissions("bus:bookChapter:del")
	@RequestMapping(value = "delete")
	public String delete(BookChapter bookChapter, RedirectAttributes redirectAttributes) {
		bookChapterService.delete(bookChapter);
		addMessage(redirectAttributes, "删除小说章节内容成功");
		return "redirect:"+Global.getAdminPath()+"/bus/bookChapter/?repage";
	}
	
	/**
	 * 批量删除小说章节内容
	 */
	@RequiresPermissions("bus:bookChapter:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			bookChapterService.delete(bookChapterService.get(id));
		}
		addMessage(redirectAttributes, "删除小说章节内容成功");
		return "redirect:"+Global.getAdminPath()+"/bus/bookChapter/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("bus:bookChapter:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(BookChapter bookChapter, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "小说章节内容"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<BookChapter> page = bookChapterService.findPage(new Page<BookChapter>(request, response, -1), bookChapter);
    		new ExportExcel("小说章节内容", BookChapter.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出小说章节内容记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/bus/bookChapter/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("bus:bookChapter:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<BookChapter> list = ei.getDataList(BookChapter.class);
			for (BookChapter bookChapter : list){
				try{
					bookChapterService.save(bookChapter);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条小说章节内容记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条小说章节内容记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入小说章节内容失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/bus/bookChapter/?repage";
    }
	
	/**
	 * 下载导入小说章节内容数据模板
	 */
	@RequiresPermissions("bus:bookChapter:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "小说章节内容数据导入模板.xlsx";
    		List<BookChapter> list = Lists.newArrayList(); 
    		new ExportExcel("小说章节内容数据", BookChapter.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/bus/bookChapter/?repage";
    }
	
	@ResponseBody
	@RequestMapping(value = "findHundredChapterList")
	public List<BookChapter> findHundredChapterList(@RequestParam("bookId")String bookId){
		BookChapter chapter = new BookChapter();
		chapter.setBookId(bookId);
		
		List<BookChapter> list = bookChapterService.findHundredChapterList(chapter);
		return list;
	}
	

}