package com.project.cloudator.service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.project.cloudator.entity.UserInfo;
import com.project.cloudator.repository.UserInfoRepository;

@Service
public class UserInfoService {

    @Autowired
    private UserInfoRepository repo;

    public void save(UserInfo userInfo) {
        repo.save(userInfo);
    }

    public UserInfo getUserInfoById(Long id) {
        return repo.findById(id).get();
    }

}