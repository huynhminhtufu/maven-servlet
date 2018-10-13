package com.mrhmt.servlet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "ProcessServlet", urlPatterns = {"/ProcessServlet"})
public class ProcessServlet extends HttpServlet {
    private final String loginPage = "login.html";
    private final String loginServlet = "LoginStAXServlet";
    private final String searchServlet = "SearchStAXServlet";
    private final String deleteServlet = "DeleteStAXServlet";
    private final String updateServlet = "UpdateStAXServlet";
    private final String createStudentServlet = "CreateStudentStAXServlet";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        try {
            String button = request.getParameter("btAction");
            String url = loginPage;

            if (button != null) {
                if (button.equals("Login")) {
                    url = loginServlet;
                } else if (button.equals("Search")) {
                    url = searchServlet;
                } else if (button.equals("Delete")) {
                    url = deleteServlet;
                } else if (button.equals("Update")) {
                    url = updateServlet;
                } else if (button.equals("Create New Student")) {
                    url = createStudentServlet;
                }
            }

            RequestDispatcher rd = request.getRequestDispatcher(url);
            rd.forward(request, response);
        } finally {
            out.close();
        }
    }
}
