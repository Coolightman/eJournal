package com.coolightman.app.config;

import com.coolightman.app.security.TokenUtil;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
@Slf4j
public class TokenRequestFilter extends OncePerRequestFilter {
    private static String BEARER = "Bearer ";

    private UserDetailsService userDetailsService;
    private TokenUtil tokenUtil;

    public TokenRequestFilter(final UserDetailsService userDetailsService,
                              final TokenUtil tokenUtil) {
        this.userDetailsService = userDetailsService;
        this.tokenUtil = tokenUtil;
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final FilterChain filterChain) throws ServletException, IOException {

        final String requstTokenHeader = request.getHeader(AUTHORIZATION);
        String username = null;
        String token = null;

        if (requstTokenHeader != null && requstTokenHeader.startsWith(BEARER)) {
            token = requstTokenHeader.replace(BEARER, "");

            try {
                username = tokenUtil.getUsernameFromToken(token);
            } catch (IllegalArgumentException e) {
                log.warn("Unable to get Token");
            } catch (ExpiredJwtException e) {
                log.warn("Token has expired");
            }
        } else {
            log.warn("Token does not begin with Bearer String");
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);
            response.addHeader(AUTHORIZATION, BEARER + token);
        }
        filterChain.doFilter(request, response);
    }
}
