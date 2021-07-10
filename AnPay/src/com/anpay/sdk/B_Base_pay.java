package com.anpay.sdk;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

//支持基础操作例如Array、list、Vector、数字处理、随机算法等的常用操作
public class B_Base_pay {
	//获取当前系统时间
	public static String Time(){
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time=df.format(System.currentTimeMillis());
		return time;
	}
	//生成随机码,字母或数字
	public static String Code(int length){
		String s="";
		for (int i = 0; i < length; i++) {
			s+=C_Cmd_pay.lettersNow[Rand(0,42)];
		}
		return s;
	}
	//获取客户端IP
    public static String getIP(HttpServletRequest request) { 
  	  	if (request.getHeader("x-forwarded-for") == null) { 
  	  		return request.getRemoteAddr(); 
  	  	} 
  	  	return request.getHeader("x-forwarded-for"); 
	}
	//生成6位验证码
	public static String Code(){
		int i=0;
		Random random=new Random();
		i=random.nextInt(899999)+100000;
		String s=Integer.toString(i);
		return s;
	}
	
	//生成14位系统时间标记
	public static String Timerm(){
		SimpleDateFormat df=new SimpleDateFormat("yyyyMMddHHmmss");
		String time=df.format(System.currentTimeMillis());
		return time;
	}
	//生成二维码
	public static String QrCode(String s){
	    String texts = s;
		QRCodeWriter qrCodeWriter = new QRCodeWriter();
	    BitMatrix bitMatrix;
		try {
			bitMatrix = qrCodeWriter.encode(texts, BarcodeFormat.QR_CODE, 600, 600);
		    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
		    Base64.Encoder encoder = Base64.getEncoder();
		    String text = encoder.encodeToString(outputStream.toByteArray());
			return text;
		} catch (WriterException e1) {
			e1.printStackTrace();
			return "n_do";
		} catch (IOException e) {
			e.printStackTrace();
			return "n_do";
		}
	}
	
	//生成指定整数之间的一个随机数(包含star，不包含end)
	public static int Rand(int star,int end){
		int i=0;
		Random random=new Random();
		int x=end-star;
		i=random.nextInt(x)+star;
		return i;
	}

	//精准加法
	public static double add(double v1,double v2){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
	}
	
	//精准减法，vi为被减数
    public static double sub(double v1,double v2){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    }
    
    //精准乘法
    public static double mul(double v1,double v2){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }
    
	//精准除法运算，v1为被除数，当发生除不尽的情况时，由scale参数指定精度，以后的数字四舍五入。
    public static double div(double v1,double v2,int scale){
        if(scale<0){
            throw new IllegalArgumentException("精确位数必须大于等于0");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2,scale,BigDecimal.ROUND_HALF_UP).doubleValue();
    }
    
    //精确的四舍五入处理
    public static double round(double v,int scale){
        if(scale<0){
            throw new IllegalArgumentException("精确位数必须大于等于0");
        }
        BigDecimal b = new BigDecimal(Double.toString(v));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one,scale,BigDecimal.ROUND_HALF_UP).doubleValue();
    }
    

	//发起http请求
    public static String httpGet(String redirect_url,String order_number,String state,String pay_type,String pay_order_number) {
        String url = redirect_url + "?order_number=" + order_number + "&state=" 
        		+ state + "&pay_type=" + pay_type + "&pay_order_number=" + pay_order_number;
        try {
            String result = HttpUtil.sendGet(url, "utf-8");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return "n_do";
        }
    }
}
