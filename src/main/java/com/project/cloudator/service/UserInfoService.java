package com.project.cloudator.service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.project.cloudator.entity.UserInfo;
import com.project.cloudator.repository.UserInfoRepository;

@Service
public class UserInfoService {

    @Autowired
    private UserInfoRepository repo;

    /**
     * Guarda la información de un usuario.
     *
     * @param userInfo La información del usuario que se quiere guardar.
     */
    public void save(UserInfo userInfo) {
        repo.save(userInfo);
    }

    /**
     * Busca la información de un usuario por su ID.
     *
     * @param id El ID del usuario que se quiere buscar.
     * @return La información del usuario con el ID proporcionado.
     */
    public UserInfo getUserInfoById(Long id) {
        return repo.findById(id).get();
    }

}