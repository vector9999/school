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
			<!-- 검색영역 -->
			<div id="bbs_search">
				<form name="frm" method="post" action="/board/selectList.do"> <!-- 파라메터를 보내는 것이기 때문에 form으로 감싸져있음, post던 get으로 보내던 상관없음 -->
					<fieldset>
						<legend>검색조건입력폼</legend>
						<label for="ftext" class="hdn">검색분류선택</label>
						<select name="searchCondition" id="ftext"> <!-- searchCondition ==> 검색의 조건 거는 것, 하나 추가한다면 전체검색(all)정도  -->
							<option value="0" <c:if test="${searchVO.searchCondition eq '0'}">selected="selected"</c:if>>제목</option>
							<option value="1" <c:if test="${searchVO.searchCondition eq '1'}">selected="selected"</c:if>>내용</option>
							<option value="2" <c:if test="${searchVO.searchCondition eq '2'}">selected="selected"</c:if>>작성자</option>
						</select>
						<label for="inp_text" class="hdn">검색어입력</label> <!-- searchKeyword, 검색어 입력부분, 작업할때 주의해야할 웹접근성 부분 input인데 label이 없다면 꼭 title을 써줘라.-->
						<input name="searchKeyword" value="${searchVO.searchKeyword}" type="text" class="inp_s" id="inp_text"/>
						<span class="bbtn_s">
							<input type="submit" value="검색" title="검색(수업용 게시판 게시물 내)"/>
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
								<th class="tit" scope="col">제목</th>
								<th class="writer" scope="col">작성자</th>
								<th class="date" scope="col">작성일</th>
								<th class="hits" scope="col">조회수</th>
							</tr>
						</thead>
						<tbody>
							<!-- 공지글 -->
							<c:forEach var="result" items="${noticeResultList}" varStatus="status">
								<tr class="notice">
									<td class="num"><span class="label-bbs spot">공지</span></td>
									<td class="tit">
										<c:url var="viewUrl" value="/board/select.do${_BASE_PARAM}">
											<c:param name="boardId" value="${result.boardId}"/>
											<c:param name="pageIndex" value="${searchVO.pageIndex}"/>
										</c:url>
										<a href="${viewUrl}"><c:out value="${result.boardSj}"/></a>
									</td>
									<td class="writer" data-cell-header="작성자 : "><c:out value="${result.frstRegisterId}"/></td>
									<td class="date" data-cell-header="작성일 : ">
										<fmt:formatDate value="${result.frstRegistPnttm}" pattern="yyyy-MM-dd"/>
									</td>
									<td class="hits" data-cell-header="조회수 : "><c:out value="${result.inqireCo}"/></td>
								</tr>
							</c:forEach>
							<!-- 일반글 -->
							<c:forEach var="result" items="${resultList}" varStatus="status">
								<tr>
									<td class="num">
										<c:out value="${paginationInfo.totalRecordCount - ((searchVO.pageIndex - 1) * searchVO.pageUnit) - (status.count - 1)}"/> <!-- 순번, 총 갯수에서 -1씩 빠지게끔 하는게 고정된 로직. 재활용 -->
									</td>
									<td class="tit">
										<c:url var="viewUrl" value="/board/select.do${_BASE_PARAM}"> <!-- 제목 눌렀을때 상세페이지로 가게끔 -->
											<c:param name="boardId" value="${result.boardId}"/> <!-- 상세페이지로 가기 위한 pk값 -->
											<c:param name="pageIndex" value="${searchVO.pageIndex}"/> <!-- 현재 몇페이지인지 알려주기 위해(페이징이 있을 경우에만 사용) -->
										</c:url>
										<a href="${viewUrl}">
											<c:if test="${result.othbcAt eq 'Y'}"> <!-- 비밀글: 관리자, 작성자만 조회가능 해아함 -->
												<img src="/asset/BBSTMP_0000000000001/images/ico_board_lock.gif" alt=""/>
											</c:if>
											<c:out value="${result.boardSj}"/>
										</a>
									</td>
									<td class="writer" data-cell-header="작성자 : "><c:out value="${result.frstRegisterId}"/></td>
									<td class="date" data-cell-header="작성일 : ">
										<fmt:formatDate value="${result.frstRegistPnttm}" pattern="yyyy-MM-dd"/> <!-- fmt를 사용했지만 DB에서 TO_CHAR()사용 할 수도 있음 - 단, 타입이 Date가 아닌 String이 될 것 -->
									</td>
									<td class="hits" data-cell-header="조회수 : "><c:out value="${result.inqireCo}"/></td> <!-- 상세페이지 이동할때마다 조회수 증가  -->
								</tr>
							</c:forEach>
							<!-- 게시글이 없을 경우 -->
							<c:if test="${fn:length(resultList) == 0}"> <!-- 검색조건이 있는 시스템에서 필수요소 검색결과가 없으면 없다고 표시, 검색결과가 달렸으면 무조건적으로 들어가야함 -->
								<tr class="empty"><td colspan="5">검색 데이터가 없습니다.</td></tr>
							</c:if>
						</tbody>
					</table>
				</div>
				<div id="paging">
					<c:url var="pageUrl" value="/board/selectList.do${_BASE_PARAM}"/>
					<c:set var="pagingParam"><c:out value="${pageUrl}"/></c:set>
					<ui:pagination paginationInfo="${paginationInfo}" type="image" jsFunction="${pagingParam}"/>
				</div>
			</div>
			<div class="btn-cont ar">
				<a href="/board/boardRegist.do" class="btn spot"><i class="ico-check-spot"></i>글쓰기</a>
			</div>
		</div>
	</div>
	<script>
		<c:if test="${not empty message}"> /* 이슈가 있을때, 비밀글인데 다른사용자가 접근하려고 할때 메세지 띄우는 등 */
			alert("${message}");
		</c:if>
	</script>
</body>
</html>