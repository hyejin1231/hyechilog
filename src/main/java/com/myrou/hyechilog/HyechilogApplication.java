package com.myrou.hyechilog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.myrou.hyechilog.config.AppConfig;

@EnableConfigurationProperties(AppConfig.class)
@SpringBootApplication
public class HyechilogApplication {

	public static void main(String[] args) {
		SpringApplication.run(HyechilogApplication.class, args);
	}

}
