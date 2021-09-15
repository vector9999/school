<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>목록 가져오기 과제</title>
</head>
<body>
<table>
	<thead>
		<tr>
			<th>TEMP_VAL</th>
			<th>TEMP_ID</th>
		</tr>
	</thead>
	<tbody>
	<c:forEach var="list" items="${list}">
			<tr>
				<td>${list.tempVal}</td>
				<td>${list.tempId}</td>
			</tr>
	</c:forEach>
	</tbody>
</table>
</body>
</html>