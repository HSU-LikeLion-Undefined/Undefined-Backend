package com.likelion.RePlay.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

//배포 테스트를 위한 코드
@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello, CI/CD Pipeline!";
    }
}