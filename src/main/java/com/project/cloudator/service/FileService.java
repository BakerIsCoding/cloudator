package com.project.cloudator.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Date;
import java.util.ArrayList;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.cloudator.entity.File;
import com.project.cloudator.entity.FileCountByDate;
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

    public Long getStorage(Long userId) {
        long totalSpaceUsed = 0;
        List<Long> totalStorageUsed = repo.findFileSizesByOwner(userId);

        for (Long fileSize : totalStorageUsed) {
            totalSpaceUsed += fileSize;
        }

        return totalSpaceUsed;
    }

    public List<FileCountByDate> countFilesFromLastWeekGrouped() {
        Calendar calendar = Calendar.getInstance();

        // Se ajusta la fecha
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date endDate = calendar.getTime();

        calendar.add(Calendar.DATE, -7); // -7 días
        Date startDate = calendar.getTime(); // Fecha normal

        List<Object[]> results = repo.countFilesByDateRangeGrouped(startDate, endDate);
        ArrayList<FileCountByDate> fileCounts = new ArrayList<>();

        for (Object[] result : results) {
            Date date = (Date) result[0];
            Long count = (Long) result[1];
            FileCountByDate fileCountByDate = new FileCountByDate(date, count);
            fileCounts.add(fileCountByDate);
        }

        return fileCounts;
    }/*
      * List<Object[]> results = repo.countFilesByDateRangeGrouped(startDate,
      * endDate);
      * return results.stream()
      * .map(result -> new FileCountByDate((Date) result[0], (Long) result[1]))
      * .collect(Collectors.toList());
      * }
      */

    public List<FileCountByDate> countFilesForOwnerFromLastWeekGrouped(Long ownerId) {
        Calendar calendar = Calendar.getInstance();

        // Se ajusta la fecha
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date endDate = calendar.getTime(); // Fecha de hoy a medianoche

        calendar.add(Calendar.DATE, -7); // -7 días
        Date startDate = calendar.getTime(); // Fecha normal

        List<Object[]> results = repo.countFilesByOwnerAndDateRangeGrouped(startDate, endDate, ownerId);
        ArrayList<FileCountByDate> fileCounts = new ArrayList<>();

        for (Object[] result : results) {
            Date date = (Date) result[0];
            Long count = (Long) result[1];
            FileCountByDate fileCountByDate = new FileCountByDate(date, count);
            fileCounts.add(fileCountByDate);
        }

        return fileCounts;
    }

    public List<File> findFilesByOwner(Long owner) {
        return repo.findFilesByOwner(owner);
    }

    public List<File> findFilesByFilename(String busqueda) {
        return repo.findFilesByFilename(busqueda);
    }

}
