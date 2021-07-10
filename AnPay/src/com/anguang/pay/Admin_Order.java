package com.anguang.pay;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.anpay.sdk.C_Filter_pay;

import database.D_Dao;

@WebServlet("/Admin_Order")
public class Admin_Order extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public Admin_Order() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		C_Filter_pay.Encoding(request, response);
		HttpSession session=request.getSession();
		if (session.getAttribute("Now_Admin")!=null) {
			String type=C_Filter_pay.get(request, "type");
			if (type.equals("search")) {
				String word=C_Filter_pay.get(request, "word");
				String sql="SELECT * FROM paylist order by id desc limit 300";
				if (!word.equals("all")) {
					sql="SELECT * FROM paylist WHERE order_number LIKE '%"+word+"%' or pay_order_number LIKE '%"+word+"%' or state_pay LIKE '%"+word+"%' or state_call"
						+ " LIKE '%"+word+"%' or project_id LIKE '%"+word+"%' or project_title LIKE '%"+word+"%' or money_remarks LIKE '%"+word+"%' or pay_type LIKE '%"+word+"%' or create_time LIKE '%"+word+"%' order by id desc";
				}
				String json=D_Dao.SelecttoJson(sql, null);
				C_Filter_pay.put(json, response);
			}
		} else {
			C_Filter_pay.put("n_login", response);
		}
	}

}
