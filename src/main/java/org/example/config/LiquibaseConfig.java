package org.example.config;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/*//@Configuration
public class LiquibaseConfig {
    private final DataSource dataSource;

    @Autowired
    public LiquibaseConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public SpringLiquibase getLiquibaseBean(){
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(dataSource);
        liquibase.setChangeLog("classpath:/db.changelog/changelog-master.xml");
        liquibase.setShouldRun(true);
        return liquibase;
    }
}*/
