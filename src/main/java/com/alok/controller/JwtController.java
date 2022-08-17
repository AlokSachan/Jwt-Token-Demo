package com.alok.controller;


import com.alok.model.JwtRequest;
import com.alok.model.JwtResponse;
import com.alok.service.CustomUserDetailsService;
import com.alok.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class JwtController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @PostMapping(path = "/generateToken")
    public ResponseEntity<JwtResponse> generateToken(@RequestBody JwtRequest jwtRequest){

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(jwtRequest.getUserName(), jwtRequest.getPassword());
        authenticationManager.authenticate(authenticationToken);
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(jwtRequest.getUserName());
        String token = jwtUtil.generateToken(userDetails);
        return new ResponseEntity<>(JwtResponse.builder()
                .token(token).build(), HttpStatus.CREATED);
    }
}
