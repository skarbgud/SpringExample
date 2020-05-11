package org.zerock.controller;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.zerock.domain.Ticket;

import com.google.gson.Gson;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
//Test for Controller
@WebAppConfiguration
@ContextConfiguration({"file:src/main/webapp/WEB-INF/spring/root-context.xml","file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml"})
//Java Config
//@ContextConfiguration(classes ={
//org.zerock.config.RootConfig.class,
//org.zerock.config.ServletConfig.class})
@Log4j
public class SampleControllerTests {
	
	@Setter(onMethod_ = {@Autowired})
	private WebApplicationContext ctx;
	
	private MockMvc mockMvc;
	
	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx).build();
	}
	
	//testConvert()는 SampleController에 작성해 둔  convert() 메서드를 테스트하기 위해 작성
	//SampleController의 convert()는 JSON으로 전달되는 데이터를 받아서 Ticket타입으로 변환한다.
	//이를 위해서 해당 데이터가 JSON이라는 것을 명시해 줄 필요가 있다.
	@Test
	public void testConvert() throws Exception{
		
		Ticket ticket = new Ticket();
		ticket.setTno(123);
		ticket.setOwner("Admin");
		ticket.setGrade("AAA");
		
		String jsonStr = new Gson().toJson(ticket); 	//코드 내의 Gson 라이브러리는 java의 객체를 JSON문자열로 변환하기 위해서 사용
		
		log.info(jsonStr);
		
		mockMvc.perform(post("/sample/ticket")
				.contentType(MediaType.APPLICATION_JSON) 	//MockMvc는 contentType을 이용해서 전달하는 데이터가 무엇인지 알려줄 수 있다.
				.content(jsonStr))
		.andExpect(status().is(200));
	}
}
