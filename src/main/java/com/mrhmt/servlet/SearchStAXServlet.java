package com.mrhmt.servlet;

import com.mrhmt.dto.StudentDTO;
import com.mrhmt.utils.XMLUtils;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "SearchStAXServlet", urlPatterns = {"/SearchStAXServlet"})
public class SearchStAXServlet extends HttpServlet {
    private final String xmlFile = "WEB-INF/studentAccounts.xml";
    private final String showResultPage = "search.jsp";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        InputStream is;
        XMLEventReader reader;
        List<StudentDTO> students = null;

        try {
            String searchValue = request.getParameter("txtStatus");

            String realPath = this.getServletContext().getRealPath("/");
            String filePath = realPath + xmlFile;

            is = new FileInputStream(filePath);

            reader = XMLUtils.parseFileToStAXIterator(is);

            while (reader.hasNext()) {
                reader.nextEvent();
                String id = XMLUtils.getNodeStAXValue(reader, "student", "", "id");

                if (id != null) {
                    String sClass = XMLUtils.getNodeStAXValue(reader, "student", "", "class");
                    String lastname = XMLUtils.getTextStAXContext(reader, "lastname");
                    String middlename = XMLUtils.getTextStAXContext(reader, "middlename");
                    String firstname = XMLUtils.getTextStAXContext(reader, "firstname");
                    String sex = XMLUtils.getTextStAXContext(reader, "sex");
                    String password = XMLUtils.getTextStAXContext(reader, "password");
                    String address = XMLUtils.getTextStAXContext(reader, "address");
                    String status = XMLUtils.getTextStAXContext(reader, "status");

                    if (status.trim().equals(searchValue)) {
                        StudentDTO dto = new StudentDTO(id, sClass, firstname.trim(), middlename.trim(),
                                lastname.trim(), Integer.parseInt(sex.trim()), password.trim(), address.trim(), status.trim());

                        if (students == null) {
                            students = new ArrayList<StudentDTO>();
                        }

                        students.add(dto);
                    }
                }
            }

            request.setAttribute("SEARCHRESULT", students);

            RequestDispatcher rd = request.getRequestDispatcher(showResultPage);
            rd.forward(request, response);
        } catch (XMLStreamException ex) {
            Logger.getLogger(SearchStAXServlet.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            out.close();
        }
    }
}
