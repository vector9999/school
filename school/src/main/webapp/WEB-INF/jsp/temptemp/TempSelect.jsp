<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html>
<head>
<script  src="http://code.jquery.com/jquery-latest.min.js"></script>
<meta http-equiv="Content-Language" content="ko" >
<title>데이터 가져오기</title>
</head>
<body>
<%-- 결과 값 : ${result.tempVal}== --%>
${result.tempVal}
<div class="box-btn">
	<c:url var="uptUrl" value="/temptemp/tempRegist.do">
		<c:param name="tempId" value="${result.tempId}"/>
	</c:url>
	<a href="${uptUrl}">수정</a>
	<c:url var="delUrl" value="/temptemp/delete.do">
		<c:param name="tempId" value="${result.tempId}"/>
	</c:url>
	<a href="${delUrl} class="btn-del"">삭제</a>
	<a href="/temptemp/selectList.do">목록</a>
</div>
<script>
	$(document).ready(function(){
		$(".btn-del").click(function() {
			if(!confirm("삭제 하시겠습니까?")) {
				return false;
			}
		});
	});
</script>
</body>
</html>