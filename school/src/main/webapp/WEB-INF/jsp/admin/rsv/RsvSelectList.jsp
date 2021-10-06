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
					<!-- 검색영역 -->
					<div id="bbs_search">
						<form name="frm" method="post" action="/admin/rsv/rsvSelectList.do"> <!-- 파라메터를 보내는 것이기 때문에 form으로 감싸져있음, post던 get으로 보내던 상관없음 -->
							<fieldset>
								<legend>검색조건입력폼</legend>
								<label for="ftext" class="hdn">검색분류선택</label>
								<select name="searchCondition" id="ftext"> <!-- searchCondition ==> 검색의 조건 거는 것, 하나 추가한다면 전체검색(all)정도  -->
									<option value="0" <c:if test="${searchVO.searchCondition eq '0'}">selected="selected"</c:if>>프로그램명</option>
									<option value="1" <c:if test="${searchVO.searchCondition eq '1'}">selected="selected"</c:if>>내용</option>
								</select>
								<label for="inp_text" class="hdn">검색어입력</label> <!-- searchKeyword, 검색어 입력부분, 작업할때 주의해야할 웹접근성 부분 input인데 label이 없다면 꼭 title을 써줘라.-->
								<input name="searchKeyword" value="${searchVO.searchKeyword}" type="text" class="inp_s" id="inp_text"/>
								<span class="bbtn_s">
									<input type="submit" value="검색" title="검색(수업용 예약관리 게시물 내)"/>
								</span>
							</fieldset>
						</form>
					</div>
					<!-- 목록영역 -->
					<div id="bbs_wrap">
						<div class="total">
							총 게시물
							<strong><c:out value="${paginationInfo.totalRecordCount}"/></strong> 건 | <!-- list쿼리에서 검색한 총 갯수, totalRecordCount ===> 전체가 아니고 검색한! 조건에 대한 게시물 수를 가져옴, 그래야 페이징 처리가 되기 때문 -->
							현재페이지<strong><c:out value="${paginationInfo.currentPageNo}"/></strong>/
							<c:out value="${paginationInfo.totalPageCount}"/>
						</div>
						<div class="bbs_list">
							<table class="list_table">
								<thead>
									<tr>
										<th class="num" scope="col">번호</th>
										<th class="tit" scope="col">프로그램명</th>
										<th scope="col">신청기간</th>
										<th scope="col">운영일</th>
										<th scope="col">운영시간</th>
										<th scope="col">강사명</th>
										<th scope="col">신청자</th>
										<th scope="col">관리</th>
									</tr>
								</thead>
								<tbody>
									<!-- 일반글 -->
									<c:forEach var="result" items="${resultList}" varStatus="status">
										<tr>
											<td class="num">
												<c:out value="${paginationInfo.totalRecordCount - ((searchVO.pageIndex - 1) * searchVO.pageUnit) - (status.count - 1)}"/> <!-- 순번, 총 갯수에서 -1씩 빠지게끔 하는게 고정된 로직. 재활용 -->
											</td>
											<td class="tit">
												<c:url var="updateUrl" value="/admin/rsv/rsvRegist.do${_BASE_PARAM}"> 
													<c:param name="resveId" value="${result.resveId}"/> 
													<c:param name="pageIndex" value="${searchVO.pageIndex}"/> 
												</c:url>
												<a href="${updateUrl}">
													<c:out value="${result.resveSj}"/>
												</a>
											</td>
											<td>
												<c:out value="${result.reqstBgnde}"/>~<br/>
												<c:out value="${result.reqstEndde}"/>
											</td>
											<td>
												<c:out value="${result.useBeginDt}"/>~<br/>
												<c:out value="${result.useEndDt}"/>
											</td>
											<td><c:out value="${result.useBeginTime}~${result.useEndTime}"/></td>
											<td><c:out value="${result.recNm}"/></td>
											<td>
												<c:url var="applyUrl" value="/admin/rsv/selectApplyList.do${_BASE_PARAM}">
												<c:param name="resveId" value="${result.resveId}"/>
												<c:param name="pageIndex" value="${searchVO.pageIndex}"/>
												</c:url>
												<a href="${applyUrl}" class="btn spot">신청자</a>
											</td>
											<td>
												<a href="${updateUrl}" class="btn spot">수정</a>
												</br></br>
												<c:url var="deleteUrl" value="/admin/rsv/rsvDelete.do${_BASE_PARAM}">
												<c:param name="resveId" value="${result.resveId}"/>
												<c:param name="pageIndex" value="${searchVO.pageIndex}"/>
												</c:url>
												<a href="${deleteUrl}" class="btn spot btn-del">삭제</a>
											</td>
										</tr>
									</c:forEach>
									<!-- 게시글이 없을 경우 -->
									<c:if test="${fn:length(resultList) == 0}"> <!-- 검색조건이 있는 시스템에서 필수요소 검색결과가 없으면 없다고 표시, 검색결과가 달렸으면 무조건적으로 들어가야함 -->
										<tr class="empty"><td colspan="8">검색 데이터가 없습니다.</td></tr>
									</c:if>
								</tbody>
							</table>
						</div>
						<div id="paging">
							<c:url var="pageUrl" value="/admin/rsv/rsvSelectList.do${_BASE_PARAM}"/>
							<c:set var="pagingParam"><c:out value="${pageUrl}"/></c:set>
							<ui:pagination paginationInfo="${paginationInfo}" type="image" jsFunction="${pagingParam}"/>
						</div>
					</div>
					<div class="btn-cont ar">
						<a href="/admin/rsv/rsvRegist.do" class="btn spot"><i class="ico-check-spot"></i>등록</a>
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
		<c:if test="${not empty message}"> /* 이슈가 있을때, 비밀글인데 다른사용자가 접근하려고 할때 메세지 띄우는 등 */
			alert("${message}");
		</c:if>
		
		//예약 글 삭제
		$(".btn-del").click(function(){
			if(!confirm("삭제하시겠습니까?")){
			 	return false;
			}
		});
	</script>
</body>
</html>