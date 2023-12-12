package com.example.registrationlogindemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.registrationlogindemo.functions.LogWriter;

@SpringBootApplication
public class RegistrationLoginDemoApplication {

	public static void main(String[] args) {
		LogWriter logwriter = new LogWriter();
		/* 
		logwriter.startWriter("""
				         _____            _             ____              _
				        / ____|          (_)           |  _ \\            | |
				       | (___  _ __  _ __ _ _ __   __ _| |_) | ___   ___ | |_
				        \\___ \\| '_ \\| '__| | '_ \\ / _` |  _ < / _ \\ / _ \\| __|
				       ____ ) | |_) | |  | | | | | (_| | |_) | (_) | (_) | |_
				       |_____/| .__/|_|  |_|_| |_|\\__, |____/ \\___/ \\___/ \\__|
				              | |                  __/ |
				              |_|                 |___/
				""");
				*/
		logwriter.startWriter("");
		SpringApplication.run(RegistrationLoginDemoApplication.class, args);
	}

}
