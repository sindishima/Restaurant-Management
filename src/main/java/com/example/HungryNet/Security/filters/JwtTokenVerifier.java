package com.example.HungryNet.Security.filters;

import com.example.HungryNet.Security.UserDetailsImpl;
import com.example.HungryNet.Security.UserDetailsServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class JwtTokenVerifier extends OncePerRequestFilter {

//    @Autowired
//    UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");

        String url = request.getServletPath();
        if (request.getServletPath().startsWith("/login")) {
            filterChain.doFilter(request, response);  //forward the request into the next filter in the chain
        }



        String token = authorizationHeader.replace("Bearer ", "");


        if (!authorizationHeader.startsWith("Bearer ") || token == null) {
            response.setStatus(403);
            Map<String, String> error = new HashMap<>();
            error.put("error", "Unauthorized");
            response.setContentType(APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(), error);
            filterChain.doFilter(request, response);
        } else {
            try {
                String keys = "secretkey12345678secretkey12345678secretkey12345678secretkey12345678secretkey12345678";

                Jws<Claims> claimJws = Jwts.parser()
                        .setSigningKey(Keys.hmacShaKeyFor(keys.getBytes()))
                        .parseClaimsJws(token);

                Claims body = claimJws.getBody();
                String username = body.getSubject();


                var authorities = (List<Map<String, String>>) body.get("authorities");
                Set<SimpleGrantedAuthority> simpleGrantedAuthoritySet = authorities.stream().map(m -> new SimpleGrantedAuthority(m.get("authority")))
                        .collect(Collectors.toSet());

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                username,
                                null,
                                simpleGrantedAuthoritySet);

                SecurityContextHolder.getContext().setAuthentication(authentication);
                filterChain.doFilter(request, response);
            } catch (JwtException e) {
                response.setStatus(403);
                Map<String, String> error = new HashMap<>();
                error.put("error", e.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        }

    }
}
