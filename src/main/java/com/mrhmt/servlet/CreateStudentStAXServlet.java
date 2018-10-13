package com.mrhmt.servlet;

import com.mrhmt.utils.XMLUtils;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "CreateStudentStAXServlet", urlPatterns = {"/CreateStudentStAXServlet"})
public class CreateStudentStAXServlet extends HttpServlet {
    private final String xmlFileName = "WEB-INF/studentAccounts.xml";
    private final String insertErrorPage = "insertError.html";
    private final String loginPage = "login.html";
    private final String inserSuccess ="insertSuccess.html";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String url = insertErrorPage;

        try {
            String id = request.getParameter("txtId");
            String sClass = request.getParameter("txtClass");
            String lastname = request.getParameter("txtLast");
            String middlename = request.getParameter("txtMiddle");
            String firstname = request.getParameter("txtFirst");
            String sex = request.getParameter("txtSex");
            int nSex = Integer.parseInt(sex);
            String password = request.getParameter("txtPassword");
            String address = request.getParameter("txtAddress");
            String status = request.getParameter("txtStatus");

            String realPath = this.getServletContext().getRealPath("/");
            boolean result = XMLUtils.insertNodeInStAXUsingJAXB(id, sClass, lastname, middlename, firstname, address,
                    password, status, nSex, xmlFileName, realPath);

            if (result) {
                url = inserSuccess;
            }

            RequestDispatcher rd = request.getRequestDispatcher(url);
            rd.forward(request, response);
        } finally {
            out.close();
        }
    }
}
