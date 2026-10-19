package universitySchoolOS.config;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;
import universitySchoolOS.AppConstatnt.AppConstant;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final JwtFilter jwtFilter;
    private final JwtLogoutHandler jwtLogoutHandler;
    private final HostVerificationFilter hostVerificationFilter;
    private final CorsConfigurationSource corsConfigurationSource;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/v1/api/auth/login").permitAll()
                        .requestMatchers("/v1/api/auth/register").permitAll()
                        .requestMatchers("/v1/api/auth/logout").authenticated()
                        .requestMatchers("/v1/api/principal/**").hasAuthority("PRINCIPAL")
                        .requestMatchers("/v1/api/teacher/**").hasAuthority("TEACHER")
                        .requestMatchers("/v1/api/student/**").hasAuthority("STUDENT")
                        .requestMatchers("/v1/api/university/**").hasAuthority("UNIVERSITY")
                        .anyRequest().authenticated()
                )
                .httpBasic(basic -> basic.disable())
                .authenticationProvider(authenticationProvider())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(hostVerificationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout -> logout
                        .logoutUrl(AppConstant.LOGOUT_URL)
                        .addLogoutHandler(jwtLogoutHandler)
                        .logoutSuccessHandler(
                                (request, response, authentication) -> {
                                    if (response.getStatus() == HttpServletResponse.SC_OK) {
                                        response.setContentType("text/plain");
                                        response.getWriter().write(AppConstant.LOGOUT);
                                    }
                        })
                )
                .build();
    }



    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder(12);
    }

}
