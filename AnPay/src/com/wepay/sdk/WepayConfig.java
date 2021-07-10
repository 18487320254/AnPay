package com.wepay.sdk;

public class WepayConfig {
	public static String certPath="c:/apiclient_cert.p12";//API证书
	public static String AppID="wx9a2437aea02fc832";//APPID
	public static String MchID="1601539266";//商户号
	public static String Key="6E1F15C7048B7DD626D2657223782F62";//商户API密钥
	public static String notify_url="http://pay.anguangkeji.com/WePayNotify";//异步通知地址
	public static String scene_info="{\"h5_info\":{\"type\": \"Wap\",\"wap_url\": \"http://pay.anguangkeji.com/payend.html\",\"wap_name\": \"订单支付\"}}";//支付场景（仅用于H5）
	public static String secret="f74d2ea9d646f39bc97bcee0398838a7";//开发者密码AppSecret(仅用于JSAPI)
	public static String redirect_uri="http://pay.anguangkeji.com/ancodepayindex.html";//JSAPI支付获取code后回调
}
