package org.example.controllers;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TestController {


    @GetMapping("/test")
    public String test(){
        System.out.println("req");
        return "index";
    }


    @PostConstruct
    public void initCallback(){
        System.out.println("TestController initCallback");
    }

    @PreDestroy
    public void destroyCallback(){
        System.out.println("TestController destroyCallback");
    }
}
