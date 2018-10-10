package com.mrhmt.servlet;

import com.mrhmt.sax.StudentHandler;
import com.mrhmt.utils.XMLUtils;
import org.xml.sax.SAXException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "LoginSAXServlet", urlPatterns = {"/LoginSAXServlet"})
public class LoginSAXServlet extends HttpServlet {
    private final String errorPage = "invalid.html";
    private final String searchPage = "search.jsp";
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

        String username = request.getParameter("txtUsername");
        String password = request.getParameter("txtPassword");

        String url = errorPage;

        String realPath = this.getServletContext().getRealPath("/");
        String xmlFilePath = realPath + xmlFile;
        StudentHandler handler = new StudentHandler(username, password);

        try {
            XMLUtils.parseFileToSAX(xmlFilePath, handler);

            boolean result = handler.isFound();
            if (result) {
                url = searchPage;
                HttpSession session = request.getSession();
                session.setAttribute("USER", username);
                session.setAttribute("FULLNAME", handler.getFullName());
            }
        } catch (ParserConfigurationException ex) {
            ex.printStackTrace();
        } catch (SAXException ex) {
            ex.printStackTrace();
        } finally {
            RequestDispatcher rd = request.getRequestDispatcher(url);
            rd.forward(request, response);
            out.close();
        }
    }
}
