package ru.an.pp33;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class Pp33Application {
	public static void main(String[] args) {
		SpringApplication.run(Pp33Application.class, args);
	}
}
