package com.mrhmt.servlet;

import com.mrhmt.utils.XMLUtils;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "LoginStAXServlet", urlPatterns = {"/LoginStAXServlet"})
public class LoginStAXServlet extends HttpServlet {
    private final String invalidPage = "invalid.html";
    private final String xmlFile = "WEB-INF/studentAccounts.xml";
    private final String searchPage = "search.jsp";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        InputStream is = null;
        XMLStreamReader reader = null;
        String url = invalidPage;

        try {
            String username = request.getParameter("txtUsername");
            String password = request.getParameter("txtPassword");

            String realPath = this.getServletContext().getRealPath("/");
            String filePath = realPath + xmlFile;

            is = new FileInputStream(filePath);

            reader = XMLUtils.parseFileToStAXCursor(is);
            String fullName;

            while (reader.hasNext()) {
                int cursor = reader.next();

                if (cursor == XMLStreamConstants.START_ELEMENT) {
                    String tagName = reader.getLocalName();

                    if (tagName.equals("student")) {
                        String id = XMLUtils.getNodeStAXValue(reader, "student", "", "id");
                        if (id.equals(username)) {
                            String lastname = XMLUtils.getTextStAXContext(reader, "lastname");
                            fullName = lastname.trim();
                            String middlename = XMLUtils.getTextStAXContext(reader, "middlename");
                            fullName = fullName + " " + middlename.trim();
                            String firstname = XMLUtils.getTextStAXContext(reader, "firstname");
                            fullName = fullName + " " + firstname;

                            String pass = XMLUtils.getTextStAXContext(reader, "password");

                            if (pass.trim().equals(password)) {
                                String status = XMLUtils.getTextStAXContext(reader, "status");

                                if (!status.trim().equals("dropout")) {
                                    url = searchPage;
                                    HttpSession session = request.getSession();
                                    session.setAttribute("FULLNAME", fullName);
                                    session.setAttribute("USER", username);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        } catch (XMLStreamException ex) {
            ex.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (XMLStreamException ex) {
                    Logger.getLogger(LoginStAXServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            if (is != null) {
                is.close();
            }

            RequestDispatcher rd = request.getRequestDispatcher(url);
            rd.forward(request, response);
            out.close();
        }
    }
}
