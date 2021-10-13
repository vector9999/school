package egovframework.let.rsv.web;

import java.io.PrintWriter;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import egovframework.com.cmm.LoginVO;
import egovframework.com.cmm.util.EgovUserDetailsHelper;
import egovframework.let.rsv.service.ReservationApplyService;
import egovframework.let.rsv.service.ReservationApplyVO;
import egovframework.let.rsv.service.ReservationService;
import egovframework.let.rsv.service.ReservationVO;
import egovframework.let.utl.fcc.service.EgovStringUtil;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import net.sf.json.JSONObject;

@Controller
public class ReservationApplyController {

	@Resource(name = "reservationServiceApply")
	private ReservationApplyService reservationServiceApply;
	
	@Resource(name = "reservationService")
	private ReservationService reservationService;
	

	/*//예약자정보 목록 가져오기
	@RequestMapping(value = "/rsv/selectApplyList.do")
	public String selectApplyList(@ModelAttribute("searchVO") ReservationVO searchVO, HttpServletRequest request, ModelMap model) throws Exception {

		PaginationInfo paginationInfo = new PaginationInfo();
		
		paginationInfo.setCurrentPageNo(searchVO.getPageIndex()); //현재 페이지
		paginationInfo.setRecordCountPerPage(searchVO.getPageUnit()); //페이지 갯수
		paginationInfo.setPageSize(searchVO.getPageSize()); //페이지 사이즈
		
		searchVO.setFirstIndex(paginationInfo.getFirstRecordIndex()); //페이징 SQL의 조건절에 사용되는 시작 rownum
		searchVO.setLastIndex(paginationInfo.getLastRecordIndex()); //페이징 SQL의 조건절에 사용되는 마지막 rownum
		searchVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage()); //한 페이지당 게시되는 게시물 건 수
		
		List<EgovMap> resultList = reservationServiceApply.selectReservationApplyList(searchVO);
		model.addAttribute("resultList", resultList);
		
		//검색 목록 총 수
		int totCnt = reservationServiceApply.selectReservationApplyListCnt(searchVO);
		paginationInfo.setTotalRecordCount(totCnt); //전체 게시물 수
		model.addAttribute("paginationInfo", paginationInfo);
		
		//사용자정보
		LoginVO user = (LoginVO)EgovUserDetailsHelper.getAuthenticatedUser();
		model.addAttribute("USER_INFO", user);
		return "/rsv/RsvApplySelectList";
	}
	
	//예약정보 상세 - 관리자 페이지와 달리 사용자 페이지에서는 상세정보가 필요함.
	@RequestMapping(value = "/rsv/rsvSelect.do")
	public String select(@ModelAttribute("searchVO")ReservationVO searchVO, HttpServletRequest request, ModelMap model) throws Exception {
		request.getSession().removeAttribute("sessionReservationApply");
		
		LoginVO user = (LoginVO)EgovUserDetailsHelper.getAuthenticatedUser();
		model.addAttribute("USER_INFO", user);
		
		ReservationVO result = reservationService.selectReservation(searchVO);
		
		model.addAttribute("result", result);
		return "/rsv/RsvSelect";
	
	}*/
		
		
	//예약정보 등록/수정
	@RequestMapping(value = "/rsv/rsvApplyRegist.do")
	public String rsvApplyRegist(@ModelAttribute("searchVO")ReservationApplyVO ReservationVO, HttpServletRequest request, ModelMap model) throws Exception {
		LoginVO user = (LoginVO)EgovUserDetailsHelper.getAuthenticatedUser(); //로그인 정보 가져옴 
		if(user == null || user.getId() == null) { //비회원 권한 비허용 - 보안문제
			model.addAttribute("message", "로그인 후 사용 가능합니다.");
			return "forward:/rsv/selectList.do"; //등록하지 못하게 list로 보냄
		} else {
			model.addAttribute("USER_INFO", user); //유저 정보가 있다면 담음
		}
		//프로그램 정보
		ReservationVO reservation = new ReservationVO();	//아이디가 없을 경우는 빈값으로 가고, 아이디가 있을 경우는 맞춰서 조회
		if(!EgovStringUtil.isEmpty(ReservationVO.getResveId())) {
			reservation = reservationService.selectReservation(ReservationVO); //아이디가 있는 경우, 상세정보를 조회
		}
		model.addAttribute("reservation", reservation); 
		
		//예약정보
		ReservationApplyVO result = new ReservationApplyVO();
		if(!EgovStringUtil.isEmpty(ReservationVO.getResveId())) {
			result = reservationServiceApply.selectReservationApply(ReservationVO); //아이디가 있는 경우, 상세정보를 조회
		}
		model.addAttribute("result", result); 
		
		request.getSession().removeAttribute("sessionReservationApply");
		
		return "/rsv/RsvApplyRegist";
	}
	
	
	//예약자정보 등록하기
	@RequestMapping(value = "/rsv/rsvApplyInsert.do")
		public String rsvApplyInsert(@ModelAttribute("searchVO")ReservationApplyVO searchVO, HttpServletRequest request, ModelMap model) throws Exception {
		 //이중 서브밋 방지 체크 - f5누를떄 무제한 등록되는것 방지
			if(request.getSession().getAttribute("sesssionReservation") != null) { //get으로 값이 있는지 확인, 있다면 이중 서브밋이기 때문에 리스트로 가도록
				return "forward:/rsv/selectList.do";
			}
			
			LoginVO user = (LoginVO)EgovUserDetailsHelper.getAuthenticatedUser(); //로그인 이후에 등록이 가능하기 때문에 쓰여진 코드
			if(user == null) {
				model.addAttribute("message", "로그인 후 사용가능합니다.");
				return "forward:/rsv/selectList.do";
			}
			
			searchVO.setUserId(user.getId()); //등록할때 해당 사용자의 정보 저장
			searchVO.setCreatIp(request.getRemoteAddr());
			
			ReservationApplyVO result = reservationServiceApply.insertReservationApply(searchVO);
			if(!EgovStringUtil.isEmpty(result.getErrorCode())) {
				model.addAttribute("message", result.getMessage());
			} else {
				model.addAttribute("message", "신청완료되었습니다.");
			}
			
			//이중 서브밋 방지
			request.getSession().setAttribute("sessionReservationApply", searchVO); //insert 하고나서 세션에 값을 넣어주고 맨위에선 이를 체크해서 이중 등록 방지를 해줌
			return "forward:/rsv/selectList.do";
		}
	
	
	//예약정보 수정하기
	@RequestMapping(value = "/rsv/rsvApplyUpdate.do")
	public String rsvApplyUpdate(@ModelAttribute("searchVO")ReservationApplyVO searchVO, HttpServletRequest request, ModelMap model) throws Exception {
		//이중 서브밋 방지
		if(request.getSession().getAttribute("sessionReservationApply") != null) {
			return "forward:/rsv/selectList.do";
		}
		
		LoginVO user = (LoginVO)EgovUserDetailsHelper.getAuthenticatedUser(); //로그인 이후에 등록이 가능하기 때문에 쓰여진 코드
		if(user == null) {
			model.addAttribute("message", "로그인 후 사용가능합니다.");
			return "forward:/rsv/selectList.do";
		}
		searchVO.setUserId(user.getId());
		reservationServiceApply.updateReservationApply(searchVO);
		
		//이중 서브밋 방지
		request.getSession().setAttribute("sessionReservationApply", searchVO);
		return "forward:/rsv/selectList.do";
	}
	
	//예약정보 삭제하기
	@RequestMapping(value = "/rsv/rsvApplyDelete.do")
	public String rsvApplyDelete(@ModelAttribute("searchVO")ReservationApplyVO searchVO, HttpServletRequest request, ModelMap model) throws Exception {
	
		LoginVO user = (LoginVO)EgovUserDetailsHelper.getAuthenticatedUser(); //로그인 이후에 등록이 가능하기 때문에 쓰여진 코드
		if(user == null) {
			model.addAttribute("message", "로그인 후 사용가능합니다.");
			return "forward:/rsv/selectList.do";
		}
		
		searchVO.setUserId(user.getId());
		reservationServiceApply.deleteReservationApply(searchVO);
		
		return "forward:/rsv/selectList.do";
	}
	
	
	//예약여부 체크
	@RequestMapping(value = "/rsv/rsvCheck.json")
	public void rsvCheck(@ModelAttribute("searchVO")ReservationApplyVO searchVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
		String successYn = "Y";
		String message = "성공";
		
		JSONObject jo = new JSONObject();
		response.setContentType("text/javascript; charset=utf-8");
		
		LoginVO user = (LoginVO)EgovUserDetailsHelper.getAuthenticatedUser();
		if(user == null) {
			successYn = "N";
			message = "로그인 후 사용 가능합니다.";
		}
		searchVO.setUserId(user.getId());
		
		ReservationApplyVO result = reservationServiceApply.rsvCheck(searchVO);
		if(!EgovStringUtil.isEmpty(result.getErrorCode())) {
			successYn = "N";
			message = result.getMessage();
		}
		
		jo.put("successYn", successYn);
		jo.put("message", message);
		
		PrintWriter printwriter = response.getWriter(); //json뿐 아니라 text 파일을 뿌릴때도 이 방식을 사용한다.
		printwriter.println(jo.toString());
		printwriter.flush();
		printwriter.close();
	}
}		
	
		












