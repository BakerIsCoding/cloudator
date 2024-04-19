package com.project.cloudator.repository;

import com.project.cloudator.entity.File;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Long> {

    @Query("SELECT f FROM File f WHERE f.owner = :owner")
    List<File> findFilesByOwner(Long owner);

    @Query(value = "SELECT * FROM files WHERE filename LIKE %:busqueda%", nativeQuery = true)
    public List<File> findFilesByFilename(@Param("busqueda") String filename);

}
