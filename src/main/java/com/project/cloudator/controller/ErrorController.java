package com.project.cloudator.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ErrorController {
    @GetMapping("/error401/")
    public String getMethodName() {
        return "/errors/error401";
    }

}
