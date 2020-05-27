package org.zerock.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.domain.Criteria;
import org.zerock.domain.ReplyPageDTO;
import org.zerock.domain.ReplyVO;
import org.zerock.service.ReplyService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

@RequestMapping("/replies/")
@RestController
@Log4j
@AllArgsConstructor
public class ReplyController {
	
	private ReplyService service;
	
	//REST방식으로 처리할 때 주의해야 하는 점은 브라우저나 외부에서 서버를 호출할 때 데이터의 포맷과 서버에서 보내주는 데이터의 타입을 명확히 설계해야 하는 것이다.
	//예를 들어 댓글 등록의 경우 브라우저에서는 JSOJ타입으로 된 댓글 데이터를 전송하고, 서버에서는 댓글의 처리 결과가 정상적으로 되었는지 문자열로 결과를 알려주도록 한다.
	@PreAuthorize("isAuthenticated()")
	@PostMapping(value = "/new",			//create는 @PostMapping으로 POST방식으로만 동작하도록 설계하고, consumes와 produces를 이용해서 JSON방식의 데이터만 처리하도록 하고, 문자열을 반환하도록 설계
			consumes = "application/json",	
			produces = { MediaType.TEXT_PLAIN_VALUE })
	public ResponseEntity<String> create(@RequestBody ReplyVO vo){	//@RequestBody : JSON 데이터를 원하는 타입의 객체로 변환해야 하는 경우 주로 사용
																	//@RequestBody를 적용해서 JSON 데이터를 ReplyVO타입으로 변환하도록 지정한다.
		log.info("ReplyVO: "+vo);
		
		int insertCount = service.register(vo);
		
		log.info("Reply INSERT COUNT: "+insertCount);
		
		//삼항 연산자 처리
		return insertCount == 1 ? new ResponseEntity<>("success",HttpStatus.OK) : new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	//특정 게시물의 댓글 목록 확인
	@GetMapping(value = "/pages/{bno}/{page}",
			produces = { MediaType.APPLICATION_XML_VALUE,
						 MediaType.APPLICATION_JSON_UTF8_VALUE })
	public ResponseEntity<ReplyPageDTO> getList(
		@PathVariable("page") int page,
		@PathVariable("bno") Long bno){
		
		log.info("getList..........");
		
		Criteria cri = new Criteria(page,10);
		
		log.info("get Reply List Bno: "+bno);
		
		log.info("cri: "+cri);
		
		return new ResponseEntity<>(service.getListPage(cri, bno),HttpStatus.OK);
	}
	
	//조회
	@GetMapping(value = "/{rno}",
			produces = {MediaType.APPLICATION_XML_VALUE,
						MediaType.APPLICATION_JSON_UTF8_VALUE })
	public ResponseEntity<ReplyVO> get(@PathVariable("rno") Long rno){
		
		log.info("get: "+rno);
		
		return new ResponseEntity<>(service.get(rno),HttpStatus.OK);
	}
	
	//삭제
	@PreAuthorize("principal.username == #vo.replyer")
	@DeleteMapping(value = "/{rno}")
	public ResponseEntity<String> remove(@RequestBody ReplyVO vo,
			@PathVariable("rno") Long rno){
		
		log.info("remove: "+ rno);
		
		log.info("replyer: "+vo.getReplyer());
	
		return service.remove(rno) == 1? new ResponseEntity<>("success",HttpStatus.OK) : new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	//수정
	@PreAuthorize("principal.username == #vo.replyer")
	@RequestMapping(method = {RequestMethod.PUT, RequestMethod.PATCH },
			value = "/{rno}",
			consumes = "application/json")
	public ResponseEntity<String> modify(
						@RequestBody ReplyVO vo,
						@PathVariable("rno") Long rno){
		
		
		log.info("rno: "+rno);
		
		log.info("modify: "+vo);
		
		return service.modify(vo) == 1? new ResponseEntity<>("success",HttpStatus.OK) : new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
