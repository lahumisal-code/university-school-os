package universitySchoolOS.cors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class CorsConfig {

    private final CorsProperties corsProperties;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        log.info("Initializing CorsConfigurationSource from external properties...");
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(parsePropertyString(corsProperties.getAllowedOrigins()));
        configuration.setAllowedMethods(parsePropertyString(corsProperties.getAllowedMethods()));
        configuration.setAllowedHeaders(parsePropertyString(corsProperties.getAllowedHeaders()));
        configuration.setExposedHeaders(parsePropertyString(corsProperties.getExposedHeaders()));

        configuration.setAllowCredentials(corsProperties.isAllowCredentials());
        configuration.setMaxAge(corsProperties.getMaxAge());
        log.info("Loaded CORS configuration properties successfully. Origins allowed: {}", configuration.getAllowedOrigins());

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        log.info("CORS configuration source mapped to all application paths (/**).");
        return source;
    }

    private List<String> parsePropertyString(String propertyValue) {
        if (propertyValue == null || propertyValue.isBlank()) {
            return List.of();
        }
        return Arrays.stream(propertyValue.split(","))
                .map(String::trim)
                .filter(str -> !str.isEmpty())
                .collect(Collectors.toList());
    }
}