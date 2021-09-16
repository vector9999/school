<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<c:choose>
	<c:when test="${not empty result.tempId}">
		<c:set var="actionUrl" value="/temptemp/update.do"/>
	</c:when>
	<c:otherwise>
		<c:set var="actionUrl" value="/temptemp/insert.do"/>
	</c:otherwise>
</c:choose>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Language" content="ko" >
<title>데이터 가져오기</title>
</head>
<body>
 * 등록폼
 	<form action="${actionUrl}" method="post" name="tempVO">
 		<%-- <input type="hidden" name="tempId" value="${result.tempId}"/> --%>
 		<label for="tempId">값 아이디 : </label>
 		<input type="text" id="tempId" name="tempId" value="${result.tempId}" />
 		<label for="tempVal">값 정보 : </label>
 		<input type="text" id="tempVal" name="tempVal" value="${result.tempVal}"/>
 		<button type="submit">등록</button>
 		<br/>
 	</form>
 
<!-- <form action="/temp/insert.do" method="post" name="frm">
	<label for="tempVal">값 경로 : </label>
	<input type="text" id="tempVal" name="tempVal"/>
	<br/>
	<button type="submit">등록</button>
</form> -->

</body>
</html>