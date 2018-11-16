/**
 * Copyright &copy; 2010-2016 MainSoft All rights reserved.
 */
package com.jeeplus.modules.bus.web;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.web.BaseController;

/**
 * 上传附件controller
 * @author zhangsc
 */
@Controller
@RequestMapping(value = "${adminPath}/bus/nodArchive")
public class NodArchiveController extends BaseController {
	public static final String UM_IMAGE_URL = "umEitor/image/";
	
	/**
	 * 百度编辑器上传图片
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@ResponseBody
    @RequestMapping(value = "umEditorUpload")
    public String umEditorUploadImg(HttpServletRequest request, HttpServletResponse response, Model model) {
		JSONObject jsonObj = new JSONObject();
		
		//下载图片的地址
		String downUrl = request.getRequestURL().toString();
		downUrl = downUrl.replace("/bus/nodArchive/umEditorUpload", "/sys/uploadFile/download")+"?url=";
		
		try {
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			//获取前台传值
			Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();

			String basePath = Global.getConfig("userfiles.basedir")+"/userfiles/";
	    	String path = DateUtils.getDate();
	        File pathFile = new File(basePath+path);
	        //如果目录不存在，新建目录
	        if(!pathFile.exists() && !pathFile.isDirectory()){
	        	pathFile.mkdirs();
	        }
			
			for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {   
				// 上传文件名      
				MultipartFile mf = entity.getValue();    
				String fileName = mf.getOriginalFilename(); 
				//过滤掉html等代码
				fileName = StringEscapeUtils.escapeHtml4(fileName);
				
				//生成新的文件名
				int suffixIndex = fileName.lastIndexOf(".");
				String file = fileName.substring(0, suffixIndex) + new Random().nextInt(10000);
				fileName = file + fileName.substring(suffixIndex, file.length());

				String filePath = basePath+path+File.separatorChar+fileName;
				
	            File newFile=new File(filePath);
				
	            mf.transferTo(newFile);
	            
				jsonObj.put("state", "SUCCESS");
				jsonObj.put("url", downUrl+"/sqsw/userfiles/" +path+"/"+java.net.URLEncoder.encode(fileName, "UTF-8"));
				jsonObj.put("title", mf.getOriginalFilename());
				jsonObj.put("original", mf.getOriginalFilename());
			}
		}catch (IOException e) {
			e.printStackTrace();
		}
		
		
		return jsonObj.toJSONString();
	}
	
}