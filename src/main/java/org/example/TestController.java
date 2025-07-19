package org.example;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.log4j.Log4j2;
import org.example.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Log4j2
public class TestController {

    @Autowired
    private UserService userService;
    @Autowired
    private ApplicationContext applicationContext;

    @GetMapping("/test")
    public String test(){
        System.out.println(userService.getAllUsers());
        System.out.println("req");
        return "index";
    }

    @PostConstruct
    public void initCallback(){

        log.info("TestController initCallback");
    }

    @PreDestroy
    public void destroyCallback(){
        log.debug("TestController destroyCallback");
    }
}
