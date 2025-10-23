package com.elearning.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/hello")
        public String hello () {
        return "ACCESS GRANTED";
    }

    @GetMapping("/student")
    public String studentOnly(){
        return "Hello Student";
    }

    @GetMapping("/instructor")
    public String instructorOnly(){
        return "Hello Instructor";
    }
}

