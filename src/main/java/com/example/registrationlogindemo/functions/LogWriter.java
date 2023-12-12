package com.example.registrationlogindemo.functions;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Component;

@Component
public class LogWriter {
    private String fileName = "server-log-" + getCurrentDate(0) + ".log";

    public void startWriter(String text){
        try {
            String actualDate = getCurrentDate(1);
            String finalString = text + "\nStarted at [" + actualDate + "] \n";
            FileWriter logWriter = new FileWriter(fileName, true);
            logWriter.write(finalString);
            logWriter.close();
        } catch (Exception e) {
            System.err.println("Error al escribir en el archivo: " + e.getMessage());
        }
    }

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
