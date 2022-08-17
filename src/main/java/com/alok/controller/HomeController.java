package com.alok.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping(path = "/hello")
    public String sayHello(){
        return "Hello from HomeController";
    }
}
