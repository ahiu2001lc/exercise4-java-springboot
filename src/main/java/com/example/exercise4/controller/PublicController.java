package com.example.exercise4.controller;

import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/public")
public class PublicController {

    @GetMapping("/ping")
    public Map<String, Boolean> ping() {
        return Map.of("pong", true);
    }

    @GetMapping("/hello")
    public String hello(){
        return "Hello from Java Spring-boot!";
    }

    @GetMapping("/bye")
    public String bye(){
        return "Goodbye from Bob!";
    }
}
