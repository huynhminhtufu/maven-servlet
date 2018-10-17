package com.mrhmt.servlet;

import com.mrhmt.utils.XMLUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "UpdateStAXServlet", urlPatterns = {"/UpdateStAXServlet"})
public class UpdateStAXServlet extends HttpServlet {
    private final String xmlFile = "WEB-INF/studentAccounts.xml";

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
            String id = request.getParameter("txtId");
            String sClass = request.getParameter("txtClass");
            String address = request.getParameter("txtAddress");
            String lastSearchValue = request.getParameter("lastSearchValue");

            String realPath = this.getServletContext().getRealPath("/");
            XMLUtils.updateNodeInStAXUsingJAXB(id, sClass, address, xmlFile, realPath);

            String urlRewriting = "ProcessServlet?btAction=Search&txtStatus=" + lastSearchValue;
            response.sendRedirect(urlRewriting);
        } finally {
            out.close();
        }
    }
}
