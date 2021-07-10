package com.wepay.sdk;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.anpay.sdk.B_Base_pay;
import com.anpay.sdk.B_Pay_Order;
import com.anpay.sdk.C_Filter_pay;

import database.D_Dao;
//微信h5支付
@WebServlet("/WePayH5")
public class WePayH5 extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public WePayH5() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		C_Filter_pay.Encoding(request, response);
		String order_number=C_Filter_pay.get(request, "order_number");
		B_Pay_Order b_pay_order=D_Dao.GetPayOrderBean(order_number);
		if (b_pay_order!=null&&b_pay_order.getOrder_number().equals(order_number)) {
			//*****************************************************
			try {
		        MyConfig config = new MyConfig();
		        WXPay wxpay = new WXPay(config);
		        Map<String, String> data = new HashMap<String, String>();
		        data.put("body", b_pay_order.getTitle());
		        data.put("out_trade_no", b_pay_order.getOrder_number());
		        data.put("nonce_str", B_Base_pay.Code(32));
		        data.put("fee_type", "CNY");
		        String totalstr=((int)(Double.parseDouble(b_pay_order.getMoney_total())*100))+"";
		        System.out.println(totalstr);
		        data.put("total_fee", totalstr);
		        data.put("spbill_create_ip", B_Base_pay.getIP(request));
		        data.put("notify_url", WepayConfig.notify_url);
		        data.put("trade_type", "MWEB");  // 此处指定为H5支付
		        String scene_info="{\"h5_info\":{\"type\": \"Wap\",\"wap_url\": \""+b_pay_order.getReturn_url()+"\",\"wap_name\": \"订单支付\"}}";
		        data.put("scene_info", scene_info);
		        try {
		            Map<String, String> resp = wxpay.unifiedOrder(data);
		            System.out.println(resp);
		            if (resp.get("result_code").equals("SUCCESS")) {
			            System.out.println(resp.get("prepay_id"));
			            C_Filter_pay.put(resp.get("mweb_url"), response);
					}
		        } catch (Exception e) {
		            e.printStackTrace();
					System.out.println("统一下单失败！");
		            C_Filter_pay.put("n_do", response);
		        }
			} catch (Exception e1) {
				e1.printStackTrace();
				System.out.println("统一下单失败！");
	            C_Filter_pay.put("n_do", response);
			}
		} else {
			C_Filter_pay.put("n_pay_wait", response);//无效订单或订单超时
		}
	}

}
