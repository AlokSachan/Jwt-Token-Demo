package com.alok.filter;

import com.alok.service.CustomUserDetailsService;
import com.alok.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String bearerToken = request.getHeader("Authorization");
        String userName= null;
        String token =null;
        if(bearerToken != null && bearerToken.startsWith("Bearer ")) {

            token = bearerToken.substring(7);
            try{
                  userName= jwtUtil.extractUsername(token);
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(userName);
                if(userName != null && SecurityContextHolder.getContext().getAuthentication() == null){
                      UsernamePasswordAuthenticationToken jwtToken= new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    jwtToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(jwtToken);
                  }
                  else {
                      log.error("Invalid Token!!");
                  }
            }
            catch (Exception ex){
                log.error(ex.getLocalizedMessage());
            }
        }
        else
            log.error("Provide Valid Token!!");

        filterChain.doFilter(request,response);
    }
}
