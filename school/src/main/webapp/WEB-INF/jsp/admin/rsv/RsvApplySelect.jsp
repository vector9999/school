<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"  %>
 
<!-- 기본 URL - 검색 조건 같은 값들이 계속 따라다니게 하기 위해서 위에서 먼저 선언. 다른데다가 중복해서 코딩을 할 필요없이. 하나의 변수로 만들어줌 -->
	<c:url var="_BASE_PARAM" value=""> <!-- 중복으로 가장 많이 쓰이기 때문에 - view나 paging쪽에서 사용 -->
		<c:param name="menuNO" value="50"/><!-- 임의로 넣은 값  -->
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

<title>수업용 예약관리</title>
<link href="/css/common.css" rel="stylesheet" type="text/css"> <!-- 공통 css는 항상 최상단에 두어야 함. common.css를 가져옴 -->
<!-- BBS Style -->
<link href="/asset/BBSTMP_0000000000001/style.css" rel="stylesheet"/>
<!-- 공통 Style -->
<link href="/asset/LYTTMP_0000000000000/style.css" rel="stylesheet"/>
<script  src="http://code.jquery.com/jquery-latest.min.js"></script>
</head>
<body>
<!-- 전체 레이어 시작 -->
<div id="wrap">
	<!-- header 시작 -->
	<div id="header_mainsize">
		<c:import url="/EgovPageLink.do?link=main/inc/EgovIncHeader"/>
	</div>
	<div id="topnavi">
		<c:import url="/EgovPageLink.do?link=main/inc/EgovIncTopnav"/>
	</div>
	<!-- //header 끝 -->

	<!-- container 시작 -->
	<div id="container">
		<!-- 좌측메뉴 시작 -->
		<div id="leftmenu">
			<c:import url="/EgovPageLink.do?link=main/inc/EgovIncLeftmenu"/>
		</div>
		<!-- //좌측메뉴 끝 -->
		
		<!-- 현재 위치 네비게이션 시작  -->
		<div id="content">
			<div class="container">
				<div id="contents">
						<form action="/admin/rsv/rsvApplyConfirm.do" method="post" id="frm" name="frm" onsubmit="return regist()">
							<input type="hidden" name="resveId" value="${result.resveId}">
							<input type="hidden" name="reqstId" value="${result.reqstId}">
						
						<table class="chart2">
							<caption>예약정보 작성</caption>
							<colgroup>
								<col style="width:120px"/>
								<col/>
							</colgroup>
							<tbody>
								<tr>
									<th scope="row">신청자명</th>
									<td>
										<c:out value="${result.chargerNm}"/>
										()<c:out value="${result.frstRegisterId}"/>)
									</td>
								</tr>
								<tr>
									<th scope="row">연락처</th>
									<td>
										<c:out value="${result.telno}"/>
									</td>
								</tr>
								<tr>
									<th scope="row">이메일</th>
									<td>
										 <c:choose>
										 	<c:when test="${empty result.email}">없음</c:when>
										 	<c:otherwise>
												<c:out value="${result.email}"/>
										 	</c:otherwise>
										 </c:choose>
									</td>
								</tr>
								<tr>
									<th scope="row">승인여부</th>
									<td>
										<select id="confmSeCode" name="confmSeCode">
											<option value="R">접수</option>
											<option value="O" <c:if test="${result.confmSeCode eq 'O'}">selected="selected"</c:if>>승인완료</option>
											<option value="X" <c:if test="${result.confmSeCode eq 'X'}">selected="selected"</c:if>>반려</option>
										</select>
									</td>
								</tr>
								<tr id="resn" <c:if test="${result.confmSeCode ne 'X'}">style="display:none;"</c:if>>
									<th>반려사유</th>
									<td>
										<textarea rows="5" id="returnResn" name="returnResn"><c:out value="${result.returnResn}"/></textarea>
									</td>
								</tr>
							</tbody>
						</table>
					</form>
					
					<div class="btn-cont ar">
						<c:choose>
							<c:when test="${not empty searchVO.resveId}">
								<a href="#" id="btn-upt" class="btn">수정</a>
								<c:url var="delUrl" value="/admin/rsv/rsvApplyDelete.do${_BASE_PARAM}">
									<c:param name="reqstId" value="${result.reqstId}"/>
								</c:url>
								<a href="${delUrl}" id="btn-del" class="btn"><i class="ico-del"></i>삭제</a>
							</c:when>
						<c:otherwise>
							<a href="#none" id="btn-reg" class="btn spot">등록</a>
						</c:otherwise>
						</c:choose>
						<c:url var="listUrl" value="/admin/rsv/rsvSelectList.do${_BASE_PARAM}"/>
						<a href="${listUrl}" class="btn">취소</a>
					</div>
				</div>
			</div>
		</div>
		<!-- //현재위치 네비게이션 끝 -->
	</div>
	<!--//container 끝  -->
	
	<!-- footer 시작 -->
	<div id="footer"><c:import url="/EgovPageLink.do?link=main/inc/EgovIncFooter"/></div>
	<!-- //footer 끝 -->
</div>
	<script>
		$(document).ready(function(){
			$("#confmSeCode").change(function() {
			var val = $(this).val();
			if(val == "X") {
				$("#resn").show();
			} else {
				$("#resn").hide();
			}
		});
		
		//예약자 삭제
		$("#btn-del").click(function(){
			if(!confirm("삭제하시겠습니까?")){
			 	return false;
			}
		});
		
		//승인
		$("#btn-upt").click(function(){
			$("#frm").submit();
			return false;
		});
		
	});
	
	//vali
	function regist() {
		alert(11);
		if($("#confmSeCode").val() == "X" && !$("#returnResn").val()){
			alert("반려 사유를 작성해주세요.");
			$("#returnResn").focus();
			return false;
		}
	}
	</script>
</body>
</html>