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

    @Autowired
    private UserService userService;

    /**
     * Guarda un archivo en la base de datos.
     *
     * @param file El archivo que se quiere guardar.
     * @return Un mensaje que indica que el archivo se guardó correctamente.
     */
    public String saveFileOnDb(File file) {
        repo.save(file);
        System.out.println("Fichero guardado correctamente");
        return "Fichero guardado correctamente";
    }

    /**
     * Obtiene el espacio de almacenamiento utilizado por un usuario.
     *
     * @param userId El ID del usuario cuyo espacio de almacenamiento se quiere
     *               obtener.
     * @return El espacio de almacenamiento utilizado por el usuario.
     */
    public Long getStorage(Long userId) {
        long totalSpaceUsed = 0;
        List<Long> totalStorageUsed = repo.findFileSizesByOwner(userId);

        for (Long fileSize : totalStorageUsed) {
            totalSpaceUsed += fileSize;
        }

        return totalSpaceUsed;
    }

    /**
     * Cuenta los archivos subidos en la última semana y los agrupa por fecha.
     *
     * @return Una lista de conteos de archivos agrupados por fecha.
     */
    public List<FileCountByDate> countFilesFromLastWeekGrouped() {
        Calendar calendar = Calendar.getInstance();

        // Ajusta la fecha al final del día actual
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        Date endDate = calendar.getTime();

        calendar.add(Calendar.DATE, -7); // -7 días
        // Ajusta la fecha al inicio del día
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date startDate = calendar.getTime();

        List<Object[]> results = repo.countFilesByDateRangeGrouped(startDate, endDate);
        ArrayList<FileCountByDate> fileCounts = new ArrayList<>();

        for (Object[] result : results) {
            Date date = (Date) result[0];
            Long count = (Long) result[1];
            FileCountByDate fileCountByDate = new FileCountByDate(date, count);
            fileCounts.add(fileCountByDate);
        }

        return fileCounts;
    }

    /**
     * Cuenta los archivos subidos por un propietario en la última semana y los
     * agrupa por fecha.
     *
     * @param ownerId El ID del propietario cuyos archivos se quieren contar.
     * @return Una lista de conteos de archivos agrupados por fecha.
     */
    public List<FileCountByDate> countFilesForOwnerFromLastWeekGrouped(Long ownerId) {
        Calendar calendar = Calendar.getInstance();

        // Ajusta la fecha al final del día actual
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        Date endDate = calendar.getTime();

        calendar.add(Calendar.DATE, -7); // -7 días
        // Ajusta la fecha al inicio del día
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date startDate = calendar.getTime();

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

    /**
     * Busca los archivos de un propietario.
     *
     * @param owner El ID del propietario cuyos archivos se quieren buscar.
     * @return Una lista de los archivos del propietario.
     */
    public List<File> findFilesByOwner(Long owner) {
        return repo.findFilesByOwner(owner);
    }

    /**
     * Busca archivos por su nombre.
     *
     * @param busqueda El nombre del archivo que se quiere buscar.
     * @return Una lista de los archivos con el nombre proporcionado.
     */
    public List<File> findFilesByFilename(String busqueda) {
        return repo.findFilesByFilename(busqueda);
    }

    /**
     * Actualiza la visibilidad de un archivo.
     *
     * @param fileId   El ID del archivo cuya visibilidad se quiere actualizar.
     * @param isPublic Un booleano que indica si el archivo debe ser público.
     */
    public void updateFileVisibility(Long fileId, Boolean isPublic) {
        File file = repo.findById(fileId).orElseThrow(() -> new RuntimeException("Archivo no encontrado"));
        file.setIspublic(isPublic);
        repo.save(file);
    }

    /**
     * Comprueba si un usuario es el propietario de un archivo.
     *
     * @param fileId El ID del archivo que se quiere comprobar.
     * @param userId El ID del usuario que se quiere comprobar.
     * @return Un booleano que indica si el usuario es el propietario del archivo.
     */
    public boolean checkFileOwnership(Long fileId, Long userId) {
        File file = repo.findById(fileId).orElseThrow(() -> new RuntimeException("Archivo no encontrado"));
        return file.getOwner().equals(userId);
    }

    /**
     * Elimina un archivo de la base de datos.
     *
     * @param fileId El ID del archivo que se quiere eliminar.
     */
    public void deleteFile(Long fileId) {
        repo.deleteById(fileId);
    }

    /**
     * Obtiene el nombre de un archivo de la base de datos.
     *
     * @param fileId El ID del archivo que se quiere obtener el nombre.
     */
    public String getFileById(Long fileId) {
        return repo.getFileNameById(fileId);
    }

    /**
     * Busca los archivos de un propietario y devuelve el nombre de usuario en lugar
     * del ID.
     *
     * @param owner El ID del propietario cuyos archivos se quieren buscar.
     * @return Una lista de los archivos del propietario con el nombre de usuario.
     */
    public List<File> findFilesByOwnerWithUsername(Long owner) {
        List<File> files = repo.findFilesByOwner(owner);
        for (File file : files) {
            String username = userService.getUsernameById(file.getOwner());
            file.setOwnerUsername(username);
        }
        return files;
    }

}
