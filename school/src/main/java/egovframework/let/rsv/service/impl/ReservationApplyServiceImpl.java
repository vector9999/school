package egovframework.let.rsv.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.let.rsv.service.ReservationApplyService;
import egovframework.let.rsv.service.ReservationApplyVO;
import egovframework.let.rsv.service.ReservationService;
import egovframework.let.rsv.service.ReservationVO;
import egovframework.rte.fdl.idgnr.EgovIdGnrService;
import egovframework.rte.fdl.property.EgovPropertyService;
import egovframework.rte.psl.dataaccess.util.EgovMap;

@Service("reservationServiceApply")
public class ReservationApplyServiceImpl implements ReservationApplyService {

	@Resource(name = "propertiesService")
	protected EgovPropertyService propertyService;
	
	@Resource(name = "reservationApplyMapper")
	private ReservationApplyMapper reservationApplyMapper;
	
	@Resource(name = "egovReqIdGnrService")
	private EgovIdGnrService idgenService;
	
	@Resource(name = "reservationService")
	private ReservationService reservationService;

	//예약자 등록하기 - 중요
	@Override
	public ReservationApplyVO insertReservationApply(ReservationApplyVO vo) throws Exception {
		//신청인원체크
		ReservationVO reservationVO = new ReservationVO();
		reservationVO.setResveId(vo.getResveId());
		ReservationVO result = reservationService.selectReservation(reservationVO);
		if(result.getMaxAplyCnt() <= result.getApplyFCnt()) { //fcnt: 현재 최종 승인난 인원이 몇명인지 
			vo.setErrorCode("ERROR-R1");
			vo.setMessage("마감되었습니다.");
		} else {
			//기존 신청여부
			if(reservationApplyMapper.duplicateApplyCheck(vo) > 0) { //단, 이 사람이 먼저 프로그램을 신청했는지 체크 중복체크를 위해 작성
				vo.setErrorCode("ERROR-R2");
				vo.setMessage("이미 해당 프로그램이 예약이 되어져 있습니다."); //이미 예약 되어져 있다면 튕기게, 
			} else {
			String id = idgenService.getNextStringId();
			vo.setReqstId(id);
			reservationApplyMapper.insertReservationApply(vo); //예약 되어져 있지 않을 경우 예약이 되게끔.
			}
		}
		return vo;
	}

	//예약자 목록 가져오기
	@Override
	public List<EgovMap> selectReservationApplyList(ReservationApplyVO vo) throws Exception {
		return reservationApplyMapper.selectReservationApplyList(vo);
	}

	//예약자 목록 수 
	@Override
	public int selectReservationApplyListCnt(ReservationApplyVO vo) throws Exception {
		return reservationApplyMapper.selectReservationApplyListCnt(vo);
	}

	//예약자 상세정보
	@Override
	public ReservationApplyVO selectReservationApply(ReservationApplyVO vo) throws Exception {
		return reservationApplyMapper.selectReservationApply(vo);
	}

	//예약자 수정하기
	@Override
	public void updateReservationApply(ReservationApplyVO vo) throws Exception {
		reservationApplyMapper.updateReservationApply(vo);
	}
	
	//예약자 삭제하기
	@Override
	public void deleteReservationApply(ReservationApplyVO vo) throws Exception {
		reservationApplyMapper.deleteReservationApply(vo);
	}
	
	//예약자 승인처리
	@Override
	public void updateReservationConfirm(ReservationApplyVO vo) throws Exception {
		reservationApplyMapper.updateReservationConfirm(vo);
	}
	
	//예약 가능여부 확인 - 등록과 동일한데, ajax사용해서 땡겨올때 사용할 것. 신청버튼을 눌렀을때 ajax를 이용해서 이걸 먼저 체크, 체크 후에 신청할수 있는 사람이라면 신청페이지로 넘어가게 처리해주는 것임.
	@Override
	public ReservationApplyVO rsvCheck(ReservationApplyVO vo) throws Exception {
		//신청인원체크
		ReservationVO reservationVO = new ReservationVO();
		reservationVO.setResveId(vo.getResveId());
		ReservationVO result = reservationService.selectReservation(reservationVO);
		if(result.getMaxAplyCnt() <= result.getApplyFCnt()) {
			vo.setErrorCode("ERROR-R1");
			vo.setMessage("마감되었습니다.");
		} else if(reservationApplyMapper.duplicateApplyCheck(vo) > 0) {
				vo.setErrorCode("ERROR-R2");
				vo.setMessage("이미 해당 프로그램이 예약이 되어져 있습니다.");
			}
		return vo;
	}
	


}
