package com.wepay.sdk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.anpay.sdk.C_Filter_pay;

import database.D_Dao;

@WebServlet("/WePayNotify")
public class WePayNotify extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public WePayNotify() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		C_Filter_pay.Encoding(request, response);
	    System.out.println("微信异步回调到达！");
        String inputLine = null;
        // 接收到的数据
        StringBuffer recieveData = new StringBuffer();
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(
                    request.getInputStream(), "UTF-8"));
            while ((inputLine = in.readLine()) != null) {
                recieveData.append(inputLine);
            }
        } catch (IOException e) {
        } finally {
            try {
                if (null != in) {
                    in.close();
                }
            } catch (IOException e) {
            }
        }
	    //System.out.println(recieveData.toString());
	    
		try {
			MyConfig config = new MyConfig();
		    WXPay wxpay = new WXPay(config);
		    Map<String, String> notifyMap = WXPayUtil.xmlToMap(recieveData.toString());  // 转换成map
		    System.out.println(notifyMap);
		    if (wxpay.isPayResultNotifySignatureValid(notifyMap)) {
		    	System.out.println("验签正确");
		        // 签名正确
		        // 进行处理。
		        // 注意特殊情况：订单已经退款，但收到了支付结果成功的通知，不应把商户侧订单状态从退款改成支付成功
		    	if (notifyMap.get("return_code").toString().equals("SUCCESS")&&notifyMap.get("result_code").toString().equals("SUCCESS")) {//成功
					System.out.println("支付成功！");
		    		//String total_fee=notifyMap.get("total_fee").toString();//金额
					String order_number=notifyMap.get("out_trade_no").toString();//商户订单号
					String pay_number=notifyMap.get("transaction_id").toString();//微信支付订单号
					//String money_total=""+B_Base_pay.round(Double.parseDouble(total_fee)/100.00, 2);
					//********************业务处理************************
					if (D_Dao.Notify(order_number,pay_number,"wechat")) {
						System.out.println("订单状态修改：成功！");
						if (D_Dao.CbPay(order_number)) {
							System.out.println("子业务系统通知：成功！");
							C_Filter_pay.put("<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>", response);
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
					System.out.println(notifyMap.get("支付结果为：失败！"));//支付完成时间
				}
	    	} else {
		        // 签名错误，如果数据里没有sign字段，也认为是签名错误
				System.out.println(notifyMap.get("支付结果为：签名错误！"));//支付完成时间
		    }
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("异步处理异常！");
		}
	}

}
