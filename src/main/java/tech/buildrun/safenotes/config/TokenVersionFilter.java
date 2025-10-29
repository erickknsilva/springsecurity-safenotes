package tech.buildrun.safenotes.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import tech.buildrun.safenotes.repository.UserRepository;

import java.io.IOException;

@Component
public class TokenVersionFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;

    public TokenVersionFilter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof JwtAuthenticationToken jwtToken) {
            var jwt = jwtToken.getToken();
            var tokenVersion = jwt.getClaimAsString("version");

            if (tokenVersion == null) {
                throw new RuntimeException("Token version is null");
            }

            long userId = Long.parseLong(jwt.getSubject());
            var currentVersion = userRepository.findTokenVersionByUserId(userId);

            if (!currentVersion.toString().equalsIgnoreCase(tokenVersion)) {
                deny(response);
                return;
            }


        }

        filterChain.doFilter(request, response);
    }

    private void deny(HttpServletResponse response) throws IOException {
        SecurityContextHolder.clearContext();
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write("""
                {
                    "error": "Invalid_token_version"
                }
                """);
    }
}
