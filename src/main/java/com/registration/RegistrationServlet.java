package com.registration;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class RegistrationServlet
 */
@WebServlet("/register")
public class RegistrationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		//get the values from the request
		String uname = request.getParameter("name");
		String uemail = request.getParameter("email");
		String upw = request.getParameter("pass");
		String Reupw = request.getParameter("re_pass");
		String umobile = request.getParameter("contact");
		RequestDispatcher dispatcher =null;
		Connection con = null;
		
		
		//Server side validation
		if(uname == null || uname.equals("")) {
			request.setAttribute("status", "invalidName");
			dispatcher = request.getRequestDispatcher("registration.jsp");
			dispatcher.forward(request, response);
		}
		
		if(uemail == null || uemail.equals("")) {
			request.setAttribute("status", "invalidEmail");
			dispatcher = request.getRequestDispatcher("registration.jsp");
			dispatcher.forward(request, response);
		}
		
		if(upw == null || upw.equals("")) {
			request.setAttribute("status", "invalidPassword");
			dispatcher = request.getRequestDispatcher("registration.jsp");
			dispatcher.forward(request, response);
		}else if (!upw.equals(Reupw)) {
			request.setAttribute("status", "invalidConfirmPassword");
			dispatcher = request.getRequestDispatcher("registration.jsp");
			dispatcher.forward(request, response);
		}
		
		if(umobile == null || umobile.equals("")) {
			request.setAttribute("status", "invalidMobileNumber");
			dispatcher = request.getRequestDispatcher("registration.jsp");
			dispatcher.forward(request, response);
		}else if(umobile.length() < 12) {
			request.setAttribute("status", "invalidMobileLength");
			dispatcher = request.getRequestDispatcher("registration.jsp");
			dispatcher.forward(request, response);
		}
		

		try {
			//load JDBC Driver
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/user?useSSL=false", "root", "12345");
			PreparedStatement pst = con
					.prepareStatement("insert into users(uname,upw,uemail,umobile) values(?,?,?,?) ");
			
			//set the values for SQL query
			pst.setString(1, uname);
			pst.setString(2, upw);
			pst.setString(3, uemail);
			pst.setString(4, umobile);
			
			int rowcount = pst.executeUpdate();
			
			 // Set up the request dispatcher to forward the request
			dispatcher = request.getRequestDispatcher("registration.jsp");
			if (rowcount > 0) {
				// If insertion was successful, set status attribute to success
				request.setAttribute("status", "success");
			}else {
				request.setAttribute("status", "failed");
			}
			// Forward the request to registration.jsp
			dispatcher.forward(request, response);
			
		} catch (Exception e) {
		e.printStackTrace();
		}finally {
			try {
				// Close the database connection if it was opened
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

}
