package egovframework.let.temp.service.web;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import egovframework.let.temp.service.TempService;
import egovframework.let.temp.service.TempVO;

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
}