package ex01.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import connUtil.DBConnection;   // DB ���� ��ƿ��Ƽ 

public class GiftSelect {
	public static void main(String[] args) 
				throws SQLException {
		
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		
//		1. ����̺� �ε�
		conn = DBConnection.getConnection();
		
//		2. Connection & Open
							 //driver:@IP:portNumber:SID(or ���������ͺ��̽���)   ==> "jdbc:oracle:thin:@localhost:1521:orcl";

//		3. ��� (DML ���) - select 
		stmt = conn.createStatement();
		rs = stmt.executeQuery("SELECT * FROM GIFT");  // ctrl+shift+X
		
		System.out.println("��ǰ��ȣ\t\t��ǰ��\t\t������\t�ְ�");
		
		while (rs.next()) {
			int gno = rs.getInt(1);     
			String gname = rs.getString("gname");  // get�ڷ���("�ʵ��"); ������
			int g_s = rs.getInt(3);
			int g_e = rs.getInt(4);
			
			System.out.println(gno+"\t\t"+gname+"\t\t  "+g_s+"\t"+g_e);
		}
		
//		4. �ڿ���ȯ(�ݱ�)
		rs.close();		stmt.close();		conn.close();
	}
}
