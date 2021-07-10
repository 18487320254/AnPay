package com.alipay.sdk;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.anpay.sdk.B_Pay_Order;
import com.anpay.sdk.C_Filter_pay;

import database.D_Dao;

//支付宝手机支付
@WebServlet("/AlipayH5")
public class AlipayH5 extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public AlipayH5() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		C_Filter_pay.Encoding(request, response);
		String order_number=C_Filter_pay.get(request, "order_number");
		B_Pay_Order b_pay_order=D_Dao.GetPayOrderBean(order_number);
		if (b_pay_order!=null&&b_pay_order.getOrder_number().equals(order_number)) {
			//*****************************************************
			// 商户订单号，商户网站订单系统中唯一订单号，必填
		    String out_trade_no = b_pay_order.getOrder_number();
			// 订单名称，必填
		    String subject = b_pay_order.getTitle();
		    // 付款金额，必填
		    String total_amount=b_pay_order.getMoney_total();
		    // 商品描述，可空
		    String body = "";
		    // 超时时间 可空
		    String timeout_express="";//2m
		    // 销售产品码 必填
		    String product_code="QUICK_WAP_WAY";
		    
		    // SDK 公共请求类，包含公共请求参数，以及封装了签名与验签，开发者无需关注签名与验签     
		    //调用RSA签名方式
		    AlipayClient client = new DefaultAlipayClient(AlipayConfig.URL, AlipayConfig.APPID, AlipayConfig.RSA_PRIVATE_KEY, AlipayConfig.FORMAT, AlipayConfig.CHARSET, AlipayConfig.ALIPAY_PUBLIC_KEY,AlipayConfig.SIGNTYPE);
		    AlipayTradeWapPayRequest alipay_request=new AlipayTradeWapPayRequest();
		    // 封装请求支付信息
		    AlipayTradeWapPayModel model=new AlipayTradeWapPayModel();
		    model.setOutTradeNo(out_trade_no);
		    model.setSubject(subject);
		    model.setTotalAmount(total_amount);
		    model.setBody(body);
		    model.setTimeoutExpress(timeout_express);
		    model.setProductCode(product_code);
		    alipay_request.setBizModel(model);
		    // 设置异步通知地址
		    alipay_request.setNotifyUrl(AlipayConfig.notify_url);
		    // 设置同步地址
		    alipay_request.setReturnUrl(b_pay_order.getReturn_url());   
		    
		    // form表单生产
		    String form = "";
			try {
				// 调用SDK生成表单
				form = client.pageExecute(alipay_request).getBody();
				response.setContentType("text/html;charset=" + AlipayConfig.CHARSET); 
			    response.getWriter().write(form);//直接将完整的表单html输出到页面 
			    response.getWriter().flush(); 
			    response.getWriter().close();
			} catch (AlipayApiException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		} else {
			C_Filter_pay.put("n_pay_wait", response);//无效订单或订单超时
		}
	}

}
