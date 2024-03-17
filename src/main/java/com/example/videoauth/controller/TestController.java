package com.example.videoauth.controller;

import com.example.videoauth.repository.OnlineUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/viet")
public class TestController {
	@Autowired
	RedisTemplate<String, String> redisTemplate;
	@Autowired
	OnlineUserRepository onlineUserRepository;

	@GetMapping
	public String getVideos() {
//		redisTemplate.
//		onlineUserRepository.save(new OnlineUser(
//				"tonen",
//				LocalDateTime.now(),
//				""
//		));
		return "All videos";
	}
}
