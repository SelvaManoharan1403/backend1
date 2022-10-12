package com.profitableaccountingsystemapi.config;

import com.profitableaccountingsystemapi.service.UserService;
import com.profitableaccountingsystemapi.util.JwtUtils;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = httpServletRequest.getHeader("authorization");
        String username = null;
        String jwtToken = null;

        try {
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                jwtToken = authorizationHeader.substring(7);
                username = jwtUtils.getUsernameFromToken(jwtToken);
            }

            if ( username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails currentUser =  this.userService.loadUserByUsername(username);
                Boolean tokenValidate = jwtUtils.validateToken(jwtToken, currentUser);

                if (tokenValidate) {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(currentUser, null, currentUser.getAuthorities());
                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }
        }  catch (ExpiredJwtException ex) {
            String requestURL = httpServletRequest.getRequestURL().toString();
            // allow for Refresh Token creation if following conditions are true.
            if (requestURL.contains("refreshToken")) {
                allowForRefreshToken(ex, httpServletRequest);
            } else
                httpServletRequest.setAttribute("exception", ex);

        } catch (BadCredentialsException ex) {
            httpServletRequest.setAttribute("exception", ex);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private void allowForRefreshToken(ExpiredJwtException ex, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                null, null, null);
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        request.setAttribute("claims", ex.getClaims());
    }
}
