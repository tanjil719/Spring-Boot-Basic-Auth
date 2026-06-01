package com.example.basicauth.filters;

import com.example.basicauth.payloads.CustomPrincipal;
import com.example.basicauth.utilities.JWTUtil;
import com.example.basicauth.utilities.SecurityUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// OncePerRequestFilter is a base class for filters that should be invoked once per request.
// It provides a doFilterInternal() method that subclasses must implement to perform the actual filtering logic.

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        SecurityUtil.getTokenFromRequestHeader(request).ifPresent(token -> {
            try {
                if (JWTUtil.isValidToken(token)) {
                    if (SecurityContextHolder.getContext().getAuthentication() == null) {
                        CustomPrincipal customPrincipal = JWTUtil.getTokenHolderDetails(token);

                        if (customPrincipal != null) {
                            //Generating Authentication Object to store it in SecurityContextHolder
                            UsernamePasswordAuthenticationToken authenticationObject = SecurityUtil.getAuthentication(customPrincipal, UsernamePasswordAuthenticationToken.class);

                            //Store the request details in the authentication object. This is useful for later use in the
                            //application, such as logging or auditing.
                            authenticationObject.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                            SecurityContextHolder.getContext().setAuthentication(authenticationObject);
                        }
                    }
                }
            } catch (Exception e) {
                // Log the exception or handle it as needed
                logger.error("Error processing JWT authentication", e);
            }
        });

        //Call the next filter in the chain.
        filterChain.doFilter(request, response);
    }
}
