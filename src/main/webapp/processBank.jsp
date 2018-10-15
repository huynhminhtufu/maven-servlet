<%--
  Created by IntelliJ IDEA.
  User: TUHUYNH
  Date: 10/15/2018
  Time: 7:10 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x"%>
<html>
<head>
    <title>Process</title>
</head>
<body>
<h2>Processing...</h2>

<c:set var="username" value="${param.txtUsername}" scope="session" />
<c:set var="password" value="${param.txtPin}" />

<c:if test="${not empty username and not empty password}">
    <c:import var="xmlDoc" url="WEB-INF/atmAccount.xml"/>
    <x:parse var="doc" doc="${xmlDoc}" scope="session"/>
    <c:out value="${username}" />
    <c:out value="${password}" />
    <x:if select="$doc//*[@username=$username and @pin=$password]">
        <c:out value="Ahihi"/>
        <jsp:forward page="transactionView.jsp"/>
    </x:if>
</c:if>
<c:if test="${empty username or empty password}">
    <h2>Invalid!</h2>
</c:if>
</body>
</html>
