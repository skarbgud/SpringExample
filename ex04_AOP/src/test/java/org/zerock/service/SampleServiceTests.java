package org.zerock.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
@Log4j
@ContextConfiguration({"file:src/main/webapp/WEB-INF/spring/root-context.xml"})
//java설정의 경우
//@ContextConfiguration(class={RootConfig.class})
public class SampleServiceTests {
	
	@Setter(onMethod_ = @Autowired)
	private SampleService service;
	
	//AOP 설정을 한 Target에 대해서 Proxy 객체가 정상적으로 만들어져 있는지 확인
	//<aop:aspectj-autoproxy></aop:aspectj-autoproxy>가 정상적으로 모든 동작을 하고, LogAdvice에 설정 문제가 없다면 service 변수의 클래스는 단순히 
	//org.zerock.service.SampleServiceImp의 인스턴스가 아닌 생성된 Proxy클래스의 인스턴스가 된다.  
	
	@Test
	public void testClass() {
		
		log.info(service);
		log.info(service.getClass().getName());
		
		//결과
//		INFO : org.zerock.service.SampleServiceTests - org.zerock.service.SampleServiceImpl@4278284b
//		INFO : org.zerock.service.SampleServiceTests - com.sun.proxy.$Proxy20
		// 단순히 service 변수를 출력했을 때는 기존에 사용하듯이 SampleServiceImpl 클래스의 인스턴스처럼 보인다.
		// 이것은 toString()의 결과이므로 좀 더 세밀하게 파악하려면 getClass()를 이용해서 파악해야만 한다.
		// com.sun.proxy.$Proxy는 JDK의 다이나믹프록시 기법이 적용된 결과이다. 
	}
	
	@Test
	public void testAdd() throws Exception{
		
		log.info(service.doAdd("123", "456"));
		
		//결과
//		INFO : org.zerock.aop.LogAdvice - ====================	AOP의 LogAdvice의 설정이 같이 적용된다.
//		INFO : org.zerock.service.SampleServiceTests - 579
	}
	
	
	//@AfterThrowing   TEST
	@Test
	public void testAddError() throws Exception{
		
		log.info(service.doAdd("123", "ABC"));
		
//		INFO : org.zerock.aop.LogAdvice - Exception...!!
//		INFO : org.zerock.aop.LogAdvice - exception: java.lang.NumberFormatException: For input string: "ABC"
		
		
	}
}
