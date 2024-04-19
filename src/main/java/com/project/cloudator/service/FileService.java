package com.project.cloudator.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.cloudator.entity.File;
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

    public List<File> findFilesByOwner(Long owner) {
        return repo.findFilesByOwner(owner);
    }

    public List<File> findFilesByFilename(String busqueda) {
        return repo.findFilesByFilename(busqueda);
    }

}
