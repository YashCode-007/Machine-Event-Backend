package com.factory.machine_events.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        System.out.println("Auth Filter Called");

        try {

            String jwtToken = jwtUtils.getJwtFromHeader(request);
            if(jwtToken != null && jwtUtils.validateToken(jwtToken)) {
                System.out.println("Token Here : " +jwtToken);

                String userId = jwtUtils.getUserIdFromToken(jwtToken);
                System.out.println("User Id inside filter : "+userId);

                Claims claims = jwtUtils.getAllClaims(jwtToken);

                List<String> roles = claims.get("roles", List.class);
                List<GrantedAuthority> authorities = List.of();
                if(roles != null) {
                    System.out.println("roles : "+roles);
                    authorities = roles.stream()
                            .map(role->(GrantedAuthority)new SimpleGrantedAuthority(role))
                            .toList();
                }
                System.out.println("Authorities after Granted authority : "+authorities);

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userId,
                        null,
                        authorities
                );
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            System.out.println(jwtToken);
        } catch (Exception e) {
            System.out.println("Problem in filter : "+e.getMessage());
        }
        filterChain.doFilter(request,response);
    }
}
