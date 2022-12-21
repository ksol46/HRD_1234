package controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import DAO.MemberDAO;

@WebServlet("/")
public class MemberController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public MemberController() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		doPro(request,response);
	}

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		doPro(request,response);
	}

    
	protected void doPro(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//웹 프로젝트 하나당 생성됨 , 톰캣의 context path를 가져온다.
		//Servers.xml에서 확인 할 수 있음.
		String context = request.getContextPath();
		//주소중에 맨 끝 파일명만 가져온다. (경로의 맨 끝 파일명을 가져온다.)
		String command = request.getServletPath();
		String site = null;
		System.out.println(context + "," + command);
		
		MemberDAO member = new MemberDAO();
		
		switch (command) {
		case "/home" :
			site = "index.jsp";
			break;
		case "/insert" :
			site = member.insert(request, response);
			break;
		case "/list" :
			site = member.selectAll(request, response);
			break;
		case "/add" :
			site = member.nextCustno(request, response);
			break;
		case "/modify" :
			site = member.modify(request, response);
			break;
		case "/result":
			site = member.selectResult(request,response);
			break;
		case "/update" :
			int result1 = member.update(request, response);
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();
			
			if(result1 ==1) { //업데이트 성공 , 자바스크립트
				out.println("<script>"); ///location.href='/HRD_1234';
				out.println("alert('회원수정이 완료되었습니다!'); location.href='" + context +"';");
				out.println("</script>");
				out.flush();
			} else {
				out.println("<script>"); ///location.href='/HRD_1234';
				out.println("alert('땡!'); location.href='" + context +"';");
				out.println("</script>");
				out.flush();
			}
		case "/delete":
			int result2 = member.delete(request, response);
			response.setContentType("text/html; charset=UTF-8");
			out = response.getWriter();
			
			if(result2 == 1) {//업데이트 성공
				out.print("<script>");   				  //location.href= '/HRD_1234';
				out.print("alert('회원정보가 삭제되었습니다!'); location.href='" + context + "';");
				out.print("</script>");
				out.flush();
			} else {
				out.print("<script>");   				  //location.href= '/HRD_1234';
				out.print("alert('삭제실패!!'); location.href='" + context + "';");
				out.print("</script>");
				out.flush();
			}
			break;
		}
		
		getServletContext().getRequestDispatcher("/" + site).forward(request, response);
	}
}
		