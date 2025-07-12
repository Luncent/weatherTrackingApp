package org.example.controllers.config;

import jakarta.annotation.PostConstruct;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.springframework.context.annotation.Configuration;
import java.util.Properties;

@Configuration
public class DatabaseConfig {

    @PostConstruct
    public void initCallback(){
        try(SessionFactory factory = createSessionFactory();
            Session session = factory.openSession()){

            session.beginTransaction();
            try {
                User user = session.get(User.class, 1L);
                System.out.println(user);
            }
            catch(Exception e){
                session.getTransaction().rollback();
                e.printStackTrace();
            }
        }
    }


    private SessionFactory createSessionFactory() {
        try {
            var configuration = getConfiguration();
            configuration.addAnnotatedClass(User.class);

            StandardServiceRegistryBuilder serviceRegistry = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties());

            SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry.build());
            return sessionFactory;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private static org.hibernate.cfg.Configuration getConfiguration() {
        var configuration = new org.hibernate.cfg.Configuration();

        Properties settings = new Properties();
        settings.put("hibernate.connection.driver_class", "org.postgresql.Driver");
        settings.put("hibernate.connection.url", "jdbc:postgresql://localhost:5433/weather_tracking_app_db");
        settings.put("hibernate.connection.username", "postgres");
        settings.put("hibernate.connection.password", "123");
        settings.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQL9Dialect");
        settings.put("hibernate.show_sql", "true");
        settings.put("format_sql", "true");
        settings.put("hibernate.hbm2ddl.auto", "validate");

        configuration.setProperties(settings);
        return configuration;
    }

}
