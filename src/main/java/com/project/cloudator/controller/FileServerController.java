package com.project.cloudator.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class FileServerController {
    @PostMapping("/fileserver/upload/")
    public String postMethodName(@RequestBody String jwtToken) {
        

        return "xd";
    }

}
