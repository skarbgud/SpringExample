package org.zerock.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.domain.AttachFileDTO;

import lombok.extern.log4j.Log4j;
import net.coobird.thumbnailator.Thumbnailator;

@Controller
@Log4j
public class UploadController {
	
	@GetMapping("/uploadForm")
	public void uploadForm() {
		
		log.info("upload form");
	}
	
	@PostMapping("/uploadFormAction")
	public void uploadFormPost(MultipartFile[] uploadFile, Model model) {
		//파일 처리는 스프링에서 제공하는 MultipartFile이라는 타입을 이용한다. 화면에서 여러 개 선택할 수 있으므로 배열 타입으로 설정한 후 파일을 업로드
		
		String uploadFolder = "C:\\upload";
		
		for (MultipartFile multipartFile: uploadFile ) {
			
			log.info("----------------------------------");
			log.info("Upload File Name: "+multipartFile.getOriginalFilename());
			log.info("Upload File Size: "+multipartFile.getSize());
			
			File saveFile = new File(uploadFolder,multipartFile.getOriginalFilename());
			
			try {
				multipartFile.transferTo(saveFile);
			} catch (Exception e) {
				// TODO: handle exception
				log.error(e.getMessage());
			}//end catch
			
		}//end for
		
		//MultipartFile의 메소드
		//String getName() - 파라미터의 이름 <input>태그의 이름
		//String getOriginalFileName() - 업로드되는 파일의 이름
		//boolean isEmpty() - 파일이 존재하지 않는 경우  true
		//long getSize() - 업로드 되는 파일의 크기
		//byte[] getBytes() - byte[]로 파일 데이터 봔화
		//InputStream getInputStream() - 파일데이터와 연결된 InputStream을 반환
		//transferTo(File file) - 파일의 저장
	}
	
	
	@GetMapping("/uploadAjax")
	public void uploadAjax() {
		
		log.info("upload ajax");
	}
	
	//getFolder()는 오늘 날짜의 경로를 문자열로 생성한다. 생성된 경로는 폴더 경로로 수정된 뒤에 반환
	private String getFolder() {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		Date date = new Date();
		
		String str = sdf.format(date);
		
		return str.replace("-", File.separator);
	}
	
	//특정한 파일이 이미지 타입인지를 검사하는 별도의 checkImageType() 메서드를 추가한다.
	private boolean checkImageType(File file) {
		 
		try {
			String contentType = Files.probeContentType(file.toPath());
			
			return contentType.startsWith("image");
			
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return false;
	}
	
	//AttachDTO의 리스트를 반환하는 구조로 변경해야 한다.
	@PreAuthorize("isAuthenticated()")
	@PostMapping(value = "/uploadAjaxAction", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public ResponseEntity<List<AttachFileDTO>> uploadAjaxPost(MultipartFile[] uploadFile) {	//uploadAjaxPost()는 기존과 달리 ResponseEntity<List<AttachFileDTO>>를 반환하는 형태로 수정
		
		List<AttachFileDTO> list = new ArrayList<>();
		String uploadFolder = "C:\\upload";
		
		String uploadFolderPath = getFolder();
		// make folder.........
		File uploadPath = new File(uploadFolder, uploadFolderPath);
				
		//해당 경로가 있는지 검사하고,폴더를 생성
		if(uploadPath.exists() == false) {
			uploadPath.mkdirs();
		}
		//make yyyy/MM/dd folder
		
		for (MultipartFile multipartFile : uploadFile) {
			
			AttachFileDTO attachDTO = new AttachFileDTO();
			
			String uploadFileName = multipartFile.getOriginalFilename();
			
			//IE has file path
			uploadFileName = uploadFileName.substring(uploadFileName.lastIndexOf("\\")+1);
			
			log.info("only file name: "+uploadFileName);
			
			attachDTO.setFileName(uploadFileName);
			
			UUID uuid = UUID.randomUUID();
			
			uploadFileName = uuid.toString() + "_" + uploadFileName;
			
			
			try {
				File saveFile = new File(uploadPath,uploadFileName);
				multipartFile.transferTo(saveFile);
				
				attachDTO.setUuid(uuid.toString());
				attachDTO.setUploadPath(uploadFolderPath);
				
				//check image type file
				if(checkImageType(saveFile)) {	//이미지 파일이라면 
				
					attachDTO.setImage(true);
					
					FileOutputStream thumbnail = new FileOutputStream(new File(uploadPath,"s_"+uploadFileName));	//Thumbnailator는 InputStream과 java.io.File객체를 이용해서 파일을 생성할 수 있고, 																													//
					
					Thumbnailator.createThumbnail(multipartFile.getInputStream(), thumbnail, 100, 100);		//뒤에 사이즈에 대한 부분을 파라미터로 width와 height를 지정할 수 있다.
					
					thumbnail.close();
				}
				
				//add to List
				list.add(attachDTO);
				
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			} //end catch
			
		}//end for
		
		return new ResponseEntity<>(list, HttpStatus.OK);	//JSON 데이터를 반환하도록 변경된다.
	}
	
	@GetMapping("/display")
	@ResponseBody
	public ResponseEntity<byte[]> getFile(String fileName){	//getFile()은 문자열로 파일의 경로가 포함된 fileName을 파라미터로 받고
			///byte[]를 전송한다. byte[]로 이미지 파일의 데이터를 전송할 때 신경 쓰이는 것은 브라우저에 보내주는 MIME타입이 파일의 종류에 따라 달라지는 점이다.
			//이부분을 해결하기 위해서 probeContentType()을 이용해서 적절한 MIME 타입 데이터를 Http의 헤더 메시지에 포함할 수 있도록 처리한다.
		log.info("fileName: "+fileName);
		
		File file = new File("c:\\upload\\"+fileName);
		
		log.info("file: "+file);
		
		ResponseEntity<byte[]> result = null;
		
		try {
			HttpHeaders header = new HttpHeaders();
			
			header.add("Content-Type", Files.probeContentType(file.toPath()));
			result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file),header,HttpStatus.OK);
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return result;
	}
	
	@GetMapping(value = "/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)	//MIME타입은 다운로드를 할 수 있는 'application/octet-stream'으로 지정
	@ResponseBody
	public ResponseEntity<Resource> downloadFile(@RequestHeader("User-Agent")String userAgent,String fileName){
		
		log.info("download file: "+fileName);
		
		Resource resource = new FileSystemResource("c:\\upload\\"+fileName);
		
		
		if(resource.exists() == false) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		String resourceName = resource.getFilename();
		
		//remove UUID
		String resourceOriginalName = resourceName.substring(resourceName.indexOf("_")+1);
		
		HttpHeaders headers = new HttpHeaders();
		
		//저장되는 이름은 'Content-Disposition'을 이용해서 지정한다. 파일 이름에 대한 문자열 처리는 파일 이름이 한글인 경우 저장할 때 깨지는 문제를 막기 위해서이다.
		try {
			
			String downloadName = null;
			
			if(userAgent.contains("Trident")) {
				
				log.info("IE browser");
				
				downloadName = URLEncoder.encode(resourceOriginalName,"UTF-8").replace("\\+", "");
			}else if(userAgent.contains("Edge")) {
				
				log.info("Edge browser");
				
				downloadName = URLEncoder.encode(resourceOriginalName,"UTF-8");
				
				log.info("Edge name: "+downloadName);
				
			}else {
				
				log.info("Chrome browser");
				downloadName = new String(resourceOriginalName.getBytes("UTF-8"),"ISO-8859-1");
			}
			
			
			headers.add("Content-Disposition", "attachment; fileName="+downloadName);
			
		} catch (UnsupportedEncodingException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return new ResponseEntity<Resource>(resource, headers, HttpStatus.OK);
	}
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/deleteFile")
	@ResponseBody
	public ResponseEntity<String> deleteFile(String fileName, String type){	
		//delteFile()은 브라우저에서 전송하는 파일 이름과 종류를 파라미터로 받아서 파일의 종류에 따라 다르게 동작.
		//브라우저에서 전송되는 파일 이름은 '경로+UUID+_+파일이름'으로 구성되어 있으므로, 일반 파일의 경우에는 파일만 삭제
		
		log.info("deleteFile: "+fileName);
		
		
		File file;
		
		try {
			file = new File("c:\\upload\\"+URLDecoder.decode(fileName,"UTF-8"));
			
			file.delete();	
		
			//이미지의 경우 섬네일이 존재하므로, 파일 이름의 중간에 's_'가 들어 있다. 일반 이미지 파일의 경우 's_'가 없도록 되어 있으므로,
			//이 부분을 변경해서  원본 이미지 파일도 삭제하도록 처리한다.
			if(type.equals("image")) {
				
				String largeFileName = file.getAbsolutePath().replace("s_", "");
				
				log.info("largeFileName: "+largeFileName);
				
				file = new File(largeFileName);
				
				file.delete();
			}
		} catch (UnsupportedEncodingException e) {
			// TODO: handle exception
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<String>("deleted",HttpStatus.OK);
		
	}
}
