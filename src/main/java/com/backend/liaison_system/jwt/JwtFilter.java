package com.backend.liaison_system.jwt;

import com.backend.liaison_system.user_details.LiaisonUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtServiceImpl jwtServiceImpl;
    private final LiaisonUserDetailsService liaisonUserDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            String authHeader = request.getHeader("Authorization");
            String token;
            String email;

            if (authHeader == null || !authHeader.contains("Bearer ")) {
                filterChain.doFilter(
                        request,
                        response
                );
                return;
            }
            token = authHeader.substring(7);
            email = jwtServiceImpl.extractUserEmail(token);

            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails user = liaisonUserDetailsService.loadUserByUsername(email);

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        user,
                        null,
                        user.getAuthorities()
                );
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }

        } catch (Exception exception) {
            jwtException(response);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void jwtException(HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");

        Map<String, String> error = new HashMap<>();
        error.put("message", "Invalid JWT Token");
        error.put("status", HttpStatus.UNAUTHORIZED.toString());

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(error);
        response.getWriter().write(json);
    }
}
