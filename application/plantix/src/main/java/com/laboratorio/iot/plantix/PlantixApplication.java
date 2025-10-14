package com.laboratorio.iot.plantix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PlantixApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlantixApplication.class, args);
	}

}
