package com.coolightman.app.security;

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
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.coolightman.app.security.SecurityConstants.BEARER;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

/**
 * The type Token request filter.
 */
@Component
@Slf4j
public class TokenRequestFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;
    private final TokenUtil tokenUtil;

    /**
     * Instantiates a new Token request filter.
     *
     * @param userDetailsService the user details service
     * @param tokenUtil          the token util
     */
    public TokenRequestFilter(final UserDetailsService userDetailsService,
                              final TokenUtil tokenUtil) {
        this.userDetailsService = userDetailsService;
        this.tokenUtil = tokenUtil;
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final FilterChain filterChain) throws ServletException, IOException {

        final Cookie[] cookies = request.getCookies();
        String tokenFromCookie = null;

        if (cookies != null) {
            for (final Cookie cookie : cookies) {
                if (cookie.getName().equals(AUTHORIZATION)) {
                    tokenFromCookie = cookie.getValue();
                }
            }
        }

        String username = null;

        if (tokenFromCookie != null && tokenFromCookie.startsWith(BEARER)) {
            String token = tokenFromCookie.replace(BEARER, "");

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
            final UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            final Authentication authentication =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }
}
