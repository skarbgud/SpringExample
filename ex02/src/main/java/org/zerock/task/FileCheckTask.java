package org.zerock.task;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.zerock.domain.BoardAttachVO;
import org.zerock.mapper.BoardAttachMapper;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

//작업순서 1) 데이터베이스에서 어제 사용된 파일의 목록을 얻어오고, 2) 해당 폴더의 파일 목록에서 데이터베이스에 없는 파일을 찾아낸다. 3) 이후 데이터베이스에 없는 파일들을 삭제

@Log4j
@Component
public class FileCheckTask {

/*	@Scheduled(cron = "0 * * * * *")	//cron이라는 속성을 부여해서 주기를 제어한다.'매분 0초가 될 때 마다 실행한다' scro = "(second) (minutes) (hours) (day) (months) (day of week) (year)"
	public void checkFiles() throws Exception{									//*:모든수 , ?:제외 , -:기간, ':특정시간, /:시작 시간과 반복시간, L:마지막, W:가까운 평일
		
		log.warn("File Check Task run.............");	//로그가 정상적으로 기로고디는지 확인하기 위해서 log.warn() 레벨을 이용해서 실행중 확인할 수 있도록 한다.
		
		log.warn("=====================================");
		
		
		
	}
*/
	
	@Setter(onMethod_ = {@Autowired})
	private BoardAttachMapper attachMapper;
	
	private String getFolderYesterDay() {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		Calendar cal = Calendar.getInstance();
		
		cal.add(Calendar.DATE, -1);
		
		String str = sdf.format(cal.getTime());
		
		return str.replace("-", File.separator);
	}
	
	
	
	@Scheduled(cron = "0 0 2 * * *")	
	public void checkFiles() throws Exception{	//checkFiles()는 매일 새벽2시에 동작한다.
		
		log.warn("File Check Task run.................");
		log.warn(new Date());
		// file list in database
		List<BoardAttachVO> fileList = attachMapper.getOldFiles();
		
		// ready for check file in directory with database file list
		List<Path> fileListPaths = fileList.stream()
				.map(vo -> Paths.get("C:\\upload", vo.getUploadPath(), vo.getUuid() + "_" + vo.getFileName()))
				.collect(Collectors.toList());

		// image file has thumnail file
		fileList.stream().filter(vo -> vo.isFileType() == true)
				.map(vo -> Paths.get("C:\\upload", vo.getUploadPath(), "s_" + vo.getUuid() + "_" + vo.getFileName()))
				.forEach(p -> fileListPaths.add(p));

		log.warn("===========================================");

		fileListPaths.forEach(p -> log.warn(p));

		// files in yesterday directory
		File targetDir = Paths.get("C:\\upload", getFolderYesterDay()).toFile();

		File[] removeFiles = targetDir.listFiles(file -> fileListPaths.contains(file.toPath()) == false);

		log.warn("-----------------------------------------");
		for (File file : removeFiles) {

			log.warn(file.getAbsolutePath());

			file.delete();

		}
	}
}
