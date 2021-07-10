package com.anpay.sdk;

public class B_Pay_Order {
	private String order_number;
	private String money_total;
	private String title;
	private String redirect_url;
	private String return_url;
	
	public String getOrder_number() {
		return order_number;
	}
	public void setOrder_number(String order_number) {
		this.order_number = order_number;
	}
	public String getMoney_total() {
		return money_total;
	}
	public void setMoney_total(String money_total) {
		this.money_total = money_total;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getRedirect_url() {
		return redirect_url;
	}
	public void setRedirect_url(String redirect_url) {
		this.redirect_url = redirect_url;
	}
	public String getReturn_url() {
		return return_url;
	}
	public void setReturn_url(String return_url) {
		this.return_url = return_url;
	}
	
	
}
