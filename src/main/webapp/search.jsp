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
<form action="SearchServlet">
    Address: <input name="txtKeyword" value="${param.txtKeyword}"/>
</form>

<c:set var="keyword" value="${param.txtKeyword}"/>
<c:if test="${not empty keyword}">
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
                <tr>
                    <td><input type="checkbox" name="remove[]" value="${dto.id}" /></td>
                    <td>${counter.count}</td>
                    <td>${dto.id}</td>
                    <td>${dto.sClass}</td>
                    <td>${dto.last} ${dto.middle} ${dto.first}</td>
                    <td>${dto.address}</td>
                    <td>
                        <c:if test="${dto.sex}">
                            Male
                        </c:if>
                        <c:if test="${not dto.sex}">
                            Female
                        </c:if>
                    </td>
                    <td>${dto.status}</td>
                    <td>
                        <c:url var="removeStudent" value="RemoveServlet">
                            <c:param name="id" value="${dto.id}"/>
                        </c:url>
                        <a href="${removeStudent}" title="Remove">Remove</a>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
        <button type="submit">Delete Students</button>
    </c:if>
    <c:if test="${empty result}">
        <p>No student found!</p>
    </c:if>
</c:if>
</body>
</html>
