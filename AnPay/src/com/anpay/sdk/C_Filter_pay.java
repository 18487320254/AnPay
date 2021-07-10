package com.anpay.sdk;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//过滤&验证&权限
public class C_Filter_pay{
	//servlet编码设置为UTF-8
	public static void Encoding(HttpServletRequest request, HttpServletResponse response) {
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
	}
	
	//servlet获得string
	public static String get(HttpServletRequest request,String name) {
		return request.getParameter(name).trim().replace(",", "，").replace("#equal#", "=").replace("#apostrophe#", "'").replace("#percent#", "%");
	}
	
	//servlet获得int
	public static int getInt(HttpServletRequest request,String name) {
		return Integer.parseInt(request.getParameter(name).trim());
	}
	
	//servlet获得double
	public static double getDouble(HttpServletRequest request,String name) {
		return Double.valueOf(request.getParameter(name).trim());
	}
	
	//servlet向前端输出字符串
	public static void put(String text, HttpServletResponse response) {
		try {
			PrintWriter out = response.getWriter();
		    out.print(text);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
