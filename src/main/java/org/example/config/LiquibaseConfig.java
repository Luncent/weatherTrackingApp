package org.example.config;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.util.Objects;

/*@Profile("prod")
@Configuration
public class LiquibaseConfig {
    private final DataSource dataSource;

    @Autowired
    public LiquibaseConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public SpringLiquibase getLiquibaseBean(Environment env){
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(dataSource);
        liquibase.setChangeLog("classpath:/db.changelog/changelog-master.xml");
        if(Objects.equals(env.getProperty("spring.profiles.active"), "test")){
            liquibase.setContexts("test, ");
        }
        liquibase.setShouldRun(true);
        return liquibase;
    }
}*/
