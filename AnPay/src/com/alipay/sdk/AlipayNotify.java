package com.alipay.sdk;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.anpay.sdk.C_Filter_pay;

import database.D_Dao;

//支付宝异步通知
@WebServlet("/AlipayNotify")
public class AlipayNotify extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public AlipayNotify() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		C_Filter_pay.Encoding(request, response);
		System.out.println("支付宝异步通知到达！");
		//获取支付宝POST过来反馈信息
		Map<String,String> params = new HashMap<String,String>();
		Map requestParams = request.getParameterMap();
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
			//乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
			//valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
			params.put(name, valueStr);
		}
		//获取支付宝的通知返回参数，
		//商户订单号
		String order_number = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");
		//支付宝交易号
		String pay_number = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");
		//订单金额
		//String money_total = new String(request.getParameter("total_amount").getBytes("ISO-8859-1"),"UTF-8");
		//交易状态
		String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"),"UTF-8");
		System.out.println(trade_status);
		try {
			boolean verify_result = AlipaySignature.rsaCheckV1(params, AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.CHARSET, "RSA2");
			if(verify_result){//验证成功
				System.out.println("验签成功！");
				if(trade_status.equals("TRADE_SUCCESS")||trade_status.equals("TRADE_FINISHED")){
					//支付成功或交易完成
					System.out.println("支付成功！");
					//********************业务处理************************
					if (D_Dao.Notify(order_number,pay_number,"alipay")) {
						System.out.println("订单状态修改：成功！");
						if (D_Dao.CbPay(order_number)) {
							System.out.println("子业务系统通知：成功！");
							C_Filter_pay.put("success", response);//请不要修改或删除
						} else {
							System.out.println("子业务系统通知：失败！");
							C_Filter_pay.put("fail", response);//请不要修改或删除
						}
					} else {
						System.out.println("订单状态修改：失败！");
						C_Filter_pay.put("fail", response);//请不要修改或删除
					}
					//**************************************************
				} else {
					//无操作
					System.out.println("状态非成功！");
					C_Filter_pay.put("fail", response);//请不要修改或删除
				}
			}else{//验证失败
				System.out.println("验签失败！");
				C_Filter_pay.put("fail", response);//请不要修改或删除
			}
		} catch (AlipayApiException e) {
			e.printStackTrace();
			System.out.println("验签异常！");
			C_Filter_pay.put("fail", response);//请不要修改或删除
		}
	}

}
