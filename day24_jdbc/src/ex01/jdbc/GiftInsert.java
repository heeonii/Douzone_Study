package ex01.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import connUtil.DBConnection;   // DB ���� ��ƿ��Ƽ 

public class GiftInsert {
	public static void main(String[] args) 
				throws SQLException {
		
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		Scanner sc = new Scanner(System.in);
		
//		1. ����̺� �ε�
		conn = DBConnection.getConnection();
//		conn.setAutoCommit(false);
		
//		2. Connection & Open
							 //driver:@IP:portNumber:SID(or ���������ͺ��̽���)   ==> "jdbc:oracle:thin:@localhost:1521:orcl";

//		3. ��� (DML ���) - insert 
		stmt = conn.createStatement();
		// insert into gift values(11,'��', 10, 200);
//		String sql = "INSERT INTO GIFT VALUES(12,'USB', 100, 2000)";
//		String sql = "INSERT INTO GIFT VALUES("+args[0] +",' " + args[1]+" ', " + args[2] +", " + args[3] +")"; 
		System.out.println("��ǰ��ȣ, ��ǰ��, ������, �ְ� �Է��ϼ���. ");
		int gno = sc.nextInt();
		String sql = "INSERT INTO GIFT VALUES("+gno +",' " + sc.next()+" ', " + sc.nextInt() +", " + sc.nextInt()+")"; 
		
		int result = stmt.executeUpdate(sql);
		System.out.println(result +" ������ �߰� ������!");
//		conn.commit();
//		4. �ڿ���ȯ(�ݱ�)
		stmt.close();		conn.close();
	}
}
