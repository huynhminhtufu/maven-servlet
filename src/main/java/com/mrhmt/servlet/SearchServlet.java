package com.mrhmt.servlet;

import com.mrhmt.dtos.StudentDTO;
import com.mrhmt.utils.XMLUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "SearchServlet", urlPatterns = {"/SearchServlet"})
public class SearchServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String keyword = request.getParameter("txtKeyword");
        String searchPage = "search.jsp";
        String url = searchPage;

        try {
            ServletContext context = this.getServletContext();
            Document doc = (Document) context.getAttribute("DOMTREE");

            if (doc != null) {
                XPath xpath = XMLUtils.createXPath();

                String exp = "//student[contains(address,'" + keyword + "')]";

                NodeList students = (NodeList) xpath.evaluate(exp, doc, XPathConstants.NODESET);

                if (students != null) {
                    List<StudentDTO> result = new ArrayList<>();

                    for (int i = 0; i < students.getLength(); i++) {
                        Node student = students.item(i);

                        exp = "@id";
                        String id = (String) xpath.evaluate(exp, student, XPathConstants.STRING);

                        exp = "@class";
                        String sClass = (String) xpath.evaluate(exp, student, XPathConstants.STRING);

                        exp = "lastname";
                        String lastname = (String) xpath.evaluate(exp, student, XPathConstants.STRING);

                        exp = "middlename";
                        String middlename = (String) xpath.evaluate(exp, student, XPathConstants.STRING);

                        exp = "firstname";
                        String firstname = (String) xpath.evaluate(exp, student, XPathConstants.STRING);

                        exp = "sex";
                        String sex = (String) xpath.evaluate(exp, student, XPathConstants.STRING);

                        exp = "address";
                        String address = (String) xpath.evaluate(exp, student, XPathConstants.STRING);

                        exp = "status";
                        String status = (String) xpath.evaluate(exp, student, XPathConstants.STRING);

                        StudentDTO dto = new StudentDTO(id.trim(), sClass.trim(), firstname.trim(),
                                middlename.trim(), lastname.trim(),
                                sex.trim().equals("1"), address.trim(), status.trim());

                        result.add(dto);
                    }

                    if (result != null) {
                        request.setAttribute("SEARCHRESULT", result);
                    }
                }
            }
        } catch (XPathExpressionException ex) {
            Logger.getLogger(SearchServlet.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            RequestDispatcher rd = request.getRequestDispatcher(url);
            rd.forward(request, response);
            out.close();
        }
    }
}
