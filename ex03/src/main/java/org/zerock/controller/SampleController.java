package org.zerock.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.domain.SampleVO;
import org.zerock.domain.Ticket;

import lombok.extern.log4j.Log4j;

@RestController
@RequestMapping("/sample")
@Log4j

public class SampleController {
	//단순 문자열 반환
	@GetMapping(value = "/getText", produces = "text/plain; charset=UTF-8")
	public String getText() {
		
		log.info("MIME TYPE: "+MediaType.TEXT_PLAIN_VALUE);
		
		return "안녕하세요";
	}
	
	//기존의 @Controller는 문자열을 반환하는 경우에는 JSP 파일의 이름으로 처리되지만,
	//@RestController의 경우에는 순수한 데이터가 된다. @GetMapping에 사용된 produces 속성은 해당 메서드가 생성하는 MIME 타입을 의미
	
	
	
	
	@GetMapping(value = "/getSample",
			produces = {MediaType.APPLICATION_JSON_UTF8_VALUE,	//스프링 5.2버전 부터는 Deprecated되고 APPLICATION_JSON_VALUE로 사용
						MediaType.APPLICATION_XML_VALUE})
	public SampleVO getSample() {
		
		return new SampleVO(112,"스타","로드");
		
	}
	
	//getSample()은 XML과 JSON방식의 데이터를 생성할 수 있도록 작성되었는데, 브라우저에서 '/sample/getSample'을 호출하면
	//	<SampleVO>
	//	<mno>112</mno>
	//	<firstName>스타</firstName>
	//	<lastName>로드</lastName>
	//	</SampleVO>
	// XML형태로 출력됨
	
	
	
	
	//@GetMapping이나 @RequestMapping의 produces 속성은 반드시 지정해야 하는 것은 아니므로 생략가능
	@GetMapping(value = "/getSample2")
	public SampleVO getSample2() {
		return new SampleVO(113, "로켓", "라쿤");
	}
	
	
	//리스트 타입의 객체 반환
	@GetMapping(value = "/getList")
	public List<SampleVO> getList(){
		
		//내부적으로 1부터 10미만까지의 루프를 처리하면서 SampleVO 객체를 만들어서 List<SampleVO>로 만들어 낸다.
		return IntStream.range(1, 10).mapToObj(i -> new SampleVO(i, i + "First", i+"Last")).collect(Collectors.toList());
		
	}
	
	//맵의 경우에는 '키'와 '값'을 가지는 하나의 객체로 간주
	@GetMapping(value = "/getMap")
	public Map<String, SampleVO> getMap(){
		
		Map<String, SampleVO> map = new HashMap<String, SampleVO>();
		map.put("first", new SampleVO(111,"크루트","주니어"));
		
		return map;
		
//		<Map>
//		<first>
//		<mno>111</mno>
//		<firstName>크루트</firstName>
//		<lastName>주니어</lastName>
//		</first>
//		</Map>
		
//		{"first":{"mno":111,"firstName":"크루트","lastName":"주니어"}}
		
	}
	
	
	//ResponseEntity타입
	//REST 방식으로 호출하는 경우는 화면 자체가 아니라 데이터 자체를 전송하는 방식으로 처리되기 때문에 데이터를 요청한 쪽에서는 정상적인 데이터인지 비정상적인 데이터인지를 구분할 수 있는 방법을 제공해야만 한다.
	//ResponseEntity는 데이터와 함께 HTTP헤더의 상태 메세지 등을 같이 전달하는 용도로 사용된다.
	//HTTP의 상태 코드와 에러 메세지 등을 함께 데이터를 전달할 수 있기 때문에 받는 입장에서는 확실하게 결과를 알 수 있다.
	@GetMapping(value = "/check", params = {"height","weight"})
	public ResponseEntity<SampleVO> check(Double height, Double weight){
		
		SampleVO vo = new SampleVO(0, ""+height, ""+weight);
		
		ResponseEntity<SampleVO> result = null;
		
		if(height < 150) {
			result = ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(vo);
		}
		else {
			result = ResponseEntity.status(HttpStatus.OK).body(vo);
		}
		
		return result;
	}
	
	
	
//	@RestController는 기존의 @Controller에서 사용하던 일반적인 타입이나 사용자가 정의한 타입(클래스)을 사용한다.
//	여기에 추가로 몇가지 어노테이션을 이용하는 경우가 있다.
//	-@PathVariable : 일반 컨트롤러에서도 사용이 가능하지만 REST 방식에서 자주 사용된다. URL 경로의 일부를 파라미터로 
//	사용할 때 이용
//	-@RequestBody : JSON 데이터를 원하는 타입의 객체로 변환해야 하는 경우 주로 사용
//
//	@PathVariable
//	REST방식에서는 URL 내에 최대한 많은 정보를 담으려고 노력한다. 예전에는 "?' 뒤에 추가되는 쿼리 스트링이라는 형태로
//	파라미터를 이용해서 전달되던 데이터들이 REST방식에서는 경로의 일부로 차용되는 경우가 많다
//
//	스프링 MVC에서는 @PathVariable 어노테이션을 이용해서 URL 상에 경로의 일부를 파라미터로 사용할 수 있다.
//
//	http://localhost:8089/sample/{sno}
//	http://localhost:8089/sample/{sno}/page/{pno}
//
//	위의 URL에서 '{}'로 처리된 부분은 컨트롤러의 메서드에서 변수로 처리가 가능하다
//	@PathVariable은 '{}'의 이름을 처리할 때 사용
//
//	REST 방식에서는 URL자체에 데이터를 식별할 수 있는 정보들을 표현하는 경우가 많으므로 다양한 방식으로 @PathVariable이
//	사용된다.
	@GetMapping("/product/{cat}/{pid}")
	public String[] getPath(
			@PathVariable("cat")String cat,
			@PathVariable("pid")Integer pid) {
		
		return new String[] {"category: "+cat, "productid: "+pid};

		
		//@PathVariable을 적요하고 싶은 경우에는 '{}'를 이용해서 변수명을 지정하고, @PathVariable을 이용해서 지정된 이름의 변숫값을 얻을 수 있다.
		//값을 얻을 때에는 int,double과 같은 기본 자료형은 사용할 수 없다.
		
		//브라우저에서 /sample/product/bags/1234로 호출하면 cat과 pid 변수의 값으로 처리되는 것을 확인할 수 있다.
//		<Strings>
//		<item>category: bags</item>
//		<item>productid: 1234</item>
//		</Strings>

	}
	
	
	
	
	//@RequestBody는 전달된 요청과 내용을 이용해서 해당 파라미터의 타입으로 변환을 요구한다. 내부적으로는 HttpMessageConverter타입의 객체들을 이용해서 다양한 포맷의 입력 데이터를 변환할 수 있다.
	//대부분의 경우에는 JSON 데이터를 서버에 보내서 원하는 타입의 객체로 변환하는 용도로 사용되지만, 경우에 따라서는 원하는 포맷의 데이터를 보내고, 이를 해석해서 원하는 사입으로 사용하기도 한다.
	@PostMapping("/ticket")
	public Ticket convert(@RequestBody Ticket ticket) {
		
		log.info("convert.......ticket"+ticket);
		
		return ticket;
		
		//다른 메서드와 달리 @PostMapping이 적용된 것을 볼 수 있는데, 이것은 @RequestBody가 말 그대로 요청한 내용을 처리하기 떄문에 일반적인 파라미터 전달방식을 사용할 수 없기 때문이다.
	}
}
