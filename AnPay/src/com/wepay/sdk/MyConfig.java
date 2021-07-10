package com.wepay.sdk;

import java.io.*;

//SDK基础参数实现
public class MyConfig extends WXPayConfig {
	private byte[] certData;
	public MyConfig() throws Exception {
	    String certPath = WepayConfig.certPath;//证书
	    File file = new File(certPath);
	    InputStream certStream = new FileInputStream(file);
	    this.certData = new byte[(int) file.length()];
	    certStream.read(this.certData);
	    certStream.close();
	}
	public String getAppID() {
	    return WepayConfig.AppID;
	}
	public String getMchID() {
	    return WepayConfig.MchID;
	}
	public String getKey() {
	    return WepayConfig.Key;
	}
	public InputStream getCertStream() {
	    ByteArrayInputStream certBis = new ByteArrayInputStream(this.certData);
	    return certBis;
	}
	public int getHttpConnectTimeoutMs() {
	    return 8000;
	}
	public int getHttpReadTimeoutMs() {
	    return 10000;
	}
	@Override
	IWXPayDomain getWXPayDomain() {
		// TODO Auto-generated method stub
		return new IWXPayDomain() {
			
			@Override
			public void report(String domain, long elapsedTimeMillis, Exception ex) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public DomainInfo getDomain(WXPayConfig config) {
				// TODO Auto-generated method stub
				return new IWXPayDomain.DomainInfo(WXPayConstants.DOMAIN_API, true);
			}
		};
	}
}
