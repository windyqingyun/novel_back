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
import com.jeeplus.modules.bus.entity.Book;
import com.jeeplus.modules.bus.entity.BookChapter;
import com.jeeplus.modules.bus.service.BookChapterService;
import com.jeeplus.modules.bus.service.BookService;

/**
 * 小数Controller
 * @author zhangsc
 * @version 2017-11-09
 */
@Controller
@RequestMapping(value = "${adminPath}/bus/book")
public class BookController extends BaseController {

	@Autowired
	private BookService bookService;
	@Autowired
	private BookChapterService bookChapterService;
	
	@ModelAttribute
	public Book get(@RequestParam(required=false) String id) {
		Book entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = bookService.get(id);
		}
		if (entity == null){
			entity = new Book();
		}
		return entity;
	}
	
	/**
	 * 小说列表页面
	 */
	@RequiresPermissions("bus:book:list")
	@RequestMapping(value = {"list", ""})
	public String list(Book book, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Book> page = bookService.findPage(new Page<Book>(request, response), book); 
		
		model.addAttribute("page", page);
		return "modules/bus/bookList";
	}

	/**
	 * 查看，增加，编辑小说表单页面
	 */
	@RequiresPermissions(value={"bus:book:view","bus:book:add","bus:book:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Book book, Model model) {
		List<BookChapter> bookChapterList = bookChapterService.findChapterListForSelectInput(book.getId());
		if(bookChapterList == null) {
			bookChapterList = Lists.newArrayList();
		}
		book.setChapterList(bookChapterList);
		model.addAttribute("book", book);
		return "modules/bus/bookForm";
	}

	/**
	 * 保存小说
	 */
	@RequiresPermissions(value={"bus:book:add","bus:book:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(Book book, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, book)){
			return form(book, model);
		}
		if(!book.getIsNewRecord()){//编辑表单保存
			Book t = bookService.get(book.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(book, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			bookService.save(t);//保存
		}else{//新增表单保存
			bookService.save(book);//保存
		}
		addMessage(redirectAttributes, "保存小说成功");
		return "redirect:"+Global.getAdminPath()+"/bus/book/?repage";
	}
	
	/**
	 * 删除小说
	 */
	@RequiresPermissions("bus:book:del")
	@RequestMapping(value = "delete")
	public String delete(Book book, RedirectAttributes redirectAttributes) {
		bookService.delete(book);
		addMessage(redirectAttributes, "删除小说成功");
		return "redirect:"+Global.getAdminPath()+"/bus/book/?repage";
	}
	
	/**
	 * 批量删除小说
	 */
	@RequiresPermissions("bus:book:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			bookService.delete(bookService.get(id));
		}
		addMessage(redirectAttributes, "删除小说成功");
		return "redirect:"+Global.getAdminPath()+"/bus/book/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("bus:book:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(Book book, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "小说"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Book> page = bookService.findPage(new Page<Book>(request, response, -1), book);
    		new ExportExcel("小说", Book.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出小说记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/bus/book/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("bus:book:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Book> list = ei.getDataList(Book.class);
			for (Book book : list){
				try{
					bookService.save(book);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条小说记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条小说记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入小说失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/bus/book/?repage";
    }
	
	/**
	 * 下载导入小说数据模板
	 */
	@RequiresPermissions("bus:book:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "小说数据导入模板.xlsx";
    		List<Book> list = Lists.newArrayList(); 
    		new ExportExcel("小说数据", Book.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/bus/book/?repage";
    }
	
	
	

}