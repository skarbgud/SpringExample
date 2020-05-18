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
@ContextConfiguration({ "file:src/main/webapp/WEB-INF/spring/root-context.xml" })
//Java설정의 경우 
//@ContextConfiguration(classes= {RootConfig.class})

public class SampleTXServiceTests {

  @Setter(onMethod_ = { @Autowired })
  private SampleTXService service;
  

  @Test
  public void testLong() {
    
    String str ="Starry\r\n" + 
        "Starry night\r\n" + 
        "Paint your palette blue and grey\r\n" + 
        "Look out on a summer's day";
    
    log.info(str.getBytes().length);
    
    service.addData(str);    
  }
  
  /* @Transactional 어노테이션 속성들
  	스프링에서는 트랜잭션을 처리하기 위해 제공되는 @Transactional 어노테이션을 이용하면 간단히 트랜잭션 설정을 완료할 수 있다.
  	이때 지정할 수 있는 속성들은 다음과 같다
  	
  	@Transactional 어노테이션은 몇 가지 중요한 속성을 가지고 있으니, 경우에 따라서는 속성들을 조정해서 사용해야 한다.
  	
  	전파속성
  	-PROPAGATION_MADATORY : 작업은 반드시 트랜잭션이 존재한 상태에서만 가능
  	-PROPAGATION_NESTED : 기존에 트랜잭션이 있는 경우, 포함되어서 실행
  	-PROPAGATION_NEVER : 트랜잭션 상황하에 실해외면 예외 발생
  	-PROPAGATION_NOT_SUPPORTED : 트랜잭션이 있는 경우엔 트랜잭션이 끝날 때까지 보류된 후 실행
  	-PROPAGATION_REQUIRED : 트랜잭션이 있으면 그 상황에서 실행, 없으면 새로운 트랜잭션 실행(기본설정)
  	-PROPAGATION_REQUIRED_NEW : 대상은 자신만의 고유한 트랜잭션으로 실행
  	-PROPAGATION_SUPPORTS : 트랜잭션을 필요로 하지 않으나, 트랜잭션 상황하에 있다면 포함되어서 실행
  	
  	격리속성
  	-DEFAULT : DB설정, 기본 격리 수준(기본설정)
  	-SERIALIZABLE: 가장 높은 격리, 성능 저하의 우려가 있음
  	-READ_UNCOMMITED : 커밋되지 않은 데이터에 대한 읽기를 허용
  	-READ_COMMITED : 커밋된 데이터에 대해 읽기 허용
  	-REPEATEABLE_READ : 동일 필드에 대해 다중 접근 시 모두 동일한 결과를 보장
  	
  	Read-only 속성
  	-true인 경우 insert, update, delete실행 시 예외 발생, 기본 설정은 false
  	
  	Rollback-only 예외
  	-특정 예외가 발생시 강제로 Rollback
  	
  	No-rollback-for-예외
  	-특정 예외의 발생 시에는 Rollback 처리되지 않음
  	
  	위의 속성은 모두  @Transactional을 설정할 때 속성으로 지정할 수 있다.
  	
  	@Transactional 적용 순서
  	스프링은 간단한 트랜잭션 매니저의 설정과 @Transactional 어노테이션을 이용한 설정만으로 애플리케이션 내의 트랜잭션에 대한 설정을 처리할 수 있다.
  	
  	@Transactional 어노테이션의 경우 위와 같이 메서드에 설정하는  것도 가능하지만, 클래스나 인터페이스에 선언하는 것 역시 가능하다.
  	
  	어노테이션의 우선순위
  	-메서드의  @Transactional 설정이 가장 우선시 된다.
  	-클래스의 @Transactional 설정은 메서드보다 우선순위가 낮다.
  	-인터페이스의  @Transactional 설정이 가장 낮은 우선순위 이다.
  	
 */
  
  
}


