package egovframework.let.temptemp.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.let.temptemp.service.TempTempService;
import egovframework.let.temptemp.service.TempTempVO;
import egovframework.rte.psl.dataaccess.util.EgovMap;
@Service("tempTempService")
public class TempTempServiceImpl implements TempTempService {

	@Resource(name="tempTempMapper")
	TempTempMapper tempTempMapper;
	
	@Override
	public TempTempVO selectTempTemp(TempTempVO vo) {
		return tempTempMapper.selectTempTemp(vo);
	}

	@Override
	public List<EgovMap> selectListTempTemp(TempTempVO vo) {
		return tempTempMapper.selectListTempTemp(vo);
	}

	@Override
	public int selectListCntTempTemp(TempTempVO vo) {
		return tempTempMapper.selectListCntTempTemp(vo);
	}

	@Override
	public void insertTempTemp(TempTempVO vo) {
		tempTempMapper.insertTempTemp(vo);
	}

	@Override
	public void updateTempTemp(TempTempVO vo) {
		tempTempMapper.updateTempTemp(vo);
	}

	@Override
	public void deleteTempTemp(TempTempVO vo) {
		tempTempMapper.deleteTempTemp(vo);
	}

}
