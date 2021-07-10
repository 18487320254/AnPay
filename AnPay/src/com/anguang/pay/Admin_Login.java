package com.anguang.pay;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.anpay.sdk.C_Filter_pay;


@WebServlet("/Admin_Login")
public class Admin_Login extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public Admin_Login() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		C_Filter_pay.Encoding(request, response);
		String type=C_Filter_pay.get(request, "type");
		if (type.equals("login")) {
			String id=C_Filter_pay.get(request, "id");
			String psd=C_Filter_pay.get(request, "psd");
			if (id.equals("admin_system")&&psd.equals("anguangpay.anguangyun.com")) {
				HttpSession session=request.getSession();
				session.setAttribute("Now_Admin", "admin_system");
				C_Filter_pay.put("yes", response);
			} else {
				C_Filter_pay.put("n_psd", response);
			}
		} else {
			HttpSession session=request.getSession();
			session.invalidate();
			C_Filter_pay.put("yes", response);
		}
	}

}
