package com.project.cloudator.functions;

import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Component;

@Component
public class LogWriter {
    private String fileName = "server-log-" + getCurrentDate(0) + ".log";

    /**
     * Inicia el escritor de registros.
     * 
     * @param text el texto a escribir en el registro
     */
    public void startWriter() {
        try {
            String actualDate = getCurrentDate(1);
            String finalString = "\nStarted at [" + actualDate + "] \n";
            FileWriter logWriter = new FileWriter(fileName, true);
            logWriter.write(finalString);
            logWriter.close();
        } catch (Exception e) {
            System.err.println("Error al escribir en el archivo: " + e.getMessage());
        }
    }

    /**
     * Escribe un registro en el archivo de registro.
     * 
     * @param text el texto a escribir en el registro
     */
    public void writeLog(String text) {
        try {
            String actualDate = getCurrentDate(1);
            String finalString = "[LOG][" + actualDate + "] " + text + "\n";
            FileWriter logWriter = new FileWriter(fileName, true);
            logWriter.write(finalString);
            logWriter.close();
        } catch (Exception e) {
            System.err.println("Error al escribir en el archivo: " + e.getMessage());
        }
    }

    /**
     * Escribe un error en el archivo de registro.
     * 
     * @param text el texto del error a escribir en el registro
     */
    public void writeError(String text) {
        try {
            String actualDate = getCurrentDate(1);
            FileWriter logWriter = new FileWriter(fileName, true);
            logWriter.write("[ERROR][" + actualDate + "] " + text + "\n");
            logWriter.close();
        } catch (Exception e) {
            System.err.println("Error al escribir en el archivo: " + e.getMessage());
        }
    }

    /**
     * Obtiene la fecha actual en el formato especificado.
     * 
     * @param method el método para obtener el formato de fecha (0 para
     *               "yyyy-MM-dd", 1 para "yyyy-MM-dd HH:mm:ss")
     * @return la fecha actual en el formato especificado
     */
    private String getCurrentDate(Integer method) {
        Date date = new Date();
        SimpleDateFormat dateFormat = null;

        if (method == 0) {
            dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        } else if (method == 1) {
            dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }

        return dateFormat.format(date);
    }
}
