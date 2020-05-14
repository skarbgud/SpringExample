package org.zerock.aop;

import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j;

@Aspect	//해당 클래스의 객체가 Aspect를 구현한 것임으로 나타내기 위해서 사용	
@Log4j
@Component //AOP와는 관계가 없지만 스프링에서 빈(bean)으로 인식하기 위해 사용
public class LogAdvice {
							//"execution...."문자열은 AspectJ의 표현식이다. 'execution'의 경우 접근제한자와 특정 클래스의 메서드를 지정할 수 있다.
	@Before( "execution(* org.zerock.service.SampleService*.*(..))")	//Pointcut지정 별도의 @Pointcut으로 지정해서 사용할 수도 있다.
	public void logBefore() {	//logBefore()는 @Before어노테이션을 적용하고 있다. @Before는 BeforeAdvice를 구현한 메서드로 추가한다.
		
		log.info("====================");
	}
	
	
	@Before("execution(* org.zerock.service.SampleService*.doAdd(String,String)) && args(str1,str2)")	//execution으로 시작하는 Pointcut설정에 doAdd()메서드 명시
	public void logBeforeWithParam(String str1, String str2) {					//파라미터의 타입을 명시
		
		log.info("str1: "+str1);
		log.info("str2: "+str2);
	}

	
	//@AfterThrowing - 코드를 실행하다 보면 파라미터의 값이 잘못되어서 예외가 발생하는 경우가 많다. AOP의 @AfterThrowing 어노테이션은 지정된 대상이 예외를 발생한 후에 동작하면서 문제를 찾을 수 있다.
	@AfterThrowing(pointcut = "execution(* org.zerock.service.SampleService*.*(..))", throwing = "exception")
	public void logException(Exception exception) {
		
		log.info("Exception...!!");
		log.info("exception: "+exception);
		
	}
	
	//AOP를 이용해서 좀 더 구체적인 처리를 하고 싶다면 @Around와 ProceedingJoinPoint를 이용해야한다. 
	//@Around는 조금 특별하게 동작하는데 직접 대상 메서드를 실행할 수 있는 권한을 가지고 있고, 메서드의 실행 전과 실행 후에 처리가 가능하다
	//ProceedingJoinPoint는 @Around와 같이 결합해서 파라미터나 예외 등을 처리할 수 있다.
	@Around("execution(* org.zerock.service.SampleService*.*(..))")	//Pointcut설정
	public Object logTime(ProceedingJoinPoint pjp) {	//파라미터로 ProceedingJoinPoint라는 파라미터를 지정하는데,ProceedingJoinPoint는 AOP의 대상이 되는 Target이나 파라미터등
		//뿐만 아니라, 직접 실행을 결정할 수도 있다. @Befor 등과 달리 @Around가 적용되는 메서드의 경우에는 리턴타입이 void가 아닌 타입으로 설정하고, 메서드의 실행 결과 역시 직접 반환하는 형태로 작성한다.
		
		long start = System.currentTimeMillis();
		
		log.info("Target: "+pjp.getTarget());
		log.info("Param: "+Arrays.toString(pjp.getArgs()));
		
		//invoke method
		Object result = null;
		
		try {
			result = pjp.proceed();
		} catch (Throwable e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		long end = System.currentTimeMillis();
		
		log.info("TIME: "+ (end - start));
		
		return result;
	}

}
