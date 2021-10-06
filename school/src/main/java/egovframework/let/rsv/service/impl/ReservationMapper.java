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
package egovframework.let.rsv.service.impl;

import java.util.List;

import egovframework.let.rsv.service.ReservationVO;
import egovframework.rte.psl.dataaccess.mapper.Mapper;
import egovframework.rte.psl.dataaccess.util.EgovMap;


@Mapper("reservationMapper")
public interface ReservationMapper {
	//예약 목록 가져오기
	List<EgovMap> selectReservationList(ReservationVO vo) throws Exception;
	
	//예약 목록 수 
	int selectReservationListCnt(ReservationVO vo) throws Exception;
	
	//예약 상세정보
	ReservationVO selectReservation(ReservationVO vo) throws Exception;
	
	//예약 등록하기
	void insertReservation(ReservationVO vo) throws Exception;
	
	//예약 수정하기
	void updateReservation(ReservationVO vo) throws Exception;
	
	//예약 삭제하기
	void deleteReservation(ReservationVO vo) throws Exception;

}
