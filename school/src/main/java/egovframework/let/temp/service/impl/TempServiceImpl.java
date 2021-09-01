package egovframework.let.temp.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.let.temp.service.TempService;
import egovframework.let.temp.service.TempVO;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import egovframework.rte.fdl.property.EgovPropertyService;
@Service("tempService")
public class TempServiceImpl extends EgovAbstractServiceImpl implements TempService {

	@Resource(name = "propertiesService")
	protected EgovPropertyService propertyService;
	
	/*@Resource(name = "tempMapper")
	private TempMapper tempMapper;
	*/
	
	@Resource(name = "tempDAO")
	private TempDAO tempDAO;
	
	/*@Override
	public TempVO selectTemp(TempVO vo) throws Exception {
		return tempMapper.selectTemp(vo);
	}*/
	
	@Override
	public TempVO selectTemp(TempVO vo) throws Exception {
		return tempDAO.selectTemp(vo);
	}
}
