package com.coolightman.app.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class CustomLogoutHandler implements LogoutHandler {

    @Override
    public void logout(final HttpServletRequest request,
                       final HttpServletResponse response,
                       final Authentication authentication) {

        HttpSession session = request.getSession(false);

        if (request.isRequestedSessionIdValid() && session != null) {
            session.invalidate();
        }

        Cookie[] cookies = request.getCookies();
        for (final Cookie cookie : cookies) {
            cookie.setMaxAge(0);
            cookie.setValue(null);
            cookie.setPath("/eJournal_war");
            response.addCookie(cookie);
        }
    }
}
