package com.anpay.sdk;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
//极速数据API通用请求类
public class HttpUtil {
    private static String USER_AGENT = "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0; Trident/4.0; SLCC1; .NET CLR 2.0.50727; .NET CLR 3.0.04506; customie8)";
 
    // HTTP GET request
    public static String sendGet(String url, String charset) throws Exception {
        URL realurl = new URL(url);
        HttpURLConnection con = (HttpURLConnection) realurl.openConnection();
 
        // optional default is GET
        con.setRequestMethod("GET");
 
        // add request header
        con.setRequestProperty("User-Agent", USER_AGENT);
 
        // int responseCode = con.getResponseCode();
        // System.out.println("Response Code : " + responseCode);
 
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), charset));
        String inputLine;
        StringBuffer result = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            result.append(inputLine);
            result.append("\r\n");
        }
        in.close();
        con.disconnect();
        return result.toString();
    }

    // HTTP GET request
    public static String sendPost(String url, String charset) throws Exception {
        URL realurl = new URL(url);
        HttpURLConnection con = (HttpURLConnection) realurl.openConnection();
 
        // optional default is GET
        con.setRequestMethod("Post");
 
        // add request header
        con.setRequestProperty("User-Agent", USER_AGENT);
 
        // int responseCode = con.getResponseCode();
        // System.out.println("Response Code : " + responseCode);
 
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), charset));
        String inputLine;
        StringBuffer result = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            result.append(inputLine);
            result.append("\r\n");
        }
        in.close();
        con.disconnect();
        return result.toString();
    }
}
