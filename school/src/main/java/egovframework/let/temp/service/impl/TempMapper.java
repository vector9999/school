/*
 * Copyright 2011 MOPAS(Ministry of Public Administration and Security).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package egovframework.let.temp.service.impl;

import java.util.List;

import egovframework.let.temp.service.TempVO;
import egovframework.rte.psl.dataaccess.mapper.Mapper;


@Mapper("tempMapper")
public interface TempMapper {

	//임시데이터 가져오기
	TempVO selectTemp(TempVO vo) throws Exception;
	
	//임시데이터  등록
	void insertTemp(TempVO vo) throws Exception;
	
	//임시데이터 목록 가져오기
	String listTemp(TempVO vo) throws Exception;
	//임시데이터 목록 가져오기 끝
		
		
}
