package ex01.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MainEntry {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		// 1. 드라이브 로드
		Class.forName("oracle.jdbc.OracleDriver");
		System.out.println("driver load success!");
		
		// 2. Connection & Open
						//driver:@IP:portNumber:SID
		String url = "jdbc:oracle:thin:@edudb_high?TNS_ADMIN=D:\\Wallet_edudb";
		String uid = "admin";
		String pwd = "Dlgmldnjs1229!";
		
		Connection conn = DriverManager.getConnection(url, uid, pwd);
		System.out.println("Connection success");
		
				
		// 3. 사용 (DML 명령)
				
		//4. 자원반환 (닫기)
		

	}

}
