package com.project.cloudator.repository;

import com.project.cloudator.entity.File;

import java.util.List;
import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Long> {

    @Query("SELECT FUNCTION('date', f.filedate) AS date, COUNT(f) AS count FROM File f WHERE f.filedate BETWEEN :startDate AND :endDate AND f.owner = :ownerId GROUP BY FUNCTION('date', f.filedate)")
    List<Object[]> countFilesByOwnerAndDateRangeGrouped(@Param("startDate") Date startDate,
            @Param("endDate") Date endDate, @Param("ownerId") Long ownerId);

    @Query("SELECT FUNCTION('date', f.filedate) AS date, COUNT(f) AS count FROM File f WHERE f.filedate BETWEEN :startDate AND :endDate GROUP BY FUNCTION('date', f.filedate)")
    List<Object[]> countFilesByDateRangeGrouped(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query("SELECT f FROM File f WHERE f.owner = :owner")
    List<File> findFilesByOwner(Long owner);

    @Query(value = "SELECT * FROM files WHERE filename LIKE %:busqueda%", nativeQuery = true)
    public List<File> findFilesByFilename(@Param("busqueda") String filename);

    @Query("SELECT f.filesize FROM File f WHERE f.owner = :ownerId")
    List<Long> findFileSizesByOwner(@Param("ownerId") Long ownerId);

}
