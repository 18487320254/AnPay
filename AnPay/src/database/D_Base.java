package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
//系统数据库连接管理
public class D_Base {
	private static String driver="com.mysql.jdbc.Driver";//数据库驱动
	private static String url="jdbc:mysql://localhost:3306/anpay?useSSL=false&characterEncoding=utf-8&useOldAliasMetadataBehavior=true";//数据库地址访问
	private static String username="root";//用户名
	private static String password="xqsd.123";//密码
	static Connection conn=null;
	//建立数据库连接
	public static Connection Opendb(){
		try {
			Class.forName(driver);//调用驱动
			Connection conn=DriverManager.getConnection(url, username, password);//连接数据库
			return conn;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	//关闭数据库连接
	public static void Closedb(Connection conn){
		if(conn!=null){
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
