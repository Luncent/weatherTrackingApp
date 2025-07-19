package org.example.config;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

import java.util.Properties;

@Configuration
@Log4j2
public class DatabaseConfig {

    private final Environment env;

    @Autowired
    public DatabaseConfig(Environment env) {
        this.env = env;
    }

    @PostConstruct
    public void initCallBack(){
        log.warn(env.getProperty("hikari.datasource.connectionUrl"));
    }

    @Bean(destroyMethod = "close")
    @SneakyThrows
    public HikariDataSource getDatasource() {
        Class.forName(env.getProperty("hikari.datasource.driver"));
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(env.getProperty("hikari.datasource.connectionUrl"));
        dataSource.setUsername(env.getProperty("hikari.datasource.username"));
        dataSource.setPassword(env.getProperty("hikari.datasource.password"));
        dataSource.setMaximumPoolSize(Integer.parseInt(env.getProperty("hikari.datasource.max_pool_size")));
        dataSource.setMinimumIdle(Integer.parseInt(env.getProperty("hikari.datasource.min_pool_size")));
        return dataSource;
    }

    @Bean
    @SneakyThrows
    public SessionFactory sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(getDatasource());
        sessionFactory.setPackagesToScan("org.example.entities");

        Properties settings = new Properties();
        settings.put("hibernate.connection.datasource", getDatasource());
        settings.put("hibernate.dialect", env.getProperty("hibernate.dialect"));
        settings.put("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
        settings.put("hibernate.format_sql", env.getProperty("hibernate.format_sql"));
        settings.put("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
        settings.put("hibernate.current_session_context_class", "org.springframework.orm.hibernate5.SpringSessionContext");

        sessionFactory.setHibernateProperties(settings);
        sessionFactory.afterPropertiesSet();
        return sessionFactory.getObject();
    }
}
