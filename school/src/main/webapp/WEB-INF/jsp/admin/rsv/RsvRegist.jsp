<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"  %>
 
	
<!DOCTYPE html>
<html>
<head>
<!-- 공통사용 meta tag -->
<meta http-equiv="Content-Language" content="ko">
<meta http-equiv="X-UA-Compatible" content="IE=edge"> <!-- 구버전 익플을 강제로 익플로 변경 시켜줌-->
<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, max-scale=1.0, user-scalable=no"> <!-- 반응형 : 현재 접속한 디바이스 크기를 최대크기를 디바이스 크기, initial-scale: 1배. 즉 확대 안함/유저 강제확대 불가 등 -->
<!-- 공통사용 meta tag 끝 -->

<c:choose>
	<c:when test="${not empty searchVO.resveId}">
		<c:set var="actionUrl" value="/admin/rsv/rsvUpdate.do"/>
	</c:when>
	<c:otherwise>
		<c:set var="actionUrl" value="/admin/rsv/rsvInsert.do"/>
	</c:otherwise>
</c:choose>

<c:url var="_BASE_PARAM" value=""> <!-- 중복으로 가장 많이 쓰이기 때문에 - view나 paging쪽에서 사용 -->
		<c:param name="menuNO" value="50"/><!-- 임의로 넣은 값  -->
		<c:if test="${not empty searchVO.searchCondition}"> <!-- searchCondition, searchKeyword ==> 검색 값. 검색 후 상세페이지로 갔다가 다시 목록으로 갈때 검색한 결과값 유지하기 위해서, 파라미터가 쫓아다님 -->
			<c:param name="searchCondition" value="${searchVO.searchCondition}"/>
		</c:if>
		<c:if test="${not empty searchVO.searchKeyword}">
			<c:param name="searchKeyword" value="${searchVO.searchKeyword}"/>
		</c:if>
</c:url>

<title>수업용 게시판</title>
<link href="/css/common.css" rel="stylesheet" type="text/css">
<!-- BBS Style -->
<link href="/asset/BBSTMP_0000000000001/style.css" rel="stylesheet"/>
<!-- 공통 Style -->
<link href="/asset/LYTTMP_0000000000000/style.css" rel="stylesheet"/>
<script  src="http://code.jquery.com/jquery-latest.min.js"></script>

<!-- JQuery UI -->
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script> <!-- jquery datepicker -->
<link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">

<script src="//cdnjs.cloudflare.com/ajax/libs/timepicker/1.3.5/jquery.timepicker.min.js"></script> <!-- jquery timepicker  -->
<link rel="stylesheet" href="//cdnjs.cloudflare.com/ajax/libs/timepicker/1.3.5/jquery.timepicker.min.css">

</head>
<body>
<!-- 전체 레이어 시작  -->
<div id="wrap">
	
		<!-- header 시작  -->
		<div id="header_mainsize"><c:import url="/EgovPageLink.do?link=main/inc/EgovIncHeader"/></div>
		<div id="topnavi"><c:import url="/EgovPageLink.do?link=main/inc/EgovIncTopnav"/></div>
		<!-- //header 끝 -->
		
		<!-- container 시작 -->
		<div id="container">
			<!-- 좌측메뉴 시작 -->
			<div id="leftmenu">
				<c:import url="/EgovPageLink.do?link=main/inc/EgovIncLeftmenu"/>
			</div>
			<!-- //좌측메뉴 끝 -->
			
			<!-- 현재위치 네비게이션 시작 -->
			<div id="content">
				<div class="container">
				<div id="contents">
					<form action="${actionUrl}" method="post" id="frm" name="frm" onsubmit="return regist()">
						<input type="hidden" name="resveId" value="${result.resveId}"/>
						<input type="hidden" name="resveSeCode" value="TYPE01"/>
						
						<table class="chart2">
							<caption>예약정보 작성</caption>
							<colgroup>
								<col style="width"120px"/>
								<col/>
							</colgroup>
							<tbody>
								<tr>
									<th scope="row">프로그램명</th>
									<td>
										<input type="text" id="resveSj" name="resveSj" title="제목입력" class="q3" value="<c:out value="${result.resveSj}"/>"/>
									</td>
								</tr>
								<tr>
									<th scope="row">운영기간</th>
									<td>
										<input type="text" id="useBeginDt" class="datepicker" name="useBeginDt" title="운영시작일" value="<c:out value="${result.useBeginDt}"/>" readonly="readonly" /> <!-- datepicker에서 readonly속성은 필수임  -->
										~
										<input type="text" id="useEndDt" class="datepicker" name="useEndDt" title="운영종료일" value="<c:out value="${result.useEndDt}"/>" readonly="readonly" />
									</td>
								</tr>
								<tr>
									<th scope="row">운영시간</th>
									<td>
										<input type="text" id="useBeginTime" class="timepicker" name="useBeginTime" title="운영시작시간" value="<c:out value="${result.useBeginTime}"/>" readonly="readonly" /> 
										~
										<input type="text" id="useEndTime" class="timepicker" name="useEndTime" title="운영종료시간" value="<c:out value="${result.useEndTime}"/>" readonly="readonly" /> 
									</td>
								</tr>
								<tr>
									<th scope="row">신청기간</th>
									<td>
										<input type="text" id="reqstBgnde" class="datepicker" name="reqstBgnde" title="신청시작일" value="<c:out value="${result.reqstBgnde}"/>" readonly="readonly" /> 
										~
										<input type="text" id="reqstEndde" class="datepicker" name="reqstEndde" title="신청종료일" value="<c:out value="${result.reqstEndde}"/>" readonly="readonly" />
									</td>
								</tr>
								<tr>
									<th scope="row">강사명</th>
									<td>
										<input type="text" id="recNm" name="recNm" title="강사명" value="<c:out value="${result.recNm}"/>"/> 
									</td>
								</tr>
								<tr>
									<th scope="row">신청인원수</th>
									<td>
										<input type="number" id="maxAplyCnt" name="maxAplyCnt" title="신청인원수" value="<c:out value="${result.maxAplyCnt}"/>"/>
									</td>
								</tr>
								<tr>
									<th scope="row">내용</th>
									<td>
										<textarea id="resveCn" name="resveCn" rows="15" title="내용입력"><c:out value="${result.resveCn}"/></textarea> 
									</td>
								</tr>
							</tbody>
						</table>
						<div class="btn-cont ar">
							<c:choose>
								<c:when test="${not empty searchVO.resveId}"> <!-- pk값이 있으면 수정, pk값이 없으면 등록 -->
									<c:url var="uptUrl" value="/admin/rsv/rsvRegist.do${_BASE_PARAM}">
										<c:param name="resveId" value="${result.resveId}"/>
									</c:url>
									<a href="${uptUrl}" id="btn-reg" class="btn">수정</a>
									<c:url var="delUrl" value="/admin/rsv/rsvDelete.do${_BASE_PARAM}">
										<c:param name="resveId" value="${result.resveId}"/>
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
					</form>
				</div>
			</div>
		</div>
		<!-- //현재위치 네비게이션 끝 -->
	</div>
	<!-- //container 끝 -->
	
	<!-- footer 시작 -->
	<div id="footer"><c:import url="/EgovPageLink.do?link=main/inc/EgovIncFooter"/></div>
	<!-- //footer 끝 -->
</div>
<script>
	$(document).ready(function() {
		//datepicker 세팅
		$(".datepicker").datepicker({ /* datepicker 한국의 날짜포맷은 년월일이기 때문에 */
			dateFormat: 'yy-mm-dd'
		});
		
		//timepicker 세팅
		$(".timepicker").timepicker({
			timeFormat: 'HH:mm', /* HH:24시간 기준 시간표시 */
			interval: 60, /* 간격. 10분단위로 받고싶으면 10이라고 설정. 10분단위로 끊어져서 나옴. */
			minTime: '10',
			maxTime: '18:00', /* 10시부터 오후 6시까지 선택 가능 */
			startTime: '10:00' /* 클릭했을때 가장 먼저 나오게 할 옵션 값. 13시에 많이 예약한다 - 그럼 13:00을 디폴트 값으로 두면 될 것. */
		});
		
		//예약정보 등록
		$("#btn-reg").click(function(){
			$("#frm").submit();
			return false;
		});
		
		//예약글 삭제
		$("#btn-del").click(function(){
			if(!confirm("삭제 하시겠습니까?")) {
				return false;
			}
		});
	});
	
	function regist() {
		if(!$("#resveSj").val()) {
			alert("프로그램명을 입력해주세요.");
			return false;
		}
	}
</script>

</body>
</html>