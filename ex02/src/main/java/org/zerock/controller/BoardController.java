package org.zerock.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.domain.BoardAttachVO;
import org.zerock.domain.BoardVO;
import org.zerock.domain.Criteria;
import org.zerock.domain.PageDTO;
import org.zerock.service.BoardService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

@Controller
@Log4j
@RequestMapping("/board/*")
@AllArgsConstructor
public class BoardController {

	private BoardService service;
	
//	@GetMapping("/list")
//	public void list(Model model) {
//		log.info("list");
//		
//		model.addAttribute("list",service.getList());
//	}
	
	@GetMapping("/list")
	public void list(Criteria cri ,Model model) {
		
		log.info("list: "+cri);
		model.addAttribute("list",service.getList(cri));
//		model.addAttribute("pageMaker", new PageDTO(cri, 123));
		
		int total = service.getTotal(cri);
		
		log.info("total: "+ total );
		
		model.addAttribute("pageMaker",new PageDTO(cri, total));
		
	}
	
	@GetMapping("/register")
	public void register() {
		
	}
	
	@PostMapping("/register")
	public String register(BoardVO board, RedirectAttributes rttr) {
		
		log.info("=================================");
		
		log.info("register: "+board);
		
		if(board.getAttachList() != null) {
			
			board.getAttachList().forEach(attach -> log.info(attach));
			
		}
		
		log.info("==================================");
		
		
		service.register(board);
		
		rttr.addFlashAttribute("result",board.getBno());
			
		return "redirect:/board/list";
		
	}
	
	@GetMapping({"/get","/modify"})
	public void get(@RequestParam("bno")Long bno,@ModelAttribute("cri")Criteria cri, Model model) {	//@ModelAttribute는 자동으로 Model에 데이터를 지정한 이름으로 담아줌
		log.info("/get org modify");
		
		model.addAttribute("board",service.get(bno));
	}
	
	@PostMapping("/modify")
	public String modify(BoardVO board,@ModelAttribute("cri")Criteria cri, RedirectAttributes rttr) {
		log.info("modify"+board);
		
		if(service.modify(board)) {
			rttr.addFlashAttribute("result","success");
		}
		
		
		
		return "redirect:/board/list"+cri.getListLink();
	}
	
	@PostMapping("/remove")
	public String remove(@RequestParam("bno")Long bno,@ModelAttribute("cri")Criteria cri, RedirectAttributes rttr) {
		
		log.info("remove..."+bno);
		
		List<BoardAttachVO> attachList = service.getAttachList(bno);
		
		if(service.remove(bno)) {
			
			//delete Attach Files 
			deleteFiles(attachList);
			
			rttr.addFlashAttribute("result","success");
		}
		
		return "redirect:/board/list"+cri.getListLink();
	}
	
	@GetMapping(value = "/getAttachList", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody	//@RestController로 작성되지 않았기 때문에 직접 @ResponseBody를 적용해서 JSON 데이터를 반환하도록 처리
	public ResponseEntity<List<BoardAttachVO>> getAttachList(Long bno){
		
		log.info("getAttachList"+bno);
		
		return new ResponseEntity<>(service.getAttachList(bno),HttpStatus.OK);
	}

	private void deleteFiles(List<BoardAttachVO> attachList) {
		
		if(attachList == null || attachList.size() == 0) {
			return;
		}
		
		log.info("delete attach files..............");
		log.info(attachList);
		
		attachList.forEach(attach -> {
			try {
				Path file = Paths.get("C:\\upload\\"+attach.getUploadPath()+"\\"+attach.getUuid()+"_"+attach.getFileName());
				
				Files.deleteIfExists(file);
				
				if(Files.probeContentType(file).startsWith("image")) {	//이미지 파일이면
					
					Path thumbNail = Paths.get("C:\\upload\\"+attach.getUploadPath()+"\\s_"+attach.getUuid()+"_"+attach.getFileName());	//섬네일 파일도 제거
					
					Files.delete(thumbNail);
				}
			} catch (Exception e) {
				// TODO: handle exception
				log.error("delete file error"+e.getMessage());
			}//end catch
		});
		
	}
}
