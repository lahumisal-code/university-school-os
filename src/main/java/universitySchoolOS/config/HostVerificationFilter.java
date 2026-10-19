
package universitySchoolOS.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class HostVerificationFilter extends OncePerRequestFilter {

    private final List<String> allowedHosts;

    public HostVerificationFilter(
            @Value("${allowed_host.verification_port}") String configuredHosts) {

        this.allowedHosts = Arrays.stream(configuredHosts.split(","))
                .map(String::trim)
                .map(host -> host.replaceFirst("^https?://", ""))
                .toList();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String forwardedHost = request.getHeader("X-Forwarded-Host");
        String directHost = request.getHeader("Host");

        String originatingHost =
                (forwardedHost != null && !forwardedHost.isBlank())
                        ? forwardedHost.trim()
                        : directHost;

        log.debug(
                "Verifying request host. X-Forwarded-Host: {}, Host: {}, Resolved: {}",
                forwardedHost, directHost, originatingHost);

        boolean isAllowed = originatingHost != null
                && allowedHosts.stream()
                .anyMatch(allowed -> allowed.equalsIgnoreCase(originatingHost));

        if (isAllowed) {
            filterChain.doFilter(request, response);
            return;
        }

        log.warn("Blocked request from unauthorized host: {}", originatingHost);

        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType("text/plain");
        response.getWriter().write("Access Denied: Unauthorized request host origin.");
    }
}
