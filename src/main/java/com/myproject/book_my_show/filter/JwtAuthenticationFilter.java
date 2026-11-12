package com.myproject.book_my_show.filter;

import com.myproject.book_my_show.auth.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@Order(1)
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService){
        this.jwtService = jwtService;
    }


    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if(header == null){
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring(7);

        if(!token.isBlank()){
            try{
                Claims claims = jwtService.parse(header.substring(7));
                Long userId = Long.valueOf(claims.getSubject());
                String role = claims.get("role", String.class);

                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(
                                userId, null,
                                List.of(new SimpleGrantedAuthority("ROLE_" + role)));

                SecurityContextHolder.getContext().setAuthentication(auth);
            } catch (JwtException | IllegalArgumentException e){
                log.warn("JWT rejected: {}", e.getMessage());
            }
        }
        filterChain.doFilter(request, response);
    }

}
