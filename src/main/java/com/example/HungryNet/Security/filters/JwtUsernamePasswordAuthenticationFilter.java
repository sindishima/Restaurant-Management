package com.example.HungryNet.Security.filters;

import com.example.HungryNet.Security.UserDetailsServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    public JwtUsernamePasswordAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
//        try {
//            UsernameAndPasswordAuthenticationRequest authenticationRequest = new ObjectMapper().readValue
//                    (request.getInputStream(), UsernameAndPasswordAuthenticationRequest.class);
//
//            Authentication auth = new UsernamePasswordAuthenticationToken(
//                    authenticationRequest.getUsername(),
//                    authenticationRequest.getPassword()
//            );
//
//            return authenticationManager.authenticate(auth);
//        }
//        catch (IOException e){
//            throw new RuntimeException(e);
//        }
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        return authenticationManager.authenticate(authenticationToken);  //compare this with userDetailService name and password

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
            String key = "secretkey12345678secretkey12345678secretkey12345678secretkey12345678secretkey12345678";
               String token = Jwts.builder()
                       .setSubject(authResult.getName())
                       .claim("authorities", authResult.getAuthorities())
                       .setIssuedAt(new Date())
                       .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusWeeks(2)))
                       .signWith(Keys.hmacShaKeyFor(key.getBytes()))
                       .compact();

//               Map<String, String> tokens = new HashMap<>();
//               tokens.put("Token ", "Bearer " + token);
               response.setHeader("Authorization", "Bearer " + token);
//        new ObjectMapper().writeValue(response.getOutputStream(), tokens);
    }
}
