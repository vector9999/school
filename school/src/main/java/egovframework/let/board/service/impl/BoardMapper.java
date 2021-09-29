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
package egovframework.let.board.service.impl;

import java.util.List;

import egovframework.let.board.service.BoardVO;
import egovframework.rte.psl.dataaccess.mapper.Mapper;
import egovframework.rte.psl.dataaccess.util.EgovMap;


@Mapper("boardMapper")
public interface BoardMapper {
	//게시물 목록 가져오기
	List<EgovMap> selectBoardList(BoardVO vo) throws Exception;
	
	//게시물 목록 수 
	int selectBoardListCnt(BoardVO vo) throws Exception;
	
	//게시물 상세정보
	BoardVO selectBoard(BoardVO vo) throws Exception;

	//조회수 업
	void updateViewCnt(BoardVO vo) throws Exception;
	
	//게시글 등록
	void insertBoard(BoardVO vo) throws Exception;
	
	//게시글 수정하기
	void updateBoard(BoardVO vo) throws Exception;
	
	//게시글 삭제하기
	void deleteBoard(BoardVO vo) throws Exception;

}
