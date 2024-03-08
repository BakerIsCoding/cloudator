package com.project.cloudator.service;

import com.project.cloudator.entity.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.cloudator.repository.FileRepository;

@Service
public class FileService {
    @Autowired
    private FileRepository repo;

    public String saveFileOnDb(File file) {
        repo.save(file);
        System.out.println("Fichero guardado correctamente");
        return "Fichero guardado correctamente";
    }
}
