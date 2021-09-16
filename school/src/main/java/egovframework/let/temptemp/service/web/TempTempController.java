package egovframework.let.temptemp.service.web;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import egovframework.let.temptemp.service.TempTempService;
import egovframework.let.temptemp.service.TempTempVO;
import egovframework.let.utl.fcc.service.EgovStringUtil;
import egovframework.rte.psl.dataaccess.util.EgovMap;

@Controller
public class TempTempController {
	@Resource(name="tempTempService")
	TempTempService tempTempService;
	//데이터
	@RequestMapping(value = "/temptemp/select.do")
	public String selectTempTemp(@ModelAttribute("searchVO") TempTempVO searchVO, ModelMap model) {
		TempTempVO result = tempTempService.selectTempTemp(searchVO);
		model.addAttribute("result", result);
		return "temptemp/TempSelect";
	}
	//목록
	//페이지네이션 빼고 작성중
	@RequestMapping(value = "/temptemp/selectList.do")
	public String selectListTempTemp(@ModelAttribute("searchVO")TempTempVO searchVO, ModelMap model) {
		List<EgovMap> resultList = tempTempService.selectListTempTemp(searchVO);
		model.addAttribute("resultList", resultList);
		return "temptemp/TempSelectList";
	}

	//등록폼
	@RequestMapping(value = "/temptemp/insert.do")
	public String insertTempTemp(@ModelAttribute TempTempVO searchVO) {
		tempTempService.insertTempTemp(searchVO);
		return "redirect:/temptemp/selectList.do";
	}
	//수정
	@RequestMapping(value = "/temptemp/update.do")
	public String updateTempTemp(@ModelAttribute TempTempVO searchVO) {
		tempTempService.updateTempTemp(searchVO);
		return "forward:/temptemp/selectList.do";
	}
	//삭제
	@RequestMapping(value = "/temptemp/delete.do")
	public String deleteTempTemp(@ModelAttribute TempTempVO searchVO) {
		tempTempService.deleteTempTemp(searchVO);
		return "forward:/temptemp/selectList.do";
	}
	
	//등록&수정 - ??????
	@RequestMapping(value = "/temptemp/tempRegist.do")
	public String registTempTemp(@ModelAttribute TempTempVO temptempVO, ModelMap model) {
		TempTempVO result = new TempTempVO();
		if(!EgovStringUtil.isEmpty(temptempVO.getTempId())) {
			result = tempTempService.selectTempTemp(temptempVO);
		}
		model.addAttribute("result", result);
		return "temptemp/TempRegist";
	}
	
}
