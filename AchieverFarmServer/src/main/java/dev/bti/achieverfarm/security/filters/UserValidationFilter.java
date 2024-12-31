package dev.bti.achieverfarm.security.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.bti.achieverfarm.common.Common;
import dev.bti.achieverfarm.exceptions.auth.AuthException;
import dev.bti.achieverfarm.models.res.Response;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class UserValidationFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain chain)
            throws ServletException, IOException {

        String requestURI = request.getRequestURI();

        if (requestURI.startsWith("/api/v1/auth/")) {
            chain.doFilter(request, response);
            return;
        }

        String userId = request.getParameter("userId");

        if (userId == null) {
            chain.doFilter(request, response);
            return;
        }

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null) {
                throw new AuthException(HttpStatus.UNAUTHORIZED, "Authentication information is missing.");
            }

            if (authentication.getPrincipal() instanceof UserDetails userDetails) {
                Common.Auth.validate(userId, userDetails);
            } else {
                throw new AuthException(HttpStatus.FORBIDDEN, "Invalid authentication principal.");
            }

            chain.doFilter(request, response);
        } catch (AuthException e) {
            response.setStatus(e.getStatus().value());
            response.setContentType("application/json");

            response.getWriter().write(objectMapper.writeValueAsString(Response.builder()
                    .message(e.getMessage())
                    .success(false)
                    .build()));
        }
    }
}




