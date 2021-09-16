package egovframework.let.temptemp.service;

import java.util.List;

import egovframework.rte.psl.dataaccess.util.EgovMap;

public interface TempTempService {
	public TempTempVO selectTempTemp(TempTempVO vo);
	public List<EgovMap> selectListTempTemp(TempTempVO vo);
	public int selectListCntTempTemp(TempTempVO vo);
	public void  insertTempTemp(TempTempVO vo);
	public void  updateTempTemp(TempTempVO vo);
	public void  deleteTempTemp(TempTempVO vo);
}
