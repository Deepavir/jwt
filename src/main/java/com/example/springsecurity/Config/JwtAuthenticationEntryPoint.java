package com.example.springsecurity.Config;

import java.io.IOException;

import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
//class will thrown exception due to unauthorized user

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
	

	
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException e) throws IOException, ServletException {
		
		System.out.println("Responding with unauthorized error");
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Access denied");
    }
		
}



