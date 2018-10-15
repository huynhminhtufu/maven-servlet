<%--
  Created by IntelliJ IDEA.
  User: TUHUYNH
  Date: 10/15/2018
  Time: 7:26 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x"%>
<html>
<head>
    <title>e-Banking</title>
</head>
<body>
<div>Welcome <x:out select="$doc//*[@username=$username]/fullname"/></div>
<h2>Hell Bank</h2>
</body>
</html>
