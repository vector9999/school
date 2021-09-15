<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
 <%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link href="/css/common.css" rel="stylesheet" type="text/css" >
<script  src="http://code.jquery.com/jquery-latest.min.js"></script>
</head>
<body>
<div>
	게시물 총 수 : <c:out value="${paginationInfo.totalRecordCount}"/>
    <table>
        <caption>이 표는 TEMP_ID, TEMP_VAL로 구성되어 있는 표</caption>
        <thead>
            <tr>
                <th>TEMP_ID</th>
                <th>TEMP_VAL</th>
            </tr>
	    </thead>
	    <tbody>
	    	<c:forEach var="result" items="${resultList}">
		    	<tr>
		    		<td><c:out value="${result.tempId}"/></td>
		    		<td>
		    			<c:url var="viewUrl" value="/temp/select.do">
		    				<c:param name="tempId" value="${result.tempId}"></c:param>
		    			</c:url>
			    		<a href="${viewUrl}">
			    			<c:out value="${result.tempVal}"/>
			    		</a>
		    		</td>
		    		<td>
		    			<c:url var="delUrl" value="/temp/delete.do">
		    				<c:param name="tempId" value="${result.tempId}"></c:param>
		    			</c:url>
		    			<a href="${delUrl}" class="btn-del">삭제</a>
		    		</td>
		    	</tr>
	    	</c:forEach>
	    </tbody>
    </table>
    <div id="paging_div">
    	<ul class="paging_align">
    		<c:url var="pageUrl" value="/temp/selectList.do"/>
    		<c:set var="pagingParam"><c:out value="${pageUrl}"/></c:set>
    		<ui:pagination paginationInfo="${paginationInfo}" type="image" jsFunction="${pagingParam}"/> <!-- 스프링실습에서는 일일히 만들어다씀, 여기선 함수 가져다씀. 어떠한 페이지에서도 동일하게 사용가능 -->
    	</ul>
    </div>
    </div>
    <button type="button" id="btn-reg" data-href="/temp/tempRegist.do"> <!-- 데이터로 가져오기 위한 data-href. 예전에는 버튼에 데이터 못넣고 웹접근성을 위해 a태그 별도로 썻어야했다 -->
    	등록하기
    </button>
    
    <script>
    	$(document).ready(function() {
    		$("#btn-reg").click(function(){ /* 스크립트에서 여러개 스캔하는 class보다 id를 제일 먼저 찾음 등록버튼은 하나뿐이니까 id로 선언한 것 임  */
    			location.href = $(this).data("href");
    		});
    		
    		$(".btn-del").click(function(){
    			if(!confirm("삭제하시겠습니까?")) {
    				return false;
    			}
    		});
    	});
    </script>
</body>
</html>