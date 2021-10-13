package egovframework.let.admin.rsv.web;

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
import egovframework.let.rsv.service.ReservationService;
import egovframework.let.rsv.service.ReservationVO;
import egovframework.let.utl.fcc.service.EgovStringUtil;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

@Controller
public class ReservationAdminController {

	@Resource(name = "reservationService")
	private ReservationService reservationService;
	

	//예약정보 목록 가져오기
	@RequestMapping(value = "/admin/rsv/rsvSelectList.do")
	public String selectList(@ModelAttribute("searchVO") ReservationVO searchVO, HttpServletRequest request, ModelMap model) throws Exception {

		PaginationInfo paginationInfo = new PaginationInfo();
		
		paginationInfo.setCurrentPageNo(searchVO.getPageIndex()); //현재 페이지
		paginationInfo.setRecordCountPerPage(searchVO.getPageUnit()); //페이지 갯수
		paginationInfo.setPageSize(searchVO.getPageSize()); //페이지 사이즈
		
		searchVO.setFirstIndex(paginationInfo.getFirstRecordIndex()); //페이징 SQL의 조건절에 사용되는 시작 rownum
		searchVO.setLastIndex(paginationInfo.getLastRecordIndex()); //페이징 SQL의 조건절에 사용되는 마지막 rownum
		searchVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage()); //한 페이지당 게시되는 게시물 건 수
		
		List<EgovMap> resultList = reservationService.selectReservationList(searchVO);
		model.addAttribute("resultList", resultList);
		
		//검색 목록 총 수
		int totCnt = reservationService.selectReservationListCnt(searchVO);
		paginationInfo.setTotalRecordCount(totCnt); //전체 게시물 수
		model.addAttribute("paginationInfo", paginationInfo);
		
		//사용자정보
		LoginVO user = (LoginVO)EgovUserDetailsHelper.getAuthenticatedUser();
		model.addAttribute("USER_INFO", user);
		return "admin/rsv/RsvSelectList";
	}
	
	//예약정보 등록/수정
	@RequestMapping(value = "/admin/rsv/rsvRegist.do")
	public String rsvRegist(@ModelAttribute("searchVO")ReservationVO ReservationVO, HttpServletRequest request, ModelMap model) throws Exception {
		LoginVO user = (LoginVO)EgovUserDetailsHelper.getAuthenticatedUser(); //로그인 정보 가져옴 
		if(user == null || user.getId() == null) { //비회원 권한 비허용 - 보안문제
			model.addAttribute("message", "로그인 후 사용 가능합니다.");
			return "forward:/admin/rsv/rsvSelectList.do"; //등록하지 못하게 list로 보냄
		} else {
			model.addAttribute("USER_INFO", user); //유저 정보가 있다면 담음
		}
		
		ReservationVO result = new ReservationVO();	//아이디가 없을 경우는 빈값으로 가고, 아이디가 있을 경우는 맞춰서 조회
		if(!EgovStringUtil.isEmpty(ReservationVO.getResveId())) {
			result = reservationService.selectReservation(ReservationVO); //아이디가 있는 경우, 상세정보를 조회
		}
		model.addAttribute("result", result); //해당 정보가 맞으면 result 반환
		request.getSession().removeAttribute("sessionReservation");
		return "admin/rsv/RsvRegist";
	}
	
	
	//예약정보 등록하기
	@RequestMapping(value = "/admin/rsv/rsvInsert.do")
		public String insert(@ModelAttribute("searchVO")ReservationVO searchVO, HttpServletRequest request, ModelMap model) throws Exception {
		 //이중 서브밋 방지 체크 - f5누를떄 무제한 등록되는것 방지
			if(request.getSession().getAttribute("sesssionReservation") != null) { //get으로 값이 있는지 확인, 있다면 이중 서브밋이기 때문에 리스트로 가도록
				return "forward:/admin/rsv/rsvSelectList.do";
			}
			
			LoginVO user = (LoginVO)EgovUserDetailsHelper.getAuthenticatedUser(); //로그인 이후에 등록이 가능하기 때문에 쓰여진 코드
			if(user == null) {
				model.addAttribute("message", "로그인 후 사용가능합니다.");
				return "forward:/admin/rsv/rsvSelectList.do";
			}
			
			searchVO.setUserId(user.getId()); //등록할때 해당 사용자의 정보 저장
			
			reservationService.insertReservation(searchVO); //저장
			
			//이중 서브밋 방지
			request.getSession().setAttribute("sessionReservation", searchVO); //insert 하고나서 세션에 값을 넣어주고 맨위에선 이를 체크해서 이중 등록 방지를 해줌
			return "forward:/admin/rsv/rsvSelectList.do";
		}
	
	
	//예약정보 수정하기
	@RequestMapping(value = "/admin/rsv/rsvUpdate.do")
	public String update(@ModelAttribute("searchVO")ReservationVO searchVO, HttpServletRequest request, ModelMap model) throws Exception {
		//이중 서브밋 방지
		if(request.getSession().getAttribute("sessionReservation") != null) {
			return "forward:/admin/rsv/rsvSelectList.do";
		}
		
		LoginVO user = (LoginVO)EgovUserDetailsHelper.getAuthenticatedUser(); //로그인 이후에 등록이 가능하기 때문에 쓰여진 코드
		if(user == null) {
			model.addAttribute("message", "로그인 후 사용가능합니다.");
			return "forward:/admin/rsv/rsvSelectList.do";
		}
		searchVO.setUserId(user.getId());
		reservationService.updateReservation(searchVO);
		
		//이중 서브밋 방지
		request.getSession().setAttribute("sessionReservation", searchVO);
		return "forward:/admin/rsv/rsvSelectList.do";
	}
	
	//예약정보 삭제하기
	@RequestMapping(value = "/admin/rsv/rsvDelete.do")
	public String delete(@ModelAttribute("searchVO")ReservationVO searchVO, HttpServletRequest request, ModelMap model) throws Exception {
	
		LoginVO user = (LoginVO)EgovUserDetailsHelper.getAuthenticatedUser(); //로그인 이후에 등록이 가능하기 때문에 쓰여진 코드
		if(user == null) {
			model.addAttribute("message", "로그인 후 사용가능합니다.");
			return "forward:/admin/rsv/rsvSelectList.do";
		}
		
		searchVO.setUserId(user.getId());
		reservationService.deleteReservation(searchVO);
		
		return "forward:/admin/rsv/rsvSelectList.do";
	}
		
}		
	

/*	
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
		
*/
		












