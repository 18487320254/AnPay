package com.anguang.pay;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.anpay.sdk.C_Filter_pay;

//业务支付回调
@WebServlet("/AnNotify")
public class AnNotify extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public AnNotify() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		C_Filter_pay.Encoding(request, response);
		System.out.println("业务回调正常(桉光在线收银)！");
		System.out.println(request.getQueryString());
		//业务逻辑
		//String state=C_Filter_pay.get(request, "state");
		//反馈处理结果
		C_Filter_pay.put("yes", response);
	}

}
