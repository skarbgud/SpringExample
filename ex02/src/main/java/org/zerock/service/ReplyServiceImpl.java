package org.zerock.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.domain.Criteria;
import org.zerock.domain.ReplyPageDTO;
import org.zerock.domain.ReplyVO;
import org.zerock.mapper.BoardMapper;
import org.zerock.mapper.ReplyMapper;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class ReplyServiceImpl implements ReplyService {

	@Setter(onMethod_ = @Autowired)
	private ReplyMapper mapper;
	
	@Setter(onMethod_ = @Autowired)
	private BoardMapper boardMapper;
	
	//스프링 4.3을 이용하는 경우 생성자와 자동주입을 이용해서 아래와 같이 처리할 수 있다.
//	private ReplyMapper mapper;
	
	@Transactional
	@Override
	public int register(ReplyVO vo) {
		// TODO Auto-generated method stub
		
		log.info("register...."+vo);
		
		//등록의 경우 파라미터로 전달받은 ReplyVO 내에 게시물의 번호가 존재하므로 이를 이용해서 댓글을 추가한다.
		boardMapper.updateReplyCnt(vo.getBno(), 1);
		
		return mapper.insert(vo);
	}

	@Override
	public ReplyVO get(Long rno) {
		// TODO Auto-generated method stub
		
		log.info("get...."+rno);
		
		return mapper.read(rno);
	}

	@Override
	public int modify(ReplyVO vo) {
		// TODO Auto-generated method stub
		
		log.info("modify...."+vo);
		
		return mapper.update(vo);
	}

	@Transactional
	@Override
	public int remove(Long rno) {
		// TODO Auto-generated method stub
		
		log.info("remove...."+rno);
		
		//삭제의 경우 rno만 받기 때문에 해당 댓글의 게시물을 알아내는 과정이 필요하다.
		ReplyVO vo = mapper.read(rno);
		
		boardMapper.updateReplyCnt(vo.getBno(), -1);
		
		return mapper.delete(rno);
	}

	@Override
	public ReplyPageDTO getListPage(Criteria cri, Long bno) {
		// TODO Auto-generated method stub
		
		log.info("get Reply List of a Board "+bno);
		
		return new ReplyPageDTO(
				mapper.getCountByBno(bno),
				mapper.getListWithPaging(cri, bno));
		
		
	}

}
