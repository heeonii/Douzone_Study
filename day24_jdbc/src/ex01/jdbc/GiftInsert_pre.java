package ex01.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import connUtil.DBConnection;   // DB ���� ��ƿ��Ƽ 

public class GiftInsert_pre {
	public static void main(String[] args) 	throws SQLException {
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Scanner sc = new Scanner(System.in);
		
//		1. ����̺� �ε�
		conn = DBConnection.getConnection();
//		conn.setAutoCommit(false);
		
//		2. Connection & Open
							 //driver:@IP:portNumber:SID(or ���������ͺ��̽���)   ==> "jdbc:oracle:thin:@localhost:1521:orcl";

//		3. ��� (DML ���) - insert 
		ps = conn.prepareStatement( "INSERT INTO GIFT VALUES( ?, ?, ?, ? )");
		System.out.println("��ǰ��ȣ, ��ǰ��, ������, �ְ� �Է��ϼ���? ");
		int gno = sc.nextInt();
		String gname = sc.next();
//		int g_s = sc.nextInt();
		int g_e = sc.nextInt();

		ps.setInt(1, gno);
		ps.setString(2, gname);
		ps.setInt(3, sc.nextInt());
		ps.setInt(4, g_e);
		
		int result = ps.executeUpdate();  //

		System.out.println(result +" ������ �߰� ������!");
		System.out.println(gname +" ��ǰ�� �߰� �Ǿ����ϴ�. ");
//		conn.commit();
//		4. �ڿ���ȯ(�ݱ�)
		ps.close();		conn.close();
	}
}
