package database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.anpay.sdk.B_Base_pay;
import com.anpay.sdk.B_Pay_Order;
import com.anpay.sdk.B_time_pay;

//基础数据库操作
public class D_Dao {
	//异步修改订单状态
	public static boolean Notify(String order_number,String pay_number,String paytype) {
		boolean b=false;
		String sql="update paylist set state_pay=?,pay_order_number=?,pay_time=?,pay_type=? where order_number=?";
		String par="y,"+pay_number+","+B_time_pay.Time()+","+paytype+","+order_number;
		if (Up(sql, par)) {
			b=true;
		}
		return b;
	}
	//异步通知业务系统支付状态
	public static boolean CbPay(String order_number) {
		String sql="select * from paylist where order_number=?";
		Connection conn=D_Base.Opendb();
		ResultSet rs=D_Dao.Select(conn, sql, order_number);
		try {
			if (rs.next()) {
				String re=B_Base_pay.httpGet(rs.getString("redirect_url"), order_number, "ok", rs.getString("pay_type"), rs.getString("pay_order_number"));
				System.out.println(re);
				if (re.toString().trim().equals("yes")) {
					sql="update paylist set state_call='y' where order_number=?";
					D_Dao.Up(conn, sql, order_number);
					D_Base.Closedb(conn);
					return true;
				}else {
					D_Base.Closedb(conn);
					return false;
				}
			}else {
				D_Base.Closedb(conn);
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			D_Base.Closedb(conn);
			return false;
		}
	}
	
	public static boolean NoExist(String sql,String parameter) {
		boolean b=true;
		Connection conn=D_Base.Opendb();
		try {
			java.sql.PreparedStatement st = conn.prepareStatement(sql);
			if (parameter!=null) {
				String[] pars=parameter.split(",");
				for (int i = 0; i < pars.length; i++) {
					st.setString(i+1, pars[i]);
				}
			}
			ResultSet rs=st.executeQuery();
			if (rs.next()) {
				b=false;
			}
			rs.close();
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		D_Base.Closedb(conn);
		return b;
	} 
	public static boolean NoExist(Connection conn,String sql,String parameter) {
		boolean b=true;
		try {
			java.sql.PreparedStatement st = conn.prepareStatement(sql);
			if (parameter!=null) {
				String[] pars=parameter.split(",");
				for (int i = 0; i < pars.length; i++) {
					st.setString(i+1, pars[i]);
				}
			}
			ResultSet rs=st.executeQuery();
			if (rs.next()) {
				b=false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return b;
	} 
	
	//SQL执行器-select操作-支持连接自主管理(为防止sql注入攻击，用户赋值值参数必须?占位，参数列间英文逗号隔开,同时请注意释放数据库资源)
	public static ResultSet Select(Connection conn,String sql,String parameter) {
		ResultSet rs=null;
		try {
			java.sql.PreparedStatement st = conn.prepareStatement(sql);
			if (parameter!=null) {
				String[] pars=parameter.split(",");
				for (int i = 0; i < pars.length; i++) {
					st.setString(i+1, pars[i]);
				}
			}
			rs=st.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}
	
	//SQL执行器-select单参数查询操作-不支持连接自主管理(为防止sql注入攻击，用户赋值值参数必须?占位，参数列间英文逗号隔开,同时请注意释放数据库资源)
	public static String Select(String sql,String parameter,String returnname) {
		String text=null;
		Connection conn=D_Base.Opendb();
		try {
			java.sql.PreparedStatement st = conn.prepareStatement(sql);
			if (parameter!=null) {
				String[] pars=parameter.split(",");
				for (int i = 0; i < pars.length; i++) {
					st.setString(i+1, pars[i]);
				}
			}
			ResultSet rs=st.executeQuery();
			if (rs.next()) {
				text=rs.getString(returnname);
			}
			rs.close();
			st.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return text;
	}
	
	//SQL执行器-select单参数查询操作-支持连接自主管理(为防止sql注入攻击，用户赋值值参数必须?占位，参数列间英文逗号隔开,同时请注意释放数据库资源)
	public static String Select(Connection conn,String sql,String parameter,String returnname) {
		String text=null;
		try {
			java.sql.PreparedStatement st = conn.prepareStatement(sql);
			if (parameter!=null) {
				String[] pars=parameter.split(",");
				for (int i = 0; i < pars.length; i++) {
					st.setString(i+1, pars[i]);
				}
			}
			ResultSet rs=st.executeQuery();
			if (rs.next()) {
				text=rs.getString(returnname);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return text;
	}

	//SQL执行器-支持连接自主管理-up、in、de操作(为防止sql注入攻击，用户赋值值参数必须?占位，参数列间英文逗号隔开，,同时请注意释放数据库资源)
	public static boolean Up(Connection conn,String sql,String parameter) {
		boolean b=true;
		try {
			java.sql.PreparedStatement st = conn.prepareStatement(sql);
			if (parameter!=null) {
				String[] pars=parameter.split(",");
				for (int i = 0; i < pars.length; i++) {
					st.setString(i+1, pars[i]);
				}
			}
			st.executeUpdate();
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
			b=false;
		}
		return b;
	}

	//SQL执行器-不支持连接自主管理-up、in、de操作(为防止sql注入攻击，用户赋值值参数必须?占位，参数列间英文逗号隔开)
	public static boolean Up(String sql,String parameter) {
		boolean b=true;
		Connection conn=D_Base.Opendb();
		try {
			java.sql.PreparedStatement st = conn.prepareStatement(sql);
			if (parameter!=null) {
				String[] pars=parameter.split(",");
				for (int i = 0; i < pars.length; i++) {
					st.setString(i+1, pars[i]);
				}
			}
			st.executeUpdate();
			st.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			b=false;
		}
		return b;
	}

	//SQL执行器-不支持连接自主管理-up、in、de操作+返回id(为防止sql注入攻击，用户赋值值参数必须?占位，参数列间英文逗号隔开)
	public static int reUp(String sql,String parameter) {
		int id=0;
		Connection conn=D_Base.Opendb();
		try {
			java.sql.PreparedStatement st = conn.prepareStatement(sql);
			if (parameter!=null) {
				String[] pars=parameter.split(",");
				for (int i = 0; i < pars.length; i++) {
					st.setString(i+1, pars[i]);
				}
			}
			st.executeUpdate();
			ResultSet rSet=st.executeQuery("select LAST_INSERT_ID()");
			while (rSet.next()) {
				id=rSet.getInt("LAST_INSERT_ID()");
			}
			st.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return id;
	}

	//查询并将结果封装为json格式字符串-支持连接自主管理(为防止sql注入攻击，用户赋值值参数必须?占位，参数列间英文逗号隔开,同时请注意释放数据库资源)
	public static String SelecttoJson(Connection conn,String sql,String parameter){
		String json="";
		ResultSet rs=Select(conn, sql, parameter);
		json=JSON.toJSONString(rs);
		return json;
	}

	//查询并将结果封装为json格式字符串-不支持连接自主管理(为防止sql注入攻击，用户赋值值参数必须?占位，参数列间英文逗号隔开)
	public static String SelecttoJson(String sql,String parameter){
		String json="";
		Connection conn=D_Base.Opendb();
		try {
			java.sql.PreparedStatement st = conn.prepareStatement(sql);
			if (parameter!=null) {
				String[] pars=parameter.split(",");
				for (int i = 0; i < pars.length; i++) {
					st.setString(i+1, pars[i]);
				}
			}
			ResultSet rs=st.executeQuery();
			json=JSON.toJSONString(resultSetToJSON(rs));
			rs.close();
			st.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			json="n_sql";
		}
		return json;
	}
	//查询结果转json对象
	public static JSON resultSetToJSON(ResultSet resultSet) {
		//数据集JSON格式
		JSONArray jsonArray = new JSONArray();
		//数据库中每行的数据对象
		JSONObject rowObj = null;
	      try {
	    	  //ResultSetMetaData 有关 ResultSet 中列的名称和类型的信息。
	          ResultSetMetaData rsmd = resultSet.getMetaData();
	          //遍历数据集
	          while (resultSet.next()) {
	        	  //每读取一行，新建对象
	              rowObj = new JSONObject();
	              //获取表列数
	              int columnCount = rsmd.getColumnCount();
	              //列从1开始，要等于
	              for (int i = 1; i <= columnCount; i++) {
	            	  //获取列名
	                  String columnName = rsmd.getColumnName(i);
	                  //取数据
	                  String value = resultSet.getString(columnName);
	                  //添加到rowObj中
	                  rowObj.put(columnName, value);
	              }
	              //添加到数据集Json
	              jsonArray.add(rowObj);
	          }
	      } catch (SQLException e) {
	          e.printStackTrace();
	      }
	      return jsonArray;
	}
	

	//获取数据表名
    public static String getDataTableName(String database) {
		Connection conn=D_Base.Opendb();
        String tables = "tables";
        try {
            DatabaseMetaData databaseMetaData = conn.getMetaData();
            ResultSet resultSet = databaseMetaData.getTables(database,"root",null,new String[]{"TABLE"});
            while (resultSet.next()){
            	tables+=","+resultSet.getString("TABLE_NAME");
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tables;
    }
    
    public static B_Pay_Order GetPayOrderBean(String order_number) {
		B_Pay_Order b_pay_order=new B_Pay_Order();
		String sql="select order_number,money_total,money_remarks from paylist where order_number=?";
		Connection conn=D_Base.Opendb();
		ResultSet rs=Select(conn, sql, order_number);
		try {
			if (rs.next()) {
				b_pay_order.setMoney_total(rs.getString("money_total"));
				b_pay_order.setOrder_number(rs.getString("order_number"));
				b_pay_order.setTitle(rs.getString("money_remarks"));
				return b_pay_order;
			}else {
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
    
    //首写字母变大写
	public static String firstUpperCase(String old){
	    return old.substring(0, 1).toUpperCase()+old.substring(1);
	}
	
}
