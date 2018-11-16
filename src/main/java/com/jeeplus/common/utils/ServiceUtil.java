package com.jeeplus.common.utils;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jeeplus.common.enums.IRespValueEnum;
import com.jeeplus.common.enums.RespCodeEnum;
@Component(value = "serviceUtil")
public class ServiceUtil {

	/**
	 * 根据用户提交的para 返回用户调用的方法
	 */
	public String getMethodName(String para) {
		String methodName = "";
		System.out.println("para:" + para);
		JSONObject jo = JSON.parseObject(para);
		methodName = jo.getString("methodName");
		return methodName;
	}

	/**
	 * 调用方法接口失败
	 */
	public String getMethodFailMessage(String methodName) {
		Map map = new HashMap();
		map.put("resCode", RespCodeEnum.U4.getResCode());
		map.put("resText", RespCodeEnum.U4.getFriendlyText());
		map.put("methodName", methodName);
		String res = JSON.toJSONString(map);
		return res;
	}
	
	/**
	 * 调用错误 message
	 */
	public static String getErrorMessage(String errorCode, String message) {
		Map map = new HashMap();
		map.put("resCode", errorCode);
		map.put("resText", message);
		String res = JSON.toJSONString(map);
		return res;
	}

	public String getErrorMessage() {
		return getErrorMessage(RespCodeEnum.U1.getResCode(), RespCodeEnum.U1.getFriendlyText());
	}

	public String getErrorMessage(String message) {
		return getErrorMessage(RespCodeEnum.U1.getResCode(), RespCodeEnum.U1.getFriendlyText() + message);
	}
	
	public static String doResponse(String msg) {
		int pos = msg.indexOf("-");
		String respCode = msg.substring(0, pos);
		String respMsg = msg.substring(pos + 1);
		return "{\"resCode\":\"" + respCode + "\",\"resText\":\"" + respMsg
				+ "\"}";
	}

	public static String doResponse(IRespValueEnum respEnum, boolean isFriendly) {
		if (isFriendly) {
			return "{\"resCode\":\"" + respEnum.getResCode()
					+ "\",\"resText\":\"" + respEnum.getFriendlyText() + "\"}";
		}
		return "{\"resCode\":\"" + respEnum.getResCode()
				+ "\",\"resText\":\"" + respEnum.getResText() + "\"}";
	}
	/***
	 * 调用错误 message,传错误的信息,只是针对U4
	 * @Description 
	 * @author ygq
	 * @Date 2015-7-23 下午06:02:48
	 * @param message
	 * @return
	 */
	public static String getErrorMessageForU4(String message) {
		Map map = new HashMap();
		map.put("resCode", RespCodeEnum.U4.getResCode());
		map.put("resText", RespCodeEnum.U4.getResText(message));
		String res = JSON.toJSONString(map);
		return res;
	}

}
