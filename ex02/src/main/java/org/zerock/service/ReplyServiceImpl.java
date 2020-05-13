package org.zerock.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zerock.domain.Criteria;
import org.zerock.domain.ReplyPageDTO;
import org.zerock.domain.ReplyVO;
import org.zerock.mapper.ReplyMapper;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class ReplyServiceImpl implements ReplyService {

	@Setter(onMethod_ = @Autowired)
	private ReplyMapper mapper;
	
	//스프링 4.3을 이용하는 경우 생성자와 자동주입을 이용해서 아래와 같이 처리할 수 있다.
//	private ReplyMapper mapper;
	
	@Override
	public int register(ReplyVO vo) {
		// TODO Auto-generated method stub
		
		log.info("register...."+vo);
		
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

	@Override
	public int remove(Long rno) {
		// TODO Auto-generated method stub
		
		log.info("remove...."+rno);
		
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
