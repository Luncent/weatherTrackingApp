package org.example.config;

import org.springframework.context.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

@Configuration
@ComponentScan(basePackages = "org.example",
excludeFilters = {
        @ComponentScan.Filter(type= FilterType.ANNOTATION, classes = Controller.class),
        @ComponentScan.Filter(type= FilterType.ASSIGNABLE_TYPE, classes = {SpringMVCConfig.class,AppConfig.class})
})
@PropertySource("classpath:application-${spring.profiles.active}.properties")
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class TestConfig {
}
