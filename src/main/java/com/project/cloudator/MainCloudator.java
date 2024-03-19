package com.project.cloudator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
		LogWriter logwriter = new LogWriter();

		logwriter.startWriter("""
				     _____  _       ____   _    _  _____         _______  ____   _____
				    / ____|| |     / __ \\ | |  | ||  __ \\    /\\ |__   __|/ __ \\ |  __ \\
				   | |     | |    | |  | || |  | || |  | |  /  \\   | |  | |  | || |__) |
				   | |     | |    | |  | || |  | || |  | | / /\\ \\  | |  | |  | ||  _  /
				   | |____ | |____| |__| || |__| || |__| |/ ____ \\ | |  | |__| || | \\ \\
				    \\_____||______|\\____/  \\____/ |_____//_/    \\_\\|_|   \\____/ |_|  \\_\\
				""");

		// logwriter.startWriter("");
		SpringApplication.run(MainCloudator.class, args);
	}
}
