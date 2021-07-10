package com.anpay.sdk;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import database.D_Dao;

//支付准备
@WebServlet("/AncodePay")
public class AncodePay extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public AncodePay() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		C_Filter_pay.Encoding(request, response);
		System.out.println(request.getQueryString());
		//支付订单信息
		String project_id=C_Filter_pay.get(request, "project_id");
		String project_title=C_Filter_pay.get(request, "project_title");
		String order_number=C_Filter_pay.get(request, "order_number");
		String money_total=C_Filter_pay.get(request, "money_total");
		String money_remarks=URLDecoder.decode(C_Filter_pay.get(request, "money_remarks"), "UTF-8") ;
		String redirect_url=C_Filter_pay.get(request, "redirect_url");
		String return_url=C_Filter_pay.get(request, "return_url");
		try {
			//检查支付订单
			String sql="select * from paylist where order_number=?";
			if (D_Dao.NoExist(sql, order_number)) {
				//创建支付记录
				sql="insert into paylist(project_id,project_title,order_number,money_total,money_remarks,create_time,redirect_url,state_pay,state_call) values(?,?,?,?,?,?,?,?,?)";
				String par=project_id+","+project_title+","+order_number+","+money_total+","+money_remarks+","+B_Base_pay.Time()+","+redirect_url+",n,n";
				if (D_Dao.Up(sql, par)) {
					//调起前段支付
					String title=URLEncoder.encode(money_remarks, "UTF-8");
					String wepaypage="ancodepayindex.html?"
							+ "state=ancodepay&money_total="+money_total+"&order_number="+order_number+"&order_title="+title+"&return_url="+return_url;
					response.sendRedirect(wepaypage);
				} else {
					C_Filter_pay.put("<script>alert('支付请求异常！');</script>", response);
				}
			} else {
				C_Filter_pay.put("<script>alert('支付参数异常，请核对后再支付！');</script>", response);
			}
		} catch (Exception e) {
			e.printStackTrace();
			C_Filter_pay.put("<script>alert('支付请求异常！');</script>", response);
		}
	}
}
