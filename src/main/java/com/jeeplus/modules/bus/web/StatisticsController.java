/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bus.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.modules.bus.dto.BookClickDto;
import com.jeeplus.modules.bus.dto.FodderClickDto;
import com.jeeplus.modules.bus.dto.StatisticsDto;
import com.jeeplus.modules.bus.entity.Book;
import com.jeeplus.modules.bus.enums.GroupTypeEnum;
import com.jeeplus.modules.bus.enums.RoleEnum;
import com.jeeplus.modules.bus.form.StatisticsForm;
import com.jeeplus.modules.bus.history.service.UserBuychapterHistoryService;
import com.jeeplus.modules.bus.history.service.UserClickHistoryService;
import com.jeeplus.modules.bus.history.service.UserClickLogService;
import com.jeeplus.modules.bus.service.BookService;
import com.jeeplus.modules.bus.service.PaymentBillService;
import com.jeeplus.modules.sys.utils.UserUtils;

/**
 * 数据统计controller
 * @author zhangsc
 * @version 2017-11-29
 */
@Controller
@RequestMapping(value = "${adminPath}/bus/statistics")
public class StatisticsController extends BaseController {

	@Autowired
	private PaymentBillService paymentBillService;
	@Autowired
	private UserBuychapterHistoryService userBuychapterHistoryService;
	@Autowired
	private BookService bookService;
	@Autowired
	private UserClickHistoryService userClickHistoryService;
	@Autowired
	private  UserClickLogService userClickLogService;
	
	/**
	 * 总收入
	 */
	@RequiresPermissions("bus:statistics:sumEarning")
	@RequestMapping(value = "sumEarning")
	public String sumEarning(StatisticsDto statistics ,HttpServletRequest request, HttpServletResponse response, Model model) {
		List<StatisticsDto> list = paymentBillService.getSumRevenue(statistics);
		
		model.addAttribute("statistics", statistics);
		model.addAttribute("list", list);
		return "modules/bus/sumEarningList";
	}

	
	/**
	 * 各内容提供商收入
	 */
	@RequiresPermissions("bus:statistics:coutRevenueBySupply")
	@RequestMapping(value = "coutRevenueBySupply")
	public String countRevenueBySupply(StatisticsDto statistics ,HttpServletRequest request, HttpServletResponse response, Model model) {
		List<StatisticsDto> list = paymentBillService.countRevenueBySupply(statistics);
		
		model.addAttribute("statistics", statistics);
		model.addAttribute("list", list);
		return "modules/bus/countRevenueBySupply";
	}
	
	/**
	 * 各渠道收入
	 */
	@RequiresPermissions("bus:statistics:coutRevenueByChannel")
	@RequestMapping(value = "coutRevenueByChannel")
	public String coutRevenueByChannel(StatisticsDto statistics ,HttpServletRequest request, HttpServletResponse response, Model model) {
		List<StatisticsDto> list = paymentBillService.coutRevenueByChannel(statistics);
		
		model.addAttribute("statistics", statistics);
		model.addAttribute("list", list);
		return "modules/bus/countRevenueByChannel";
	}
	
	/**
	 * 各个小说收入
	 */
	@RequiresPermissions("bus:statistics:coutRevenueByBook")
	@RequestMapping(value = "coutRevenueByBook")
	public String countRevenueByBook(StatisticsDto statistics ,HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<StatisticsDto> page = userBuychapterHistoryService.countRevenueByBook(new Page<StatisticsDto>(request, response), statistics);
		
		model.addAttribute("statistics", statistics);
		model.addAttribute("page", page);
		return "modules/bus/countRevenueByBook";
	}
	
	/**
	 * 小说列表页面
	 */
	@RequiresPermissions("bus:statistics:viewCountForBook")
	@RequestMapping(value = "viewCountForBook")
	public String viewCountForBook(Book book, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Book> page = bookService.findPage(new Page<Book>(request, response), book); 
		model.addAttribute("page", page);
		return "modules/bus/viewCountForBook";
	}
	
	/**
	 * 作品点击数据(已过时)
	 */
	@RequiresPermissions("bus:statistics:fodderClickCount")
	@RequestMapping(value = "fodderClickCount")
	public String fodderClickCount(FodderClickDto fodderClick, HttpServletRequest request, HttpServletResponse response, Model model) {
		if(StringUtils.isBlank(fodderClick.getGroupType())){
			fodderClick.setGroupType(GroupTypeEnum.ALL_DATA.getCode());
		}
		
		Page<FodderClickDto> page = userClickHistoryService.findClikCountPage(new Page<FodderClickDto>(request, response), fodderClick); 
		model.addAttribute("page", page);
		model.addAttribute("fodderClick", fodderClick);
		
		return "modules/bus/fodderClickCount";
	}
	
	
	/**
	 * 书籍点击数据book
	 */
	@RequiresPermissions("bus:statistics:bookClickCount")
	@RequestMapping(value = "bookClickCount")
	public String bookClickCount(StatisticsForm statisticsForm, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<BookClickDto> page = userClickLogService.findClikCountPage(new Page<BookClickDto>(request, response), statisticsForm); 
		
		model.addAttribute("page", page);
		model.addAttribute("statisticsForm", statisticsForm);
		return "modules/bus/bookClickCount";
	}
	

}