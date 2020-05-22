package org.zerock.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.domain.BoardAttachVO;
import org.zerock.domain.BoardVO;
import org.zerock.domain.Criteria;
import org.zerock.mapper.BoardAttachMapper;
import org.zerock.mapper.BoardMapper;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;


@Log4j
@Service	//계층구조상 주로 비즈니스 영역을 담당하는 객체임을 표시하기 위해 사용한다.
@AllArgsConstructor 	//스프링 4.3 부터는 단일 파라미터를 받는 생성자의 경우에는 필요한 파라미터를 자동으로 주입할 수 있다. @AllArgsConstructor는 모든 파라미터를 이용하는
//생성자를 만들기 때문에 실제 코드는 BoardMapper를 주입받는 생성자가 만들어지게 된다.
public class BoardServiceImpl implements BoardService {

	//spring 4.3 이상에서 자동 처리
	private BoardMapper mapper;
	private BoardAttachMapper attachMapper;
	
	
//	@Setter(onMethod_ = @Autowired)
//	private BoardMapper mapper;
	
	@Transactional
	@Override
	public void register(BoardVO board) {
		// TODO Auto-generated method stub
		log.info("register..."+board);
		
		mapper.insertSelectKey(board);
		
		if(board.getAttachList() == null || board.getAttachList().size() <= 0) {
			return;
		}
		
		board.getAttachList().forEach(attach -> {
			
			attach.setBno(board.getBno());
			attachMapper.insert(attach);
			
		});
	}

	@Override
	public BoardVO get(Long bno) {
		// TODO Auto-generated method stub
		log.info("get...."+bno);
		
		return mapper.read(bno);
	}

	@Transactional
	@Override
	public boolean modify(BoardVO board) {
		// TODO Auto-generated method stub
		
		log.info("modify...."+board);
		
		attachMapper.deleteAll(board.getBno());
		
		boolean modifyResult = mapper.update(board) == 1;
		
		if(modifyResult &&  board.getAttachList() != null && board.getAttachList().size() > 0) {
			
			board.getAttachList().forEach(attach ->{
				
				attach.setBno(board.getBno());
				attachMapper.insert(attach);
			});
		}
		
		return modifyResult;
	}

	@Transactional
	@Override
	public boolean remove(Long bno) {
		// TODO Auto-generated method stub
		log.info("remove...."+bno);
		
		attachMapper.deleteAll(bno);
		
		return mapper.delete(bno) == 1;
	}

//	@Override
//	public List<BoardVO> getList() {
//		// TODO Auto-generated method stub
//		
//		log.info("getList....");
//		return mapper.getList();
//	}

	
	@Override
	public List<BoardVO> getList(Criteria cri) {
		// TODO Auto-generated method stub
		
		log.info("get List with criteria: "+cri);
		
		return mapper.getListWithPaging(cri);
	}
	
	@Override
	public List<BoardAttachVO> getAttachList(Long bno) {
		// TODO Auto-generated method stub
		
		log.info("get Attach List by bno"+bno);
		
		return attachMapper.findByBno(bno);
	}

	@Override
	public int getTotal(Criteria cri) {
		// TODO Auto-generated method stub
		
		log.info("get total count");
		return mapper.getTotalCount(cri);
	}

}
