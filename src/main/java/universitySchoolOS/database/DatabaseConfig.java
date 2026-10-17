package universitySchoolOS.database;

import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import lombok.extern.slf4j.Slf4j;
import javax.sql.DataSource;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class DatabaseConfig {

    private final DatabaseProperties databaseProperties;

    @Bean
    public DataSource dataSource() {
        log.info("loading datasource configuration...");
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(databaseProperties.getUrl());
        dataSource.setUsername(databaseProperties.getUsername());
        dataSource.setPassword(databaseProperties.getPassword());
        dataSource.setDriverClassName(databaseProperties.getDriverClassName());
        log.info("datasource loaded successfully.");
        return dataSource;
    }
}