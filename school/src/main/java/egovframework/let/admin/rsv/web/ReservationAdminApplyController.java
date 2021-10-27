package egovframework.let.admin.rsv.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.JsonViewResponseBodyAdvice;

import egovframework.com.cmm.LoginVO;
import egovframework.com.cmm.service.EgovFileMngUtil;
import egovframework.com.cmm.service.FileVO;
import egovframework.com.cmm.service.Globals;
import egovframework.com.cmm.service.JsonResponse;
import egovframework.com.cmm.util.EgovUserDetailsHelper;
import egovframework.let.rsv.service.ReservationApplyService;
import egovframework.let.rsv.service.ReservationApplyVO;
import egovframework.let.rsv.service.ReservationService;
import egovframework.let.rsv.service.ReservationVO;
import egovframework.let.utl.fcc.service.EgovStringUtil;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

@Controller
public class ReservationAdminApplyController {
	
	@Resource(name = "reservationServiceApply")
	private ReservationApplyService reservationServiceApply;
	
	@Resource(name = "reservationService")
	private ReservationService reservationService;
	
	@Resource(name = "EgovFileMngUtil")
	private EgovFileMngUtil fileUtil;
	
	
	//예약자정보 목록 가져오기
	@RequestMapping(value = "/admin/rsv/selectApplyList.do")
	public String selectApplyList(@ModelAttribute("searchVO") ReservationApplyVO searchVO, HttpServletRequest request, ModelMap model) throws Exception {
		LoginVO user = (LoginVO)EgovUserDetailsHelper.getAuthenticatedUser();
		if(user == null || user.getId() == null) {
			model.addAttribute("message", "로그인 후 사용 가능합니다.");
			//return "forward:" + Globals.MAIN_PAGE;
		}
		
		//관리자
		searchVO.setMngAt("Y");
		
		List<EgovMap> resultList = reservationServiceApply.selectReservationApplyList(searchVO);
		model.addAttribute("resultList", resultList);
		
		//엑셀 다운로드
		if("Y".equals(searchVO.getExcelAt())) {
			return "admin/rsv/RsvApplySelectListExcel";
		}
		
		return "admin/rsv/RsvApplySelectList";
	}
	
	//예약자정보 상세 
	@RequestMapping(value = "/admin/rsv/rsvApplySelect.do")
	public String rsvApplySelect(@ModelAttribute("searchVO") ReservationApplyVO searchVO, HttpServletRequest request, ModelMap model) throws Exception {
		LoginVO user = (LoginVO)EgovUserDetailsHelper.getAuthenticatedUser();
		if(user == null || user.getId() == null) {
			model.addAttribute("message", "로그인 후 사용 가능합니다.");
			//return "forward:" + Globals.MAIN_PAGE;
		} else {
			model.addAttribute("USER_INFO",user);
		}
		
		ReservationApplyVO result = reservationServiceApply.selectReservationApply(searchVO);
		model.addAttribute("result", result);
		return "admin/rsv/RsvApplySelect";
		}
	
	//예약정보 승인
	@RequestMapping(value = "/admin/rsv/rsvApplyConfirm.do")
	public String updateReservationConfirm(@ModelAttribute("searchVO") ReservationApplyVO searchVO, HttpServletRequest request, ModelMap model) throws Exception {
		//이중 서브밋 방지
		if(request.getSession().getAttribute("sessionReservationApply") != null) {
			return "forward:/admin/rsv/selectApplyList.do";
		}	
		LoginVO user = (LoginVO)EgovUserDetailsHelper.getAuthenticatedUser();
		if(user == null || user.getId() == null) {
			model.addAttribute("message", "로그인 후 사용 가능합니다.");
			//return "forward:" + Globals.MAIN_PAGE;
		}
		
		searchVO.setUserId(user.getId());
		
		reservationServiceApply.updateReservationConfirm(searchVO);
		
		//이중 서브밋 방지
		request.getSession().setAttribute("sessionReservation", searchVO);
		return "forward:/admin/rsv/selectApplyList.do";
	
	}
	
	//예약정보 삭제하기
	@RequestMapping(value = "/admin/rsv/rsvApplyDelete.do")
	public String rsvApplyDelete(@ModelAttribute("searchVO") ReservationApplyVO searchVO, HttpServletRequest request, ModelMap model) throws Exception {
		LoginVO user = (LoginVO)EgovUserDetailsHelper.getAuthenticatedUser();
		if(user == null || user.getId() == null) {
			model.addAttribute("message", "로그인 후 사용 가능합니다.");
			//return "forward:" + Globals.MAIN_PAGE;
		}
		
		searchVO.setUserId(user.getId());
		
		reservationServiceApply.deleteReservationApply(searchVO);
		
		return "forward:/admin/rsv/selectApplyList.do";
	}
	
	//예약자정보 엑셀 다운로드
	@RequestMapping(value = "/admin/rsv/excel.do")
	public ModelAndView excel(@ModelAttribute("searchVO") ReservationApplyVO searchVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		List<String> columMap = new ArrayList<String>();
		List<Object> valueMap = new ArrayList<Object>();
		String fileName = "";
		
		columMap.add("번호"); //나오게 할 컬럼명들.
		columMap.add("신청자명");	
		columMap.add("연락처");	
		columMap.add("이메일");	
		columMap.add("신청일");	
		
		map.put("title", "예약신청현황");
		fileName = EgovStringUtil.getConvertFileName(request, "예약신청현황");
		
		//관리자
		searchVO.setMngAt("Y");
		//목록
		List<EgovMap> resultList = reservationServiceApply.selectReservationApplyList(searchVO);
		
		if(resultList != null) { //만약 값에 날짜값이 들어가게 된다면, 명칭을 바꿔서 받아오는 것이 좋다. 코드가 길어지기 때문에.  - 쿼리로 가져오는 것이 좋다는 뜻
			EgovMap tmpVO = null;
			Map<String, Object> tmpMap = null;
			for(int i=0; i<resultList.size(); i++) { //for문이 돌면서 valueMap이 계속 쌓이게 되는 것.
				tmpVO = resultList.get(i);
				
				tmpMap = new HashMap<String, Object>();
				tmpMap.put("번호", i+1);
				tmpMap.put("신청자명", tmpVO.get("chargerNm").toString() + "(" + tmpVO.get("frstRegisterId").toString() + ")");
				tmpMap.put("연락처", tmpVO.get("telno").toString());
				tmpMap.put("이메일", tmpVO.get("email").toString());
				tmpMap.put("신청일", tmpVO.get("frstRegistPnttmYmd").toString());
				
				valueMap.add(tmpMap);
			}
		}
		
		map.put("columMap", columMap);
		map.put("valueMap", valueMap);
		
		response.setHeader("Content-Disposition", "attachment; filename=" + fileName + ".xls"); //jsp에서 작업한 header. 필수
		return new ModelAndView("excelDownloadView", "dataMap", map); //html을 modelandView로 엑셀로 떨어지게 만든 것. 이 값을 받아와서 excelDownloadView 안을 돌게 되는 것
		
	}
	
	//엑셀업로드
	@RequestMapping(value = "/admin/rsv/excelUpload.json", method=RequestMethod.POST)
	public @ResponseBody JsonResponse excelUpload(ReservationApplyVO searchVO, HttpServletRequest request, HttpServletResponse response, ModelMap model, MultipartHttpServletRequest multiRequest) throws Exception {
		LoginVO user = (LoginVO)EgovUserDetailsHelper.getAuthenticatedUser(); //admin권한 가진 사람만 접속할수 있도록 추가해도 됨.
		JsonResponse res = new JsonResponse(); //반환할 객체를 한번 선언
		res.setSuccess(true); //엑셀 파일은 업로드가 되고 나서! 읽는것. 서버로 들어와진 파일을 java에서 읽는 것. - 이미지(파일업로드 된 후, 읽게됨)와 유사
		
		try { //파일을 저장하는 부분.
		List<FileVO> result = null;
		final Map<String, MultipartFile> files = multiRequest.getFileMap();
		if(!files.isEmpty()) { //common library 사용.
			result = fileUtil.parseFileInf(files, "TEMP_", 0, null, "rsvFileStorePath");
			Map<String, Object> resultMap = new HashMap<>();
		
		for(FileVO file : result) {
			if("xls".equals(file.getFileExtsn()) || "xlsx".equals(file.getFileExtsn())) {
				searchVO.setCreatIp(request.getRemoteAddr());
				resultMap = reservationServiceApply.excelUpload(file, searchVO);
				if(!(Boolean)resultMap.get("success")) {
					res.setMessage(String.valueOf(resultMap.get("msg")));
					ArrayList resultList = (ArrayList) resultMap.get("resultList");
					res.setData(resultList);
					res.setSuccess(false);
					}
				}
			}
		}
	} catch(DataAccessException e) {
		res.setMessage(e.getLocalizedMessage());
		}
		
		return res;
	}
			
}

