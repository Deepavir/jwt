package com.example.springsecurity.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.springsecurity.Entity.User;
import com.example.springsecurity.Repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class JwtUserDetailsService implements UserDetailsService {


	@Autowired
	private UserRepository userRepository;

	
	//fetching user as per user in request header and 
	@Override
    @Transactional
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
		System.out.println("loadUserByUsername started");
       
        User user =  userRepository.findByUsername(username);
        System.out.println("user " + user.getUsername());
        System.out.println("user " + user.getPassword());
        
        if (user==null) {
            new UsernameNotFoundException("User not found with username or email ");
        }

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), Collections.emptyList());
    }

    
}

