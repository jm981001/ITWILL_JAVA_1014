import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import org.apache.ibatis.cache.decorators.SerializedCache.CustomObjectInputStream;

public class CustomerGUI {

	private JFrame f;
	
	// North 영역
	private JTextField sName, sAge, sJumin; // 조회조건
	private JButton btnSelect;
	
	// Center 영역
	private JTable table;
	
	// West 영역
	private JTextField tfIdx, tfName, tfAge, tfEmail, tfJumin;
	
	// South 영역
	private JButton btnInsert, btnDelete;
	
	public CustomerGUI() {
		showFrame();
	}
	
	public void showFrame() {
		f = new JFrame("고객 관리 프로그램");
		f.setBounds(500, 300, 900, 300);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// ------- North 영역 (조회조건) --------
		JPanel pNorth = new JPanel();
		f.add(pNorth, BorderLayout.NORTH);
		pNorth.setLayout(new GridLayout(0, 4, 0, 0));
		
		// 조회 조건 sName 입력 패널
		JPanel panelName = new JPanel();
		pNorth.add(panelName);
		
		panelName.add(new JLabel("이름"));
		sName = new JTextField();
		panelName.add(sName);
		sName.setColumns(10);
		
		// 조회 조건 sAge 입력 패널
		JPanel panelAge = new JPanel();
		pNorth.add(panelAge);
		
		panelAge.add(new JLabel("나이"));
		sAge = new JTextField();
		panelAge.add(sAge);
		sAge.setColumns(10);
		
		// 조회 조건 sJumin 입력 패널
		JPanel panelJumin = new JPanel();
		pNorth.add(panelJumin);
		
		panelJumin.add(new JLabel("주민번호"));
		sJumin = new JTextField();
		panelJumin.add(sJumin);
		sJumin.setColumns(10);
		
		// 조회 버튼
		btnSelect = new JButton("조회");
		pNorth.add(btnSelect);
		// -----------------------------------
		
		// ------- WEST 영역 (회원 정보 입력 영역) --------
		JPanel pWest = new JPanel(new GridLayout(5, 0, 0, 0));
		f.add(pWest, BorderLayout.WEST);
		
		// 번호(IDX) 패널
		JPanel pIdx = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		pWest.add(pIdx);
		
		pIdx.add(new JLabel("번 호"));
		tfIdx = new JTextField(10);
		// 번호 입력 불가능하도록 잠금
		tfIdx.setEditable(false);
		pIdx.add(tfIdx);
		
		// 이름(NAME) 패널
		JPanel pName = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		pWest.add(pName);
		
		pName.add(new JLabel("이 름"));
		tfName = new JTextField(10);
		pName.add(tfName);
		
		// 나이(AGE) 패널
		JPanel pAge = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		pWest.add(pAge);
		
		pAge.add(new JLabel("나 이"));
		tfAge = new JTextField(10);
		pAge.add(tfAge);
		
		// 이메일(EMAIL) 패널
		JPanel pEmail = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		pWest.add(pEmail);
		
		pEmail.add(new JLabel("E-Mail"));
		tfEmail = new JTextField(10);
		pEmail.add(tfEmail);
		
		// 주민번호(JUMIN) 패널
		JPanel pJumin = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		pWest.add(pJumin);
		
		pJumin.add(new JLabel("주민번호"));
		tfJumin = new JTextField(10);
		pJumin.add(tfJumin);
		// -------------------------------------------
		
		// ------- CENTER 영역 (회원 정보 표시 영역) --------
		
		JScrollPane scrollPane = new JScrollPane();
		f.add(scrollPane);
		table = new JTable();
		scrollPane.setViewportView(table);
		
		// 테이블 옵션 설정
		// 셀 위치 이동 불가 설정
		table.getTableHeader().setReorderingAllowed(false);
		
		// 컬럼 제목을 표시하기 위해 DefaultTableModel 객체 생성
		// 배열을 사용하여 제목을 생성한 뒤 Model 객체에 추가
		String[] columnNames = {"번 호", "이 름", "나 이", "E-Mail", "주민번호"};
		DefaultTableModel dtm = new DefaultTableModel(columnNames, 0);
		table.setModel(dtm);
		// ---------------------------------------------
		
		// ------- SOUTH 영역 (버튼 영역) --------
		JPanel pSouth = new JPanel();
		f.add(pSouth, BorderLayout.SOUTH);
		
		btnInsert = new JButton("회원추가");
		btnDelete = new JButton("회원삭제");
		
		pSouth.add(btnInsert);
		pSouth.add(btnDelete);
		// ------------------------------------
		
		// 버튼 이벤트 리스너 연결
		btnSelect.addActionListener(e -> select());
		btnInsert.addActionListener(e -> insert());
		btnDelete.addActionListener(e -> delete());
		
		KeyAdapter adapter = new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) select(); 
			}
		};
		
		sName.addKeyListener(adapter);
		sAge.addKeyListener(adapter);
		sJumin.addKeyListener(adapter);
		
		
		
		f.setVisible(true);
	}
	
	public void select() {
		Map<String, String> param = new HashMap<String, String>();
		param.put("NAME", sName.getText());
		param.put("AGE", sAge.getText());
		param.put("JUMIN", sJumin.getText());
		
		CustomerDAO dao = new CustomerDAO();
		Vector<Vector> data = dao.select(param);
		
		// table 객체에 DefaultTableModel 객체를 가져오기
		DefaultTableModel dtm = (DefaultTableModel)table.getModel();
		
		// Model 객체의 행번호를 0으로 초기화 (표시된 회원 목록 초기화)
		dtm.setRowCount(0);
		
		// Vector 객체(data) 만큼 반복을 통해 저장된 Vector 객체(rowData)를 가져온 후
		// DefaultTableModel 객체의 addRow() 메서드로 데이터 추가
		for(Vector rowData : data) {
			dtm.addRow(rowData);
		}
		
	
	}
	
	public boolean vailCheck() {
		
		if(tfName.getText().equals("")) {
			JOptionPane.showMessageDialog(
					f, "이름 입력 필수!", "입력 오류", JOptionPane.WARNING_MESSAGE);
			tfName.requestFocus(); // 입력창 커서 요청
			return false;
		} else if(tfAge.getText().equals("")) {
			JOptionPane.showMessageDialog(
					f, "나이 입력 필수!", "입력 오류", JOptionPane.WARNING_MESSAGE);
			tfAge.requestFocus(); // 입력창 커서 요청
			return false;
		} else if(tfEmail.getText().equals("")) {
			JOptionPane.showMessageDialog(
					f, "E-Mail 입력 필수!", "입력 오류", JOptionPane.WARNING_MESSAGE);
			tfEmail.requestFocus(); // 입력창 커서 요청
			return false;
		} else if(tfJumin.getText().equals("")) {
			JOptionPane.showMessageDialog(
					f, "주민번호 입력 필수!", "입력 오류", JOptionPane.WARNING_MESSAGE);
			tfJumin.requestFocus(); // 입력창 커서 요청
			return false;
		}
		
		return true;
	}
	
	public boolean vaildAge() {
		String pattern = "^[0-9]{1,3}$"; // 숫자 1~3자리 체크하는 패턴
		if(!Pattern.matches(pattern, tfAge.getText())) {
			JOptionPane.showMessageDialog(
					f, " 나이는 숫자 3자리 이하 입력 필수!", "입력 오류", JOptionPane.WARNING_MESSAGE);
			tfAge.requestFocus(); // 입력창 커서 요청
			return false;
		}
		
		return true;
	}
	
	public void insert() {
		// TODO 유효성검사
		if(!vailCheck()) return;	// 필수입력사항 체크 (null check)
		if(!vaildAge()) return;		// 나이(숫자 1 ~ 3 체크)
		// Email 형식체크
		// 주민번호 형식체크
		
		
		String name = tfName.getText();
		String age = tfAge.getText();
		String eMail = tfEmail.getText();
		String jumin = tfJumin.getText();
		
		// 객체에 입력 데이터 저장
		CustomerDTO dto = new CustomerDTO("", name, age, eMail, jumin);
		
		// CustomerDAO 객체 생성 후 insert() 메서드에 DTO 객체를 전달하여 회원 추가 작업 요청
		CustomerDAO dao = new CustomerDAO();
		boolean isSuccess = dao.insert(dto);
		
		if(isSuccess) {
			JOptionPane.showMessageDialog(
					f, "회원 추가 성공", "성공", JOptionPane.INFORMATION_MESSAGE);
			select();
		} else {
			JOptionPane.showMessageDialog(
					f, "회원 추가 실패", "실패", JOptionPane.ERROR_MESSAGE);
		}
		
	}
	
	public void delete() {
		// InputDialog를 사용하여 삭제할 회원번호(idx)를 입력받아
		// CustomerDAO의 delete() 메서드에 전달하여 회원 삭제 작업 요청
		// => 삭제 결과를 boolean 타입으로 리턴받아
		//    삭제 성공/실패를 MessageDialog로 출력
		String idx = JOptionPane.showInputDialog(f, "삭제할 회원 번호를 입력하세요.");
		
		// 취소, x => null
		if(idx == null) return;
		
		// 입력 번호가 없을 경우(널스트링 또는 길이 0) 오류 메세지 출력
		if(idx.length() == 0) {
			JOptionPane.showMessageDialog(
					f, "번호 입력 필수!", "입력 오류", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		// IDX 값이 1부터 증가되므로 0데이터도 오류 메세지 출력
		if(idx.equals("0")) {
			JOptionPane.showMessageDialog(
					f, "번호 1이상 입력!", "입력 오류", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		// 입력 데이터가 있을 경우 CustomerDAO 객체의 delete() 메서드에 번호 전달
		CustomerDAO dao = new CustomerDAO();
		boolean isSuccess = dao.delete(idx);
		
		if(isSuccess) {
			JOptionPane.showMessageDialog(
					f, "회원 삭제 성공", "성공", JOptionPane.INFORMATION_MESSAGE);
			select();
		} else {
			JOptionPane.showMessageDialog(
					f, "회원 삭제 실패", "실패", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	
	public static void main(String[] args) {
		new CustomerGUI();
	}
	
	
}
