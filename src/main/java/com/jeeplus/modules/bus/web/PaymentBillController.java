package com.jeeplus.modules.bus.web;

import java.util.Date;
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
import com.jeeplus.modules.bus.entity.PaymentBill;
import com.jeeplus.modules.bus.service.PaymentBillService;

/**
 * 支付单Controller
 * @author zhangsc
 * @version 2017-11-03
 */
@Controller
@RequestMapping(value = "${adminPath}/bus/paymentBill")
public class PaymentBillController extends BaseController {

	@Autowired
	private PaymentBillService paymentBillService;
	
	@ModelAttribute
	public PaymentBill get(@RequestParam(required=false) String id) {
		PaymentBill entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = paymentBillService.get(id);
		}
		if (entity == null){
			entity = new PaymentBill();
		}
		return entity;
	}
	
	/**
	 * 支付单列表页面
	 */
	@RequiresPermissions("bus:paymentBill:list")
	@RequestMapping(value = {"list", ""})
	public String list(PaymentBill paymentBill, HttpServletRequest request, HttpServletResponse response, Model model) {
		//默认查询支付成功的支付单
		if(StringUtils.isBlank(paymentBill.getIssuccess())) {
			paymentBill.setIssuccess(Global.YES);
		}
		
		//默认查询7天内支付的数据
		Date date = new Date();
		if(paymentBill.getBeginPayDate() == null) {
			paymentBill.setBeginPayDate(DateUtils.addDays(date, -7));
		}
		if(paymentBill.getEndPayDate() == null) {
			paymentBill.setEndPayDate(date);
		}
		
		Page<PaymentBill> page = paymentBillService.findPage(new Page<PaymentBill>(request, response), paymentBill); 
		model.addAttribute("page", page);
		return "modules/bus/paymentBillList";
	}

	/**
	 * 查看，增加，编辑支付单表单页面
	 */
	@RequiresPermissions(value={"bus:paymentBill:view","bus:paymentBill:add","bus:paymentBill:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(PaymentBill paymentBill, Model model) {
		model.addAttribute("paymentBill", paymentBill);
		return "modules/bus/paymentBillForm";
	}

	/**
	 * 保存支付单
	 */
	@RequiresPermissions(value={"bus:paymentBill:add","bus:paymentBill:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(PaymentBill paymentBill, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, paymentBill)){
			return form(paymentBill, model);
		}
		if(!paymentBill.getIsNewRecord()){//编辑表单保存
			PaymentBill t = paymentBillService.get(paymentBill.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(paymentBill, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			paymentBillService.save(t);//保存
		}else{//新增表单保存
			paymentBillService.save(paymentBill);//保存
		}
		addMessage(redirectAttributes, "保存支付单成功");
		return "redirect:"+Global.getAdminPath()+"/bus/paymentBill/?repage";
	}
	
	/**
	 * 删除支付单
	 */
	@RequiresPermissions("bus:paymentBill:del")
	@RequestMapping(value = "delete")
	public String delete(PaymentBill paymentBill, RedirectAttributes redirectAttributes) {
		paymentBillService.delete(paymentBill);
		addMessage(redirectAttributes, "删除支付单成功");
		return "redirect:"+Global.getAdminPath()+"/bus/paymentBill/?repage";
	}
	
	/**
	 * 批量删除支付单
	 */
	@RequiresPermissions("bus:paymentBill:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			paymentBillService.delete(paymentBillService.get(id));
		}
		addMessage(redirectAttributes, "删除支付单成功");
		return "redirect:"+Global.getAdminPath()+"/bus/paymentBill/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("bus:paymentBill:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(PaymentBill paymentBill, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "支付单"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<PaymentBill> page = paymentBillService.findPage(new Page<PaymentBill>(request, response, -1), paymentBill);
    		new ExportExcel("支付单", PaymentBill.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出支付单记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/bus/paymentBill/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("bus:paymentBill:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<PaymentBill> list = ei.getDataList(PaymentBill.class);
			for (PaymentBill paymentBill : list){
				try{
					paymentBillService.save(paymentBill);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条支付单记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条支付单记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入支付单失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/bus/paymentBill/?repage";
    }
	
	/**
	 * 下载导入支付单数据模板
	 */
	@RequiresPermissions("bus:paymentBill:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "支付单数据导入模板.xlsx";
    		List<PaymentBill> list = Lists.newArrayList(); 
    		new ExportExcel("支付单数据", PaymentBill.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/bus/paymentBill/?repage";
    }
	
	
	

}