package com.example.videoauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class VideoAuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(VideoAuthApplication.class, args);
	}

}
