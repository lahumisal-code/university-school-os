package universitySchoolOS.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;
import universitySchoolOS.service.JwtService;

@Slf4j
@Component
public class JwtLogoutHandler implements LogoutHandler {

    private final JwtService jwtService;

    public JwtLogoutHandler(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public void logout(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) {
        boolean success= jwtService.logout(request);
        if (success) {
            response.setStatus(HttpServletResponse.SC_OK);
            log.info("Logout successful");
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            log.info("Logout failed: token already invalid");
        }
        log.info("Logout handler executed");
    }
}