package com.guizaouiadam1234.b2b_inventory_api.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class JwtAuthFilter extends OncePerRequestFilter {

    private final RestTemplate restTemplate;
    private final String authApiUrl;

    public JwtAuthFilter(RestTemplate restTemplate, String authApiUrl) {
        this.restTemplate = restTemplate;
        this.authApiUrl = authApiUrl;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                @SuppressWarnings("rawtypes")
                ResponseEntity<Map> resp = restTemplate.postForEntity(
                        authApiUrl + "/api/auth/validate",
                        Map.of("token", token),
                        Map.class
                );
                @SuppressWarnings("unchecked")
                Map<String, Object> body = resp.getBody();
                if (body != null && Boolean.TRUE.equals(body.get("valid"))) {
                    String username = (String) body.get("username");
                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(username, null, List.of());
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            } catch (Exception ignored) {
                // Invalid token or auth-api unreachable — SecurityContext stays empty,
                // Spring Security will respond with 401 Unauthorized.
            }
        }

        filterChain.doFilter(request, response);
    }
}
