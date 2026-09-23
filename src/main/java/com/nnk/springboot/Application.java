package com.nnk.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Point d'entrée de l'application Poseidon
 */
@SpringBootApplication
public class Application {

	/**
	 * Lance l'application Spring Boot.
	 *
	 * @param args arguments de la ligne de commande, transmis à Spring Boot
	 */
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
