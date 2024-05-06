package com.project.cloudator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.SpringVersion;

import com.project.cloudator.functions.LogWriter;

/**
 * 
 * @author Eduardo Núñez
 * @author Marc Pedreño
 * @author Hector Estrada
 */
@SpringBootApplication
public class MainCloudator {

	public static void main(String[] args) {
		System.out.println(SpringVersion.getVersion());
		LogWriter logwriter = new LogWriter();
		logwriter.startWriter();
		SpringApplication.run(MainCloudator.class, args);
	}
}
