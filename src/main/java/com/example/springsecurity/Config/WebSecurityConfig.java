package com.example.springsecurity.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.example.springsecurity.service.JwtUserDetailsService;

	@Configuration
	@EnableWebSecurity
	public class WebSecurityConfig  {

		@Autowired
		private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

		@Autowired
		private JwtUserDetailsService JwtUserDetailsService;
		@Autowired
		private JwtAuthenticationFilters JwtAuthenticationFilters;
	    @Bean
	    public PasswordEncoder passwordEncoder() {
	    	System.out.println("passwordEncoder started");
	        return  NoOpPasswordEncoder.getInstance();
	    }
	    @Bean
	    public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration configuration) throws Exception {
	    	System.out.println("authenticationManagerBean started");
	 	   return configuration.getAuthenticationManager();
	 	   }
	   
	
	    @Bean
	    public JwtAuthenticationFilters jwtAuthenticationFilter(){
	    	System.out.println("jwtAuthenticationFilter started");
	        return  new JwtAuthenticationFilters();
	    }
	   
	
	    @Bean
	    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	    	System.out.println("filterChain started");
	      http.cors().and().csrf().disable()
	          .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
	          .authorizeRequests().requestMatchers("/authenticate**").permitAll()
	          //.requestMatchers("/**").permitAll()
	          .anyRequest().authenticated()
	          .and()
	          .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint);
	      

	      http.addFilterBefore(JwtAuthenticationFilters, UsernamePasswordAuthenticationFilter.class);
	      
	      return http.build();
	    }

}
