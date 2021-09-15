package egovframework.let.temp.service.web;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import egovframework.let.temp.service.TempService;
import egovframework.let.temp.service.TempVO;
import egovframework.let.utl.fcc.service.EgovStringUtil;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

@Controller
public class TempController {

	@Resource(name = "tempService")
	private TempService tempService;
	
	//임시데이터 가져오기
	@RequestMapping(value = "/temp/select.do")
	public String select(@ModelAttribute("searchVO") TempVO searchVO, 
			HttpServletRequest request, ModelMap model) throws Exception{
		TempVO result = tempService.selectTemp(searchVO);
		model.addAttribute("result", result);
		return "temp/TempSelect";
	}
	
	//임시데이터 등록하기 (등록폼)
	@RequestMapping(value = "/temp/insert.do")
	public String insert(@ModelAttribute("searchVO") TempVO searchVO, HttpServletRequest request, ModelMap model) throws Exception {
		tempService.insertTemp(searchVO);
		//return "forward:/temp/selectList.do";
		return "redirect:/temp/selectList.do";
	}
	
	//임시데이터 등록
//	@RequestMapping(value = "/temp/tempRegist.do")
//	public String tempRegist(@ModelAttribute("searchVO") TempVO searchVO, HttpServletRequest request, ModelMap model) throws Exception {
//		return "temp/TempRegist";
//	}
	
	
	//임시데이터 목록 과제
//	@RequestMapping(value = "/temp/tempList.do")
//	public String listTemp(@ModelAttribute("listVO") TempVO listVO, HttpServletRequest request, ModelMap model) throws Exception {
//		List<TempVO> list = tempService.listTemp(listVO);
//		model.addAttribute("list",list);
//		return "temp/TempList";
//	}
	
	
	//✔ 이하 교수님 코드
	
	//임시데이터 등록/수정
	@RequestMapping(value = "/temp/tempRegist.do")
	public String tempRegist(@ModelAttribute("searchVO") TempVO tempVO, HttpServletRequest request, ModelMap model) throws Exception {
		TempVO result = new TempVO();
		if(!EgovStringUtil.isEmpty(tempVO.getTempId())) {
			result = tempService.selectTemp(tempVO);
		}
		model.addAttribute("result", result);
		
		return "temp/TempRegist";
	}
	
	//임시데이터 목록 가져오기 
	//목록을 만들기전에 VO에 상속을 하나 추가해주어야 한다. - 왜? 변수를 잡는거에는 공통이 있다. 페이징, 검색 이걸 모아둔 곳이 defaultVO-전자정부프레임에서 제공
	//변수를 매번 만드는게 아니고 상속 받을 수 있는건 상속 받아사용.
	//프로젝트에 공통으로 찾아야 할 것을 모두 모아둔 곳.
	//TempVO에 공통변수 모아둔 클래스 상속하고나서 작성 하자.
	@RequestMapping(value = "/temp/selectList.do")
	public String selectList(@ModelAttribute("searchVO") TempVO searchVO, HttpServletRequest resuest, ModelMap model) throws Exception {
		PaginationInfo paginationInfo = new PaginationInfo();
		
		paginationInfo.setCurrentPageNo(searchVO.getPageIndex()); //현재 페이지
		paginationInfo.setRecordCountPerPage(searchVO.getPageUnit()); //페이지 갯수
		paginationInfo.setPageSize(searchVO.getPageSize()); //페이지 사이즈
		
		searchVO.setFirstIndex(paginationInfo.getFirstRecordIndex()); //페이징 SQL의 조건절에 사용되는 시작 rownum
		searchVO.setLastIndex(paginationInfo.getLastRecordIndex()); //페이징 SQL의 조건절에 사용되는 마지막 rownum
		searchVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage()); //한 페이지당 게시되는 게시물 건 수
		
		List<EgovMap> resultList = tempService.selectTempList(searchVO);
		model.addAttribute("resultList", resultList);
		
		int totCnt = tempService.selectTempListCnt(searchVO);
		
		paginationInfo.setTotalRecordCount(totCnt); //전체 게시물 수
		model.addAttribute("paginationInfo", paginationInfo);
		
		return "temp/TempSelectList";
			
	}
	//임시데이터 수정하기
	@RequestMapping(value = "/temp/update.do")
	public String update(@ModelAttribute("searchVO") TempVO searchVO, HttpServletRequest request, ModelMap model) throws Exception {
		tempService.updateTemp(searchVO);
		return "forward:/temp/selectList.do";
	}
	
	//임시데이터 삭제하기
	@RequestMapping(value = "/temp/delete.do")
	public String delete(@ModelAttribute("searchVO") TempVO searchVO, HttpServletRequest request, ModelMap model) throws Exception {
		tempService.deleteTemp(searchVO);
		return "forward:/temp/selectList.do";
	}
	
}












