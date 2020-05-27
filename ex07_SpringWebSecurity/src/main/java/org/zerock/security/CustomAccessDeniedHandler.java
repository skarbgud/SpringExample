package org.zerock.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import lombok.extern.log4j.Log4j;

@Log4j
public class CustomAccessDeniedHandler implements AccessDeniedHandler{	
	//CustomAccessDeniedHandler클래스는 AccessDeniedHandler 인터페이스를 직접 구현한다.
	//인터페이스의 메서드는 handle() 뿐이기 때문이고 HttpServletRequest,HttpServletResponse를 파라미터로 사용하기 때문에 직접 서블릿 API를 이용하는 처리가 가능하다.
	
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		// TODO Auto-generated method stub
		
		log.error("Access Denied Handler");
		
		log.error("Redirect....");
		
		response.sendRedirect("/accessError");
		
		
	}
	
	
}
