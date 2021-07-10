package com.wepay.sdk;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.anpay.sdk.B_Pay_Order;
import com.anpay.sdk.C_Filter_pay;

//检测是否存在有效待支付订单
@WebServlet("/IsPayWait")
public class IsPayWait extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public IsPayWait() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		C_Filter_pay.Encoding(request, response);
		HttpSession session=request.getSession();
		B_Pay_Order b_pay_order=(B_Pay_Order) session.getAttribute("Ancode_Pay_Order");
		if (b_pay_order!=null) {
			C_Filter_pay.put("yes", response);
		} else {
			C_Filter_pay.put("n_wait", response);
		}
	}

}
