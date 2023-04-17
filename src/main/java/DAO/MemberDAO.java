package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import DTO.Member;
import DTO.Money;

public class MemberDAO {
	Connection conn = null;
	PreparedStatement ps = null;
	ResultSet rs = null;
	
	//데이터 베이스 연결 메소드
	public static Connection getConnection () throws Exception {
		Class.forName("oracle.jdbc.OracleDriver");
		Connection con = DriverManager
				.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "ksol46", "0406");
		return con;
	}
	
	
	//회원 등록
	public String insert (HttpServletRequest request,HttpServletResponse response) {
		int custno = Integer.parseInt(request.getParameter("custno"));
		String custname = request.getParameter("custname");
		String phone = request.getParameter("phone");
		String address = request.getParameter("address");
		String joindate = request.getParameter("joindate");
		String grade = request.getParameter("grade");
		String city = request.getParameter("city");
		int result = 0;
		
		try {
			conn = getConnection();//db연결
			String sql = "insert into member_tbl_02 values(?,?,?,?,to_date(?,'YYYY-MM=DD'),?,?)";
			ps = conn.prepareStatement(sql); //쿼리문 실행시키기
			ps.setInt(1, custno);
			ps.setString(2, custname);
			ps.setString(3, phone);
			ps.setString(4, address);
			ps.setString(5, joindate);
			ps.setString(6, grade);
			ps.setString(7, city);
			
			//insert, update, delete : 성공한 레코드의 갯수를 반환
			result = ps.executeUpdate();
			
			System.out.println(result);
			
			conn.close();
			ps.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		return "add";
	}
	
	//회원번호 자동증가
	public String nextCustno(HttpServletRequest request,HttpServletResponse response) {
		try {
			conn = getConnection();
			String sql = "select max(custno)+1 custno from member_tbl_02";
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			
			int custno = 0;
			
			if(rs.next()) custno = rs.getInt(1);
			request.setAttribute("custno", custno);
			
			conn.close();
			ps.close();
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "add.jsp";
		
	}
	
	//회원목록조회 / 수정
	public String selectAll(HttpServletRequest request,HttpServletResponse response) {
		ArrayList<Member> list = new ArrayList<Member>();
		try {
			conn = getConnection();//db연결
			String sql = "select custno, custname, phone, address, TO_CHAR(joindate, 'YYYY-MM-DD') joindate,";
			//가능하면 쿼리문으로 처리 해야한다. (등급별로 나눠주는 작업)
			sql += "DECODE(grade, 'A', 'VIP', 'B', '일반', '직원') grade, city from member_tbl_02 order by custno"; 
		
			ps = conn.prepareStatement(sql); //쿼리문 실행 준비
			rs = ps.executeQuery(); //쿼리문 실행
			
			while (rs.next()) {
				Member member = new Member();
				member.setCustno(rs.getInt(1));
				member.setCustname(rs.getString(2));
				member.setPhone(rs.getString(3));
				member.setAddress(rs.getString(4));
				member.setJoindate(rs.getString(5));
				member.setGrade(rs.getString(6));
				member.setCity(rs.getString(7));
				
				list.add(member);
				
			}
			
			request.setAttribute("list", list);
			
			conn.close();
			ps.close();
			rs.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		return "list.jsp";
	}
	
	public String selectResult(HttpServletRequest request, HttpServletResponse response) {
		ArrayList<Money> list = new ArrayList<Money>(); //Money 엔티티클래스(DTO)를 담는 역할.
		try {
			conn = getConnection();
			String sql = "select m1.custno, m1.custname, DECODE(grade, 'A', 'VIP', 'B', '일반', '직원') grade, sum(m2.price) price"
					+ " from member_tbl_02 m1, money_tbl_02 m2"
					+ " where m1.custno = m2.custno"
					+ " group by (m1.custno, m1.custname, grade)"
					+ " order by price desc";
			
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			
			while(rs.next()) {
				Money money = new Money();
				money.setCustno(rs.getInt(1));
				money.setCustname(rs.getString(2));
				money.setGrade(rs.getString(3));
				money.setPrice(rs.getInt(4));
				
				list.add(money);
			}
			
			request.setAttribute("list", list);
			
			conn.close();
			ps.close();
			rs.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
			return "result.jsp";
	}
	
	
	//회원정보수정(데이터를 먼저 가져온다.)
	public String modify(HttpServletRequest request,HttpServletResponse response) {
		try {
			conn = getConnection();
			int custno = Integer.parseInt(request.getParameter("custno"));
			
			String sql = "select custname, phone, address, TO_CHAR(joindate, 'YYYY-MM-DD') joindate, grade,city ";
			sql += "from member_tbl_02 where custno=" + custno;
			
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			
			Member member = new Member();
			
			if(rs.next()) {
				member.setCustno(custno);
				member.setCustname(rs.getString(1));
				member.setPhone(rs.getString(2));
				member.setAddress(rs.getString(3));
				member.setJoindate(rs.getString(4));
				member.setGrade(rs.getString(5));
				member.setCity(rs.getString(6));
				
				request.setAttribute("member", member);
				request.setAttribute("custno", custno);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "modify.jsp";
	}
	
	//회원 정보 업데이트
	public int update(HttpServletRequest request,HttpServletResponse response) {
		int custno = Integer.parseInt(request.getParameter("custno"));
		String custname = request.getParameter("custname");
		String phone = request.getParameter("phone");
		String address = request.getParameter("address");
		String joindate = request.getParameter("joindate");
		String grade = request.getParameter("grade");
		String city = request.getParameter("city");
		int result = 0;
		
		try {
			conn = getConnection();
			String sql = "update member_tbl_02 set";
			   sql +=" custname = ?, ";
			   sql +=" phone = ?, ";
			   sql +=" address = ?, ";
			   sql +=" joindate = to_date(?, 'yyyy-mm-dd'),";
			   sql +=" grade = ?, ";
			   sql +=" city = ? ";
			   sql +=" where custno = ?";
			   
			   ps = conn.prepareStatement(sql); //쿼리문 실행시키기
				ps.setString(1, custname);
				ps.setString(2, phone);
				ps.setString(3, address);
				ps.setString(4, joindate);
				ps.setString(5, grade);
				ps.setString(6, city);
				ps.setInt(7, custno);
				
				//insert, update, delete : 성공한 레코드의 갯수를 반환
				result = ps.executeUpdate();
				
				conn.close();
				ps.close();
				
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	//회원정보삭제
	public int delete(HttpServletRequest request, HttpServletResponse response) {
		int result = 0;
		try {
			conn = getConnection();
			String custno = request.getParameter("custno"); //회원번호를 기반으로 삭제할 녀석을 고르기 때문에 받아옴.
			String sql = "delete from member_tbl_02 where custno=" + custno;
			
			ps = conn.prepareStatement(sql);//위에서 적은 쿼리문을 입력(준비).
			result = ps.executeUpdate();//쿼리문 실행
			
			conn.close();
			ps.close();
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		return result;
		
	}
	
	
}







