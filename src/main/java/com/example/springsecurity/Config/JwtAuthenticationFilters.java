package com.example.springsecurity.Config;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.springsecurity.service.JwtUserDetailsService;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilters extends OncePerRequestFilter {

	@Autowired
	private JwtUserDetailsService userservice;

	@Autowired
	private JwtTokenUtil jwtutil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		System.out.println("doFilterInternal started");
		// TODO Auto-generated method stub
		// get token from header
		final String requestTokenHeader = request.getHeader("Authorization");
		System.out.println("requestTokenHeader: " + requestTokenHeader);
		String username = null;
		String jwttoken = null;
		if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
			jwttoken = requestTokenHeader.substring(7);
			
			System.out.println("jwttoken  " + jwttoken);
			// extracting user name from userdetails
			try {
				username = this.jwtutil.extractUsername(jwttoken);
				System.out.println("username " + username);
			} catch (ExpiredJwtException ex) {
				ex.printStackTrace();
				System.out.print("token has expired");
			} catch (Exception e) {
				System.out.println("exception " + e);
				e.printStackTrace();
			}
		} else {
			System.out.println("invalid token");
		}

		
		
		System.out.println("SecurityContextHolder.getContext().getAuthentication() " + SecurityContextHolder.getContext().getAuthentication());
		// validation
		if (username != null) {
			UserDetails userdetails = this.userservice.loadUserByUsername(username);
			
			System.out.println("userdetails " + userdetails);
			// authenticate the token
			if (this.jwtutil.validateToken(jwttoken, userdetails)) {
				System.out.println("Token is valid");
				UsernamePasswordAuthenticationToken usernameauth = new UsernamePasswordAuthenticationToken(userdetails,
						null, new ArrayList<>());
				usernameauth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(usernameauth);
			}
		} else {
			System.out.println("token is not valid");
		}
		filterChain.doFilter(request, response);
	}

}
