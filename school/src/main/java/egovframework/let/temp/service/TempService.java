package egovframework.let.temp.service;

import java.util.List;

public interface TempService {
	
	//임시데이터 가져오기
	public TempVO selectTemp(TempVO vo) throws Exception;
	
	
	//임시데이터 등록하기
	public String insertTemp(TempVO vo) throws Exception;
	
	//임시데이터 목록 가져오기
	public String listTemp(TempVO vo) throws Exception;

}
