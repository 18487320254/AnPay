package com.wepay.sdk;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSONObject;
import com.anpay.sdk.B_Base_pay;
import com.anpay.sdk.B_Pay_Order;
import com.anpay.sdk.C_Filter_pay;
import com.anpay.sdk.HttpUtil;
import com.wepay.sdk.WXPayConstants.SignType;

import database.D_Dao;

//微信内部公众号支付
@WebServlet("/WePayJSAPI")
public class WePayJSAPI extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public WePayJSAPI() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		C_Filter_pay.Encoding(request, response);
		String type=C_Filter_pay.get(request, "type");
		HttpSession session=request.getSession();
			//*****************************************************
			if (type.equals("get_code")) {//获取授权code
				String order_number=C_Filter_pay.get(request, "order_number");
				System.out.println(order_number);
				B_Pay_Order b_pay_order=D_Dao.GetPayOrderBean(order_number);
				session.setAttribute("Ancode_Pay_Order", b_pay_order);
				if (b_pay_order!=null) {
					String redirect_uri=URLEncoder.encode(WepayConfig.redirect_uri, "UTF-8");
					String wepaypage="https://open.weixin.qq.com/connect/oauth2/authorize?"
							+ "appid=wx9a2437aea02fc832&redirect_uri="+redirect_uri+"&response_type=code&scope=snsapi_base&state=1239753501#wechat_redirect";
					response.sendRedirect(wepaypage);
				} else {
					C_Filter_pay.put("n_pay_wait", response);//无效订单或订单超时
				}
			} else if(type.equals("set_code")) {
				B_Pay_Order b_pay_order=(B_Pay_Order) session.getAttribute("Ancode_Pay_Order");
				session.removeAttribute("Ancode_Pay_Order");
				if (b_pay_order!=null) {
					String code=request.getParameter("code");
					System.out.println("成功授权！----"+code);
					String url="https://api.weixin.qq.com/sns/oauth2/access_token?appid=wx9a2437aea02fc832&secret="+WepayConfig.secret+"&code="+code+"&grant_type=authorization_code";
					try {
			            String result = HttpUtil.sendGet(url, "utf-8");
			        	JSONObject jsonObject = JSONObject.parseObject(result);
			        	if (jsonObject.getString("openid")!=null) {
				        	System.out.println("成功获取openid！----"+jsonObject.getString("openid"));
				        	//发起下单******************
				            MyConfig config = new MyConfig();
				            WXPay wxpay = new WXPay(config);
				            Map<String, String> data = new HashMap<String, String>();
				            data.put("body", b_pay_order.getTitle());
				            data.put("out_trade_no", b_pay_order.getOrder_number());
				            data.put("nonce_str", B_Base_pay.Code(32));
				            data.put("fee_type", "CNY");
					        String totalstr=((int)(Double.parseDouble(b_pay_order.getMoney_total())*100))+"";
					        System.out.println("total_fee="+totalstr);
				            data.put("spbill_create_ip", B_Base_pay.getIP(request));
				            data.put("notify_url", WepayConfig.notify_url);
				            data.put("trade_type", "JSAPI");  // 此处指定为公众号/微信内支付
				            data.put("openid", jsonObject.getString("openid"));  // openid
				            data.put("total_fee", totalstr);
				            try {
				                Map<String, String> resp = wxpay.unifiedOrder(data);
				                System.out.println("prepay_id="+resp.get("prepay_id"));
				                String appId=WepayConfig.AppID;
				                String timeStamp=System.currentTimeMillis()+"";
				                String nonceStr=B_Base_pay.Code(32);
				                String parpackage="prepay_id="+resp.get("prepay_id");
				                String signType="MD5";
					            Map<String, String> data1 = new HashMap<String, String>();
					            data1.put("appId", appId);
					            data1.put("timeStamp", timeStamp);
					            data1.put("nonceStr", nonceStr);
					            data1.put("package", parpackage);
					            data1.put("signType", signType);
					            //生成签名
				                String sign=WXPayUtil.generateSignature(data1, WepayConfig.Key, SignType.MD5);
				                String json="{\"appId\":\""+appId+"\",\"timeStamp\":\""+timeStamp+"\",\"nonceStr\":\""+nonceStr+"\",\"package\":\""+parpackage+"\",\"signType\":\"MD5\",\"paySign\":\""+sign+"\"}";
				                C_Filter_pay.put(json, response);
				            } catch (Exception e) {
				                e.printStackTrace();
				                C_Filter_pay.put("n_do", response);
				            }
				        	//************************
						}else {
							System.out.println("openid获取错误！");
				            C_Filter_pay.put("n_do", response);
						}
			        } catch (Exception e) {
			            e.printStackTrace();
			            C_Filter_pay.put("n_do", response);
			        }
				} else {
					C_Filter_pay.put("n_pay_wait", response);//无效订单或订单超时
				}
			}
	}

}
