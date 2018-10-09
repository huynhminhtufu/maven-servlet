package com.mrhmt.servlet;

import org.w3c.dom.Document;
import com.mrhmt.utils.XMLUtils;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
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

@WebServlet(name = "LoginServlet", urlPatterns = {"/LoginServlet"})
public class LoginServlet extends HttpServlet {
    private final String errorPage = "invalid.html";
    private final String searchPage = "search.jsp";
    private final String xmlFile = "WEB-INF/studentAccounts.xml";
    private boolean bFound;
    private String fullName;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void processRequest (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String username = request.getParameter("txtUsername");
        String password = request.getParameter("txtPassword");

        String url = errorPage;

        try {
            String realPath = this.getServletContext().getRealPath("/");
            String xmlFilePath = realPath + xmlFile;
            Document doc = XMLUtils.parseFileToDOM(xmlFilePath);

            if (doc != null) {
                this.bFound = false;
                this.fullName = "";

                this.checkLogin(doc, username, password);

                if (bFound) {
                    url = searchPage;
                    HttpSession session = request.getSession();
                    session.setAttribute("ID", username);
                    session.setAttribute("FULLNAME", fullName);
                }
            }
        } catch (ParserConfigurationException | SAXException ex) {
            ex.printStackTrace();
        } finally {
            RequestDispatcher rd = request.getRequestDispatcher(url);
            rd.forward(request, response);
            out.close();
        }
    }

    private void checkLogin(Node node, String username, String password) {
        if (node == null || this.bFound) {
            return;
        }

        if (node.getNodeName().equals("student")) {
            NamedNodeMap attributes = node.getAttributes();

            String id = attributes.getNamedItem("id").getNodeValue();

            if (id.equals(username)) {
                NodeList childrenOfStudent = node.getChildNodes();

                for (int i = 0; i < childrenOfStudent.getLength(); i++) {
                    Node tmp = childrenOfStudent.item(i);

                    String nodeName = tmp.getNodeName();

                    if (nodeName.equals("lastname")) {
                        this.fullName += tmp.getTextContent().trim();
                    } else if (nodeName.equals("middlename")) {
                        this.fullName += tmp.getTextContent().trim();
                    } else if (nodeName.equals("firstname")) {
                        this.fullName += tmp.getTextContent().trim();
                    } else if (nodeName.equals("password")) {
                        String pass = tmp.getTextContent().trim();

                        if (!pass.equals(password)) break;
                    } else if (nodeName.equals("status")) {
                        String status = tmp.getTextContent().trim();

                        if (!status.equals("dropout")) {
                            this.bFound = true;

                            return;
                        }
                    }
                }
            }
        }

        NodeList children = node.getChildNodes();
        int i = 0;
        while (i < children.getLength()) {
            this.checkLogin(children.item(i), username, password);
            i++;
        }
    }
}
