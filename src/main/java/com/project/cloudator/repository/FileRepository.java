package com.project.cloudator.repository;

import com.project.cloudator.entity.File;

import java.util.List;
import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

public interface FileRepository extends JpaRepository<File, Long> {

    /**
     * Cuenta los archivos de un propietario en un rango de fechas y los agrupa por
     * fecha.
     *
     * @param startDate La fecha de inicio del rango.
     * @param endDate   La fecha de fin del rango.
     * @param ownerId   El ID del propietario de los archivos.
     * @return Una lista de arreglos de objetos, donde cada arreglo contiene una
     *         fecha y la cantidad de archivos de esa fecha.
     */
    @Query("SELECT FUNCTION('date', f.filedate) AS date, COUNT(f) AS count FROM File f WHERE f.filedate BETWEEN :startDate AND :endDate AND f.owner = :ownerId GROUP BY FUNCTION('date', f.filedate)")
    List<Object[]> countFilesByOwnerAndDateRangeGrouped(@Param("startDate") Date startDate,
            @Param("endDate") Date endDate, @Param("ownerId") Long ownerId);

    /**
     * Cuenta los archivos en un rango de fechas y los agrupa por fecha.
     *
     * @param startDate La fecha de inicio del rango.
     * @param endDate   La fecha de fin del rango.
     * @return Una lista de arreglos de objetos, donde cada arreglo contiene una
     *         fecha y la cantidad de archivos de esa fecha.
     */
    @Query("SELECT FUNCTION('date', f.filedate) AS date, COUNT(f) AS count FROM File f WHERE f.filedate BETWEEN :startDate AND :endDate GROUP BY FUNCTION('date', f.filedate)")
    List<Object[]> countFilesByDateRangeGrouped(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    /**
     * Encuentra los archivos de un propietario.
     *
     * @param owner El ID del propietario de los archivos.
     * @return Una lista de archivos del propietario.
     */
    @Query("SELECT f FROM File f WHERE f.owner = :owner")
    List<File> findFilesByOwner(Long owner);

    /**
     * Encuentra los archivos por su nombre de archivo.
     *
     * @param filename El nombre de archivo que se quiere buscar.
     * @return Una lista de archivos que coinciden con el nombre de archivo
     *         proporcionado.
     */
    @Query(value = "SELECT * FROM files WHERE filename LIKE %:busqueda% AND ispublic = 1", nativeQuery = true)
    public List<File> findFilesByFilename(@Param("busqueda") String filename);

    /**
     * Encuentra los tamaños de los archivos de un propietario.
     *
     * @param ownerId El ID del propietario de los archivos.
     * @return Una lista de los tamaños de los archivos del propietario.
     */
    @Query("SELECT f.filesize FROM File f WHERE f.owner = :ownerId")
    List<Long> findFileSizesByOwner(@Param("ownerId") Long ownerId);

    /**
     * Actualiza la visibilidad de un archivo.
     *
     * @param fileId   El ID del archivo cuya visibilidad se quiere actualizar.
     * @param isPublic Un booleano que indica si el archivo debe ser público o no.
     */
    @Modifying
    @Query("UPDATE File f SET f.ispublic = :isPublic WHERE f.id = :fileId")
    void updateFileVisibility(@Param("fileId") Long fileId, @Param("isPublic") Boolean isPublic);

    /**
     * Elimina un archivo.
     *
     * @param fileId El ID del archivo a eliminar.
     */
    @Transactional
    @Modifying
    @Query("DELETE FROM File f WHERE f.id = :fileId")
    void deleteFile(@Param("fileId") Long fileId);

}
