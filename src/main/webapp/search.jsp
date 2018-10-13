<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Search</title>
</head>
<body>
<p>Hello, ${sessionScope.FULLNAME}</p>
<h1>Search</h1>
<form action="ProcessServlet">
    Status: <input name="txtStatus" value="${param.txtStatus}"/>
    <br/>
    <input type="submit" value="Search" name="btAction" />
</form>

<c:set var="status" value="${param.txtStatus}"/>
<c:if test="${not empty status}">
    <c:set var="result" value="${requestScope.SEARCHRESULT}"/>

    <c:if test="${not empty result}">
        <h2>Result:</h2>
        <table>
            <thead>
            <tr>
                <th></th>
                <th>#</th>
                <th>ID</th>
                <th>Class</th>
                <th>Name</th>
                <th>Address</th>
                <th>Sex</th>
                <th>Status</th>
                <th>Action</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${result}" var="dto" varStatus="counter">
                <form action="ProcessServlet">
                <tr>
                    <td>
                        <input type="hidden" name="remove[]" value="${dto.id}" />
                    </td>
                    <td>${counter.count}</td>
                    <td>
                        ${dto.id}
                        <input type="hidden" name="txtId" value="${dto.id}" />
                    </td>
                    <td><input type="text" name="txtClass" value="${dto.sClass}"/></td>
                    <td>${dto.last} ${dto.middle} ${dto.first}</td>
                    <td>
                        <input type="text" name="txtAddress" value="${dto.address}" />
                    </td>
                    <td>
                       ${dto.sex}
                    </td>
                    <td>${dto.status}</td>
                    <td>
                        <c:url var="delLink" value="ProcessServlet">
                            <c:param name="btAction" value="Delete" />
                            <c:param name="id" value="${dto.id}"/>
                            <c:param name="lastSearchValue" value="${param.txtStatus}"/>
                        </c:url>
                        <a href="${delLink}" title="Remove">Delete</a>
                    </td>
                    <td>
                        <input type="submit" value="Update" name="btAction" />
                        <input type="hidden" value="${param.txtStatus}" name="lastSearchValue"/>
                    </td>
                </tr>
                </form>
            </c:forEach>
            </tbody>
        </table>
    </c:if>
    <c:if test="${empty result}">
        <p>No student found!</p>
    </c:if>
</c:if>
</body>
</html>
