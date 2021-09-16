package egovframework.let.temptemp.service.impl;

import java.util.List;

import egovframework.let.temptemp.service.TempTempVO;
import egovframework.rte.psl.dataaccess.mapper.Mapper;
import egovframework.rte.psl.dataaccess.util.EgovMap;
@Mapper("tempTempMapper")
public interface TempTempMapper {
	TempTempVO selectTempTemp(TempTempVO vo);
	List<EgovMap> selectListTempTemp(TempTempVO vo);
	int selectListCntTempTemp(TempTempVO vo);
	void insertTempTemp(TempTempVO vo);
	void updateTempTemp(TempTempVO vo);
	void deleteTempTemp(TempTempVO vo);
}
