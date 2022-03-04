package ex01.jdbc;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import connUtil.DBConnection;

public class JDBCProjectEx3 extends JFrame implements ActionListener{
	//component 객체 선언
	JPanel panWest, panSouth;  //왼쪽텍스트필드, 아래쪽 버튼
	JPanel p1,p2,p3,p4,p5; 
	JTextField txtNo, txtName, txtEmail, txtPhone;
	JButton  btnTotal, btnAdd, btnDel, btnSearch, btnCancel;
	
	JTable table; //검색과 전체 보기를 위한 테이블 객체 생성
	//상태변화를 위한 변수 선언
	private static final int NONE = 0;
	private static final int ADD = 1;
	private static final int DELETE = 2;
	private static final int SEARCH = 3;
	private static final int TOTAL = 4;
	int cmd = NONE;
	
	MyModel model; // user class
	
	
	
	public JDBCProjectEx3(){
		setTitle("고객 정보 입력 폼");
		//component 등록
		panWest = new JPanel(new GridLayout(5, 0));
		p1 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		p1.add(new JLabel("번    호"));
		p1.add(txtNo = new JTextField(12));
		panWest.add(p1);
		
		p2 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		p2.add(new JLabel("이    름"));
		p2.add(txtName = new JTextField(12));
		panWest.add(p2);
		
		p3 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		p3.add(new JLabel("이  메  일"));
		p3.add(txtEmail = new JTextField(12));
		panWest.add(p3);
		
		p4 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		p4.add(new JLabel("전화번호"));
		p4.add(txtPhone = new JTextField(12));
		panWest.add(p4);
		
		p5 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		p5.add(new JLabel(""));
		panWest.add(p5);
		
		add(panWest, "West");
		
		//button 화면 아래 등록
		panSouth = new JPanel();
		panSouth.add(btnTotal= new JButton("전체보기")); 
		panSouth.add(btnAdd= new JButton("추     가"));
		panSouth.add(btnDel= new JButton("삭     제"));
		panSouth.add(btnSearch= new JButton("검     색"));
		panSouth.add(btnCancel= new JButton("취     소"));
		add(panSouth, "South");
		
		//event 처리
		btnTotal.addActionListener(this);
		btnAdd.addActionListener(this);
		btnDel.addActionListener(this);
		btnSearch.addActionListener(this);
		btnCancel.addActionListener(this);
		
		//테이블 객체 생성
		add(new JScrollPane(table = new JTable()), "Center");
		//close
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//메인 창 출력
		setBounds(100, 100, 700, 300); //setSize(W,H);   pack(); 
		setVisible(true);
		
		dbConnect();
		total();
	} //constuctor end
	
	
	//////// db setting////////
	Connection conn;
	Statement stmt;
	PreparedStatement pstmtInsert, pstmtDelete;
	PreparedStatement pstmtTotal, pstmtTotalScroll;
	PreparedStatement pstmtSearch, pstmtSearchScroll;
	
	private String sqlInsert = "INSERT INTO CUSTOMERS VALUES(?, ?, ?, ?)";
	private String sqlDelete = "DELETE FROM CUSTOMERS WHERE CODE = ?";
	private String sqlSelect = "SELECT * FROM CUSTOMERS";
	private String sqlSearch = "SELECT * FROM CUSTOMERS WHERE NAME = ?";
	
	public void dbConnect() {
		try {
			conn = DBConnection.getConnection(); // db연결
			
			pstmtInsert = conn.prepareStatement(sqlInsert);
			pstmtDelete = conn.prepareStatement(sqlDelete);
			pstmtTotal = conn.prepareStatement(sqlSelect);
			pstmtSearch = conn.prepareStatement(sqlSearch);
			
			pstmtTotalScroll = conn.prepareCall(sqlSelect,
								ResultSet.TYPE_SCROLL_SENSITIVE,
								ResultSet.CONCUR_UPDATABLE);
			
			pstmtSearchScroll = conn.prepareCall(sqlSearch,
							ResultSet.TYPE_SCROLL_SENSITIVE,
							ResultSet.CONCUR_UPDATABLE);
			
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace(); 
		}
	}
	
	///////////////////////////////////////////////////////////////
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		if( obj == btnAdd ){
			if( cmd != ADD ){
				setText(ADD);  //user method
				return;
			} //if in
			setTitle(e.getActionCommand());
			add();  //추가
			
		}else if( obj == btnDel ){
			if( cmd != DELETE ){
				setText(DELETE);  //user method
				return;
			} //if in
			setTitle(e.getActionCommand());
			del();  //삭제
			
		}else if( obj == btnSearch ){
			if( cmd != SEARCH ){
				setText(SEARCH);  //user method
				return;
			} //if in
			setTitle(e.getActionCommand());
			search();  //검색
			
		}else if( obj == btnTotal ){
			setTitle(e.getActionCommand());
			total();  //전체보기
		}
		setText(NONE);
		init(); //초기화 메소드, user method
	}// actionPerformed end
		

	
	/////// Button Event ////////////////////////
	
	private void total() { // 전체보기
		try {
			ResultSet rsScroll = pstmtTotalScroll.executeQuery();
			ResultSet rs = pstmtTotal.executeQuery();
			
			if( model == null ) model = new MyModel();
			
			model.getRowCount(rsScroll);
			model.setData(rs);
			
			//table.setModel(model);
			table.setModel(new DefaultTableModel(model.data, model.columnName));
			table.updateUI();
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
	} // total() end
	
	private void add() {
		try {
			String strNo = txtNo.getText();
			String strName = txtName.getText();
			String strMail = txtEmail.getText();
			String strPhone = txtPhone.getText();
			
			// System.out.println(strNo + ", " + strName + ", " + strMail + ", " + strPhone);
			
//			if( strNo.length() < 1 || strName.length() < 1) {
//				JOptionPane.showMessageDialog(null, "번호와 이름은 필수 사항입니다. 입력해주세요.");
//				return;
//			}
			
			switch( JOptionPane.showConfirmDialog(null, 
								"(" + strNo + ", " + strName + ", " + strMail + ", " + strPhone + ")",
								"추가하시겠습니까?",
								JOptionPane.YES_NO_CANCEL_OPTION) ) {
			
			case 0:
				break; // 확인
			case 1:
				break; // 아니오
			} // switch end
			
			pstmtInsert.setInt(1, Integer.parseInt(strNo));
			pstmtInsert.setString(2, strName);
			pstmtInsert.setString(3, strMail);
			pstmtInsert.setString(4, strPhone);
			
			pstmtInsert.executeUpdate();
			
		} catch (Exception e) {	e.printStackTrace(); }
		
		JOptionPane.showMessageDialog(null, "추가됐습니다.");
		
		total();
		init();
	}// add() end
	
	private void del() {
		// TODO Auto-generated method stub
		
	}
	
	private void search() {
		// TODO Auto-generated method stub
		String strName = txtName.getText();
		
		if( strName.length() < 1) {
			JOptionPane.showMessageDialog(null, "이름은 필수 입니다.");
			return;
		}
		
		try {
			pstmtSearchScroll.setString(1, strName);
			ResultSet rsScroll = pstmtSearchScroll.executeQuery();
			pstmtSearch.setString(1, strName);
			ResultSet rs = pstmtSearch.executeQuery();
			
			if( model == null ) model = new MyModel();
			model.getRowCount(rsScroll);
			model.setData(rs);
			table.setModel(model);
			table.updateUI();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	//////////////////////////////////////////////
	
	
	
	private void init() {  //초기화 메소드
		txtNo.setText("");			txtNo.setEditable(false);
		txtName.setText("");		txtName.setEditable(false);
		txtEmail.setText("K");		txtEmail.setEditable(false);
		txtPhone.setText("");		txtPhone.setEditable(false);
	}// init() end

	private void setText(int command) {
		switch(command){
			case ADD :
				txtNo.setEditable(true);
				txtName.setEditable(true);
				txtEmail.setEditable(true);
				txtPhone.setEditable(true);
				break;
			case DELETE :
				txtNo.setEditable(true);
				break;
			case SEARCH :
				txtName.setEditable(true);
				break;
		}//switch end
		
		setButton(command);  //user method
	}// setText() end

	private void setButton(int command) {
		//cancel button 제외하고 어떤 버튼이 눌리더라도 모든 버튼이 비활성화
		btnTotal.setEnabled(false);
		btnAdd.setEnabled(false);
		btnDel.setEnabled(false);
		btnSearch.setEnabled(false);
		
		switch(command){
			case ADD :
				btnAdd.setEnabled(true);
				cmd = ADD;
				break;
			case DELETE :
				btnDel.setEnabled(true);
				cmd = DELETE;
				break;
			case SEARCH :
				btnSearch.setEnabled(true);
				cmd = SEARCH;
				break;
			case TOTAL :
				btnTotal.setEnabled(true);
				cmd = TOTAL;
				break;
			case NONE :
				btnTotal.setEnabled(true);
				btnAdd.setEnabled(true);
				btnDel.setEnabled(true);
				btnSearch.setEnabled(true);
				btnCancel.setEnabled(true);  //
				cmd = NONE;
				break;
		}//switch end	
	}//setButton end

	public static void main(String[] args) {
		new JDBCProjectEx3();
	}
}




