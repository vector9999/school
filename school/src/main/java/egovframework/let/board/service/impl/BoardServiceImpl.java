package egovframework.let.board.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.let.board.service.BoardService;
import egovframework.let.board.service.BoardVO;
import egovframework.let.temp.service.TempService;
import egovframework.let.temp.service.TempVO;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import egovframework.rte.fdl.idgnr.EgovIdGnrService;
import egovframework.rte.fdl.property.EgovPropertyService;
import egovframework.rte.psl.dataaccess.util.EgovMap;
@Service("boardService")
public class BoardServiceImpl extends EgovAbstractServiceImpl implements BoardService {

	@Resource(name = "boardMapper")
	private BoardMapper boardMapper;
	
	
	@Resource(name = "egovBoardIdGnrService")
	private EgovIdGnrService idgenService;

	//게시물 목록 가져오기
	@Override
	public List<EgovMap> selectBoardList(BoardVO vo) throws Exception {
		return boardMapper.selectBoardList(vo);
	}
	
	//게시물 목록 수 
	@Override
	public int selectBoardListCnt(BoardVO vo) throws Exception {
		return boardMapper.selectBoardListCnt(vo);
	}

	//게시물 상세페이지
	@Override
	public BoardVO selectBoard(BoardVO vo) throws Exception {
		//상세페이지 조회할때 마다 조회수 up
		boardMapper.updateViewCnt(vo);
		return boardMapper.selectBoard(vo);
	}

	//게시글 등록
	@Override
	public String insertBoard(BoardVO vo) throws Exception {
		String id = idgenService.getNextStringId();
		vo.setBoardId(id);
		boardMapper.insertBoard(vo);
		return id;
	}

	//게시글 수정하기
	@Override
	public void updateBoard(BoardVO vo) throws Exception {
		boardMapper.updateBoard(vo);
	}

	//게시글 삭제하기
	@Override
	public void deleteBoard(BoardVO vo) throws Exception {
		boardMapper.deleteBoard(vo);
	}
	
	
}
