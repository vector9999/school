package egovframework.let.rsv.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;

import com.ibm.icu.text.SimpleDateFormat;

import egovframework.com.cmm.service.FileVO;
import egovframework.let.rsv.service.ReservationApplyService;
import egovframework.let.rsv.service.ReservationApplyVO;
import egovframework.let.rsv.service.ReservationService;
import egovframework.let.rsv.service.ReservationVO;
import egovframework.let.utl.fcc.service.EgovStringUtil;
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
	
	@Resource(name = "egovReqTempIdGnrService")
	private EgovIdGnrService idgenTempService;
	
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
	//예약자 엑셀 업로드 
	@Override
	public Map<String, Object> excelUpload(FileVO fileVO, ReservationApplyVO vo) throws Exception {
		String fileExt = fileVO.getFileExtsn();
		FileInputStream stream = new FileInputStream(fileVO.getFileStreCours() + "/" + fileVO.getStreFileNm());
		File file = new File(fileVO.getFileStreCours() + "/" + fileVO.getStreFileNm()); //엑셀 파일을 읽음
		
		Boolean result = true;
		Boolean totResult = true;
		String resultMsg ="";
		List<EgovMap> resultList = new ArrayList<EgovMap>();
		List<String> duplList = new ArrayList<String>();
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		//기존 예약자 - 예약자가 있는지 체크
		List<EgovMap> existUserList = reservationApplyMapper.selectReservationApplyList(vo);
		
		//엑셀ID
		String tempId = idgenTempService.getNextStringId();
		vo.setReqsttempId(tempId);
		
		try {
			Workbook wb = null;
			if("XLS".equals(fileExt.toUpperCase())) {
				wb = WorkbookFactory.create(stream);
			} else if("XLSX".equals(fileExt.toUpperCase())) {
				wb = WorkbookFactory.create(stream);
			}
			FormulaEvaluator eval = wb.getCreationHelper().createFormulaEvaluator();
			
			//int sheetNum = wb.getNumberOfSheets()//시트 갯수 가져오기
			if(wb != null) {
				Sheet sheet = wb.getSheetAt(0);
				int rows = sheet.getPhysicalNumberOfRows(); //행 갯수 가져오기 - for문을 돌기 위함
				for(int r = 1; r < rows; r++) { //row 루프 - 행 갯수 만큼 돌게됨
					Row row = sheet.getRow(r);
					if(row!= null ) {
					int cells = row.getPhysicalNumberOfCells();
					for(int c = 0; c < 4; c++) { //cell 가져오기 - 총 열의 갯수, 자바에서는 데이터를 행으로 읽기 때문에 몇번열까지 데이터가 있는지 java에 알려주어야함. 만약 다른 프로젝트에서 사용하게 된다면 4라는 숫자 대신 변수로 선언해서 사용할 수도 있을 것.
						Cell cell = row.getCell(c);
						result = true;
						if(cell != null) {
							String value = "";
							switch (cell.getCellType()) {
							case Cell.CELL_TYPE_FORMULA:
								if(!EgovStringUtil.isEmpty(cell.toString())) {
									switch (eval.evaluateFormulaCell(cell)) { //엑셀이 어떤 데이터 가지고 있는지 검증(고정된 코드)
									case Cell.CELL_TYPE_NUMERIC:
										if(HSSFDateUtil.isCellDateFormatted(cell)) {
											SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
											value = formatter.format(cell.getDateCellValue());
										} else {
											value = "" + (long)cell.getNumericCellValue();
										}
										break;

									case Cell.CELL_TYPE_STRING:
										value = "" + cell.getRichStringCellValue();
										break;
									case Cell.CELL_TYPE_BLANK:
										value = "";
										break;
									case Cell.CELL_TYPE_ERROR:
										value = "" + cell.getErrorCellValue();
										break;
									case Cell.CELL_TYPE_BOOLEAN:
										value = "" + cell.getBooleanCellValue();
										break;
									default:
										break;
									
									}
								}
								break;
							case Cell.CELL_TYPE_NUMERIC:
								if(HSSFDateUtil.isCellDateFormatted(cell)) {
									SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
									value = formatter.format(cell.getDateCellValue());
								} else {
									value = "" + (long)cell.getNumericCellValue();
								}
								break;
							case Cell.CELL_TYPE_STRING:
								value = "" + cell.getRichStringCellValue();
								break;
							case Cell.CELL_TYPE_BLANK:
								value = "";
								break;
							case Cell.CELL_TYPE_ERROR:
								value = "" + cell.getErrorCellValue();
								break;
							case Cell.CELL_TYPE_BOOLEAN:
								value = "" + cell.getBooleanCellValue();
							default:
								break;
							}
							
							if(!EgovStringUtil.isEmpty(value)) {
								value = value.trim();
							}
							
							switch (c) { //for문이 돌면서 실제로 파일을 입력
							case 0:
								vo.setChargerNm(value); //신청자명
								break;
							case 1:
								vo.setUserId(value); //신청자ID
								break;
							case 2:
								vo.setTelno(value); //연락처
								break;
							case 3:
								vo.setEmail(value); //이메일
								break;
							default:
								break;
							}
						}
					}
					
				//빈 행은 제외
				if(!EgovStringUtil.isEmpty(vo.getUserId())) {
					List<String> existIdList = new ArrayList<>();
					//기존유저 중복체크
					if(existUserList != null) {
						for(EgovMap cu : existUserList) {
							existIdList.add(cu.get("frstRegisterId").toString());
						}
						
						if(existIdList.contains(vo.getUserId())) {
							EgovMap userMap = new EgovMap();
							userMap.put("userId", vo.getUserId());
							userMap.put("message", "이미 등록된 ID입니다.");
							resultList.add(userMap);
							
							result = false;
							totResult = false;
						}
					}
					
					//엑셀 중복 체크
					if(result && duplList.contains(vo.getUserId())) {
						EgovMap userMap = new EgovMap();
						userMap.put("userId", vo.getUserId());
						userMap.put("message", "엑셀이 중복으로 입력되었습니다.");
						resultList.add(userMap);
						
						result = false;
						totResult = false;
					}
					
					if(result && !EgovStringUtil.isEmpty(vo.getUserId())) {
						String id = idgenService.getNextStringId();
						vo.setReqstId(id);
						reservationApplyMapper.insertReservationApplyTemp(vo);
						duplList.add(vo.getUserId());
					}
				}
			}
		}
			
			List<EgovMap> tempList = reservationApplyMapper.selectReservationApplyTemp(vo); //임시예약자 목록 가져오기
			if(tempList.size() > 0) {
				//일괄등록
				reservationApplyMapper.insertReservationApplyTempAll(vo); //임시예약자를 예약자로 일괄 등록하기
			}
		}
		
	} catch(FileNotFoundException e) {
		result = false;
		resultMsg = "문제가 발생하여 완료하지 못하였습니다.";
		e.printStackTrace();
	} catch(InvalidFormatException e) {
		result = false;
		resultMsg = "문제가 발생하여 완료하지 못하였습니다.";
		e.printStackTrace();
	} catch(Exception e) {
		result = false;
		resultMsg = "문제가 발생하여 완료하지 못하였습니다.";
		e.printStackTrace();
	} finally {
		//임시데이터 삭제
		reservationApplyMapper.deleteReservationApplyTemp(vo);
		file.delete();
	}
	
	map.put("success", totResult);
	map.put("msg", resultMsg);
	map.put("resultList", resultList);
	
	return map;
	}
}
