package egovframework.let.temp.service;

import java.util.List;

import egovframework.rte.psl.dataaccess.util.EgovMap;

public interface TempService {
	
	//임시데이터 가져오기
	public TempVO selectTemp(TempVO vo) throws Exception;
	
	//임시데이터 등록하기
	public String insertTemp(TempVO vo) throws Exception;
	
	//목록가져오기 과제 - 내코드
//	public List<TempVO> listTemp(TempVO listVO);
	
	//교수님 코드
	//임시데이터 목록 가져오기
	public List<EgovMap> selectTempList(TempVO vo) throws Exception;
	
	//임시데이터 목록 수
	public int selectTempListCnt(TempVO vo) throws Exception;
	
	//임시데이터 수정하기
	public void updateTemp(TempVO vo) throws Exception;
	
	//임시데이터 삭제하기
	public void deleteTemp(TempVO vo) throws Exception; 
}
