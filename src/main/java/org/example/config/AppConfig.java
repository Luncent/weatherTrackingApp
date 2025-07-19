package org.example.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan(basePackages = {"org.example"})
@EnableAspectJAutoProxy(proxyTargetClass = true)
@PropertySource("classpath:application-${spring.profiles.active}.properties")
public class AppConfig {
}
