<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"  %>


<!-- 헤더값 -->
<%
	response.setHeader("Content-Disposition", "attachment; filename=apply.xls");
	response.setHeader("Content-Discription", "JSP Generated Data");
%>
	
<!DOCTYPE html>
<html>
<head>
	<title>목록</title>
	<!-- 공통사용 meta tag -->
	<meta http-equiv="Content-Language" content="ko">
	<meta charset="UTF-8">
	<!-- 공통사용 meta tag 끝 -->

<style type="text/css">

</style>
</head>
<body>
	<table>
		<thead>
			<tr>
				<th>번호</th>
				<th>신청자명</th>
				<th>연락처</th>
				<th>이메일</th>
				<th>신청일</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="result" items="${resultList}" varStatus="status">
				<tr>
					<td class="num"><c:out value="${fn:length(resultList) - (status.index)}"/></td>
					<td><c:out value="${result.chargerNm}"/>(<c:out value="${result.frstRegisterId}"/>)</td>
					<td><c:out value="${result.telno}"/></td>
					<td><c:out value="${result.email}"/></td>
					<td>
						<fmt:formatDate value="${result.frstRegistPnttm}" pattern="yyyy-MM-dd"/>
					</td>
				</tr>
			</c:forEach>
			<c:if test="${fn:length(resultList) == 0}">
				<tr>
					<td colspan="5">신청자가 없습니다.</td>
				</tr>
			</c:if>
		</tbody>
	</table>
</body>
</html>