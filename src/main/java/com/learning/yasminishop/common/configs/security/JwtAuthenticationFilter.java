package com.learning.yasminishop.common.configs.security;

import com.learning.yasminishop.common.exception.AppException;
import com.learning.yasminishop.common.exception.ErrorCode;
import com.learning.yasminishop.user.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;


@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");
        final String tokenPrefix = "Bearer ";

        // check if the authorization header is null or does not start with "Bearer "
        if (authorizationHeader == null || !authorizationHeader.startsWith(tokenPrefix)) {
            filterChain.doFilter(request, response);
            return;
        }

        final String jwtToken = authorizationHeader.replace(tokenPrefix, "");
        final String userEmail = jwtService.extractUserEmail(jwtToken);
        final String tokenType = jwtService.extractTokenType(jwtToken);

        if (userEmail != null && tokenType.equals("access") && SecurityContextHolder.getContext().getAuthentication() == null) {

            boolean isTokenValid = jwtService.isTokenValid(jwtToken);

            if (isTokenValid) {

                var user = userRepository.findByEmail(userEmail)
                        .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));
                if(Boolean.FALSE.equals(user.getIsActive())) throw new AppException(ErrorCode.UNAUTHENTICATED);

                List<String> scopes = jwtService.getAuthorities(user);

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        user.getEmail(),
                        null,
                        AuthorityUtils.createAuthorityList(scopes));
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }

        }

        filterChain.doFilter(request, response);

    }
}
