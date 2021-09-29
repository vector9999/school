package egovframework.let.board.web;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import egovframework.com.cmm.LoginVO;
import egovframework.com.cmm.util.EgovUserDetailsHelper;
import egovframework.let.board.service.BoardService;
import egovframework.let.board.service.BoardVO;
import egovframework.let.utl.fcc.service.EgovStringUtil;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

@Controller
public class BoardController {

	@Resource(name = "boardService")
	BoardService boardService;
	

	//게시물 목록 가져오기
	@RequestMapping(value = "/board/selectList.do")
	public String selectList(@ModelAttribute("searchVO") BoardVO searchVO, HttpServletRequest request, ModelMap model) throws Exception {
		//공지 게시글
		searchVO.setNoticeAt("Y");
		List<EgovMap> noticeResultList = boardService.selectBoardList(searchVO);
		model.addAttribute("noticeResultList", noticeResultList);
		
		PaginationInfo paginationInfo = new PaginationInfo();
		
		paginationInfo.setCurrentPageNo(searchVO.getPageIndex()); //현재 페이지
		paginationInfo.setRecordCountPerPage(searchVO.getPageUnit()); //페이지 갯수
		paginationInfo.setPageSize(searchVO.getPageSize()); //페이지 사이즈
		
		searchVO.setFirstIndex(paginationInfo.getFirstRecordIndex()); //페이징 SQL의 조건절에 사용되는 시작 rownum
		searchVO.setLastIndex(paginationInfo.getLastRecordIndex()); //페이징 SQL의 조건절에 사용되는 마지막 rownum
		searchVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage()); //한 페이지당 게시되는 게시물 건 수
		
		//일반게시글 - 우리는 공지+일반글 가져오게끔 - 사실 방법이 두가지. 공지글과 일반글을 아예 별도로 분리
		searchVO.setNoticeAt("N");
		List<EgovMap> resultList = boardService.selectBoardList(searchVO);
		model.addAttribute("resultList", resultList);
		
		//검색 목록 총 수
		int totCnt = boardService.selectBoardListCnt(searchVO);
		paginationInfo.setTotalRecordCount(totCnt); //전체 게시물 수
		model.addAttribute("paginationInfo", paginationInfo);
		
		//사용자정보
		LoginVO user = (LoginVO)EgovUserDetailsHelper.getAuthenticatedUser();
		model.addAttribute("USER_INFO", user);
		return "board/BoardSelectList";
		
	}

	//게시물 가져오기
	@RequestMapping(value = "board/select.do")
	public String select(@ModelAttribute("searchVO") BoardVO searchVO, HttpServletRequest request, ModelMap model) throws Exception {
		LoginVO user = (LoginVO)EgovUserDetailsHelper.getAuthenticatedUser(); //로그인 정보 가져옴 
		model.addAttribute("USER_INFO", user);
		
		BoardVO result = boardService.selectBoard(searchVO); //게시글에 대한 상세 여부를 가져옴
		//비밀글 여부 체크
		if("Y".equals(result.getOthbcAt())) {
			//본인 및 관리자만 허용
			if(user == null || user.getId() == null || (!user.getId().equals(result.getFrstRegisterId()) && !"admin".equals(user.getId()))) { //초보자가 하는 실수 이걸 java가 아닌 jsp에다가 하는 것, 보안을 위해서 반드시 서버단에서 해줘야함
				model.addAttribute("message", "작성자 본인만 확인 가능합니다.");
				return "forward:/board/selectList.do";
			}
		}
		model.addAttribute("result", result);
		return "board/BoardSelect";
		}
	
	//게시글 등록/수정
	@RequestMapping(value = "/board/boardRegist.do")
	public String boardRegist(@ModelAttribute("searchVO")BoardVO BoardVO, HttpServletRequest request, ModelMap model) throws Exception {
		LoginVO user = (LoginVO)EgovUserDetailsHelper.getAuthenticatedUser(); //로그인 정보 가져옴 
		if(user == null || user.getId() == null) { //비회원 권한 비허용 - 보안문제
			model.addAttribute("message", "로그인 후 사용 가능합니다.");
			return "forward:/board/selectList.do"; //등록하지 못하게 list로 보냄
		} else {
			model.addAttribute("USER_INFO", user); //유저 정보가 있다면 담음
		}
		
		BoardVO result = new BoardVO();	//아이디가 없을 경우는 빈값으로 가고, 아이디가 있을 경우는 맞춰서 조회
		if(!EgovStringUtil.isEmpty(BoardVO.getBoardId())) {
			result = boardService.selectBoard(BoardVO); //아이디가 있는 경우, 상세정보를 조회
			//본인 및 관리자만 허용
			if(!user.getId().equals(result.getFrstRegisterId()) && !"admin".equals(user.getId())) { //상세조회를 했을때, 본인것인지 관리자 것인지 - (보안측면)jsp에서만 하는게 아니고 java에서도 방어해주어야함!
				model.addAttribute("message", "작성자 본인만 확인 가능합니다.");
				return "forward:/board/selectList.do";
			}
		}
		model.addAttribute("result", result); //해당 정보가 맞으면 result 반환
		request.getSession().removeAttribute("sessionBoard");
		return "board/BoardRegist";
	}
	
	//게시물 등록하기
	@RequestMapping(value = "/board/insert.do")
		public String insert(@ModelAttribute("searchVO")BoardVO searchVO, HttpServletRequest request, ModelMap model) throws Exception {
		 //이중 서브밋 방지 체크 - f5누를떄 무제한 등록되는것 방지
			if(request.getSession().getAttribute("sesssionBoard") != null) { //get으로 값이 있는지 확인, 있다면 이중 서브밋이기 때문에 리스트로 가도록
				return "forward:/board/selectList.do";
			}
			
			LoginVO user = (LoginVO)EgovUserDetailsHelper.getAuthenticatedUser(); //로그인 이후에 등록이 가능하기 때문에 쓰여진 코드
			if(user == null) {
				model.addAttribute("message", "로그인 후 사용가능합니다.");
				return "forward:/board/selectList.do";
			}
			searchVO.setCreatIp(request.getRemoteAddr()); //클라이언트 아이피를 set - browser 통신할때 header에 사용자 ip정보가 들어있음. 내부 ip가 아니고 공용 ip
			searchVO.setUserId(user.getId()); //등록할때 해당 사용자의 정보 저장
			
			boardService.insertBoard(searchVO); //저장
			
			//이중 서브밋 방지
			request.getSession().setAttribute("sessionBoard", searchVO); //insert 하고나서 세션에 값을 넣어주고 맨위에선 이를 체크해서 이중 등록 방지를 해줌
			return "forward:/board/selectList.do";
		}
	
	//게시물 수정하기
	@RequestMapping(value = "/board/update.do")
	public String update(@ModelAttribute("searchVO")BoardVO searchVO, HttpServletRequest request, ModelMap model) throws Exception {
		//이중 서브밋 방지
		if(request.getSession().getAttribute("sessionBoard") != null) {
			return "forward:/board/selectList.do";
		}
		
		LoginVO user = (LoginVO)EgovUserDetailsHelper.getAuthenticatedUser(); //로그인 이후에 등록이 가능하기 때문에 쓰여진 코드
		if(user == null) {
			model.addAttribute("message", "로그인 후 사용가능합니다.");
			return "forward:/board/selectList.do";
		} else if("admin".equals(user.getId())) {
			searchVO.setMngAt("Y");
		}
		
		searchVO.setUserId(user.getId());
		boardService.updateBoard(searchVO);
		
		//이중 서브밋 방지
		request.getSession().setAttribute("sessionBoard", searchVO);
		return "forward:/board/selectList.do";
	}
	
	//게시물 삭제하기
	@RequestMapping(value = "/board/delete.do")
	public String delete(@ModelAttribute("searchVO")BoardVO searchVO, HttpServletRequest request, ModelMap model) throws Exception {
	
		LoginVO user = (LoginVO)EgovUserDetailsHelper.getAuthenticatedUser(); //로그인 이후에 등록이 가능하기 때문에 쓰여진 코드
		if(user == null) {
			model.addAttribute("message", "로그인 후 사용가능합니다.");
			return "forward:/board/selectList.do";
		} else if("admin".equals(user.getId())) {
			searchVO.setMngAt("Y");
		}
		
		searchVO.setUserId(user.getId());
		boardService.deleteBoard(searchVO);
		
		return "forward:/board/selectList.do";
	}
}












