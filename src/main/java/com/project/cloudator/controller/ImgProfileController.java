package com.project.cloudator.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;

import com.project.cloudator.functions.UserImg;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;

@ControllerAdvice
public class ImgProfileController {

    @Autowired
    private UserImg userImg;

    @Value("${domain}")
    private String domain;

    @Autowired
    private RestTemplate restTemplate;

    @ModelAttribute
    public void addAttributes(Model model) {
        Long userId = userImg.getUserId();
        String userImageURL = domain + "upload/" + userId + "/pfp/profile.jpg";
        String defaultImageURL = "/static/images/webProfile.webp";

        if (userId != null && checkImageExists(userImageURL)) {
            model.addAttribute("userIMG", userImageURL);
        } else {
            model.addAttribute("userIMG", defaultImageURL);
        }
    }

    private boolean checkImageExists(String url) {
        try {
            ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.HEAD, HttpEntity.EMPTY, Void.class);
            return response.getStatusCode() == HttpStatus.OK;
        } catch (Exception e) {
            return false;
        }
    }
}