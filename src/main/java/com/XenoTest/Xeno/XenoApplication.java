package com.XenoTest.Xeno;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
@EnableScheduling
@SpringBootApplication
public class XenoApplication {

	public static void main(String[] args) {
		SpringApplication.run(XenoApplication.class, args);
	}

}
