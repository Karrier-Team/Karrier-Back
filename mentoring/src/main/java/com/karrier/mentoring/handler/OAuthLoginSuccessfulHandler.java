package com.karrier.mentoring.handler;

import com.karrier.mentoring.auth.PrincipalDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class OAuthLoginSuccessfulHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = ((PrincipalDetails) principal).getUsername();
        response.sendRedirect("/members/update-login/"+email);
    }
}
