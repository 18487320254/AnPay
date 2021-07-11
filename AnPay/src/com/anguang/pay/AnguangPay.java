package com.anguang.pay;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.anpay.sdk.B_Base_pay;
import com.anpay.sdk.B_time_pay;
import com.anpay.sdk.C_Filter_pay;

//支付准备
@WebServlet("/AnguangPay")
public class AnguangPay extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public AnguangPay() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		C_Filter_pay.Encoding(request, response);
		//支付订单信息
		String money_total=C_Filter_pay.get(request, "money_total");
		String money_remarks=URLEncoder.encode(C_Filter_pay.get(request, "money_remarks"), "UTF-8");
		String redirect_url=URLEncoder.encode("http://pay.anguangkeji.com/AnNotify", "UTF-8");
		String return_url=URLEncoder.encode("http://pay.anguangkeji.com/payend.html", "UTF-8");
		String project_id="ANPAY";
		String project_title=URLEncoder.encode("桉光科技在线收银", "UTF-8");
		String order_number=project_id+B_Base_pay.Code(4)+B_time_pay.Timerm()+B_Base_pay.Code(4);
		//调起支付系统
		String wepaypage="http://pay.anguangkeji.com/AncodePay?"
				+ "money_total="+money_total+"&order_number="+order_number+"&money_remarks="+money_remarks+"&return_url="+return_url
				+"&redirect_url="+redirect_url+"&project_id="+project_id+"&project_title="+project_title;
		response.sendRedirect(wepaypage);
	}
}
