package com.wepay.sdk;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.anpay.sdk.C_Filter_pay;

import database.D_Dao;

//微信扫码支付结果检查
@WebServlet("/NativeIspayed")
public class NativeIspayed extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public NativeIspayed() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		C_Filter_pay.Encoding(request, response);
		String order_number=C_Filter_pay.get(request, "order_number");
		//检查业务逻辑
		String sql="select * from paylist where order_number=? and state_pay='y'";
		String par=order_number;
		if (D_Dao.NoExist(sql, par)) {
			C_Filter_pay.put("n_pay", response);
		} else {
			C_Filter_pay.put("yes", response);
		}
	}

}
