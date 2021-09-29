<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"  %>
 
<!-- 기본 URL - 검색 조건 같은 값들이 계속 따라다니게 하기 위해서 위에서 먼저 선언. 다른데다가 중복해서 코딩을 할 필요없이. 하나의 변수로 만들어줌 -->
	<c:url var="_BASE_PARAM" value=""> <!-- 중복으로 가장 많이 쓰이기 때문에 - view나 paging쪽에서 사용 -->
		<c:param name="menuNO" value="50"/><!-- 임의로 넣은 값  -->
		<c:param name="pageIndex" value="${searchVO.pageIndex}"/>
		<c:if test="${not empty searchVO.searchCondition}"> <!-- searchCondition, searchKeyword ==> 검색 값. 검색 후 상세페이지로 갔다가 다시 목록으로 갈때 검색한 결과값 유지하기 위해서, 파라미터가 쫓아다님 -->
			<c:param name="searchCondition" value="${searchVO.searchCondition}"/>
		</c:if>
		<c:if test="${not empty searchVO.searchKeyword}">
			<c:param name="searchKeyword" value="${searchVO.searchKeyword}"/>
		</c:if>
	</c:url>
	
<!DOCTYPE html>
<html>
<head>
<!-- 공통사용 meta tag -->
<meta http-equiv="Content-Language" content="ko">
<meta http-equiv="X-UA-Compatible" content="IE=edge"> <!-- 구버전 익플을 강제로 익플로 변경 시켜줌-->
<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, max-scale=1.0, user-scalable=no"> <!-- 반응형 : 현재 접속한 디바이스 크기를 최대크기를 디바이스 크기, initial-scale: 1배. 즉 확대 안함/유저 강제확대 불가 등 -->
<!-- 공통사용 meta tag 끝 -->

<title>수업용 게시판</title>
<!-- BBS Style -->
<link href="/asset/BBSTMP_0000000000001/style.css" rel="stylesheet"/>
<!-- 공통 Style -->
<link href="/asset/LYTTMP_0000000000000/style.css" rel="stylesheet"/>
<script  src="http://code.jquery.com/jquery-latest.min.js"></script>
</head>
<body>
	<div class="container">
		<div id="contents">
			<div id="bbs_wrap">
				<div class="board_view">
					<dl class="tit_view">
						<dt>제목</dt>
						<dd><c:out value="${result.boardSj}"/> </dd>
					</dl>
					<dl class="info_view2">
						<dt>작성자ID</dt>
						<dd><c:out value="${result.frstRegisterId}"/></dd>
						<dt>작성일</dt>
						<dd><fmt:formatDate value="${result.frstRegistPnttm}" pattern="yyyy-MM-dd"/></dd>
						<dt>조회수</dt>
						<dd><c:out value="${result.inqireCo}"/></dd>
					</dl>
					<div class="view_count">
						<c:out value="${result.boardCn}" escapeXml="false"/> <!-- html을 view해주기 위해서, 내용부분을 editor를 많이 쓴는데 그 editor자체가 html로 이루어져있음 
																			editor의 enter는 <br>, <p>태그로 묶이는등, 이 두 태그에는 최대한 설정이(컬러 등) 안들어가게 해야함, 
																			c:out에 이런속성이 존재한다는걸 알아둬야함.
																			editor가 들어가는 부분은 escapeXml 설정이 들어간다는 정도만 알아둬야함 -->
					</div>
				</div>
				<div class="btn-cont ar"> <!-- pk값이 있으면 업데이트와 삭제, 아닐 경우에는 등록만 보여지게 함.  -->
					<c:choose>
						<c:when test="${not empty searchVO.boardId}">
							<c:url var="uptUrl" value="/board/boardRegist.do${_BASE_PARAM}">
								<c:param name="boardId" value="${result.boardId}"/>
							</c:url>
							<a href="${uptUrl}" class="btn">수정</a>
							
							<c:url var="delUrl" value="/board/delete.do${_BASE_PARAM}">
								<c:param name="boardId" value="${result.boardId}"/>
							</c:url>
							<a href="${delUrl}" id="btn-del" class="btn"><i class="ico-del"></i>삭제</a>
						</c:when>
						<c:otherwise>
							<a href="#none" id="btn-reg" class="btn-spot">등록</a>
						</c:otherwise>
					</c:choose>
					<c:url var="listUrl" value="/board/selectList.do${_BASE_PARAM}"/> <!-- 목록은 등록과 수정 모두에 포함되니 나오게 한다. -->
					<a href="${listUrl}" class="btn">목록</a>
				</div>
				
			</div>
		</div>
	</div>
	<script>
	$(document).ready(function(){
		//게시글 삭제
		$("#btn-del").click(function() {
			if(!confirm("삭제하시겠습니까?")) {
				return false;
			}
		});
	});
	</script>
</body>
</html>