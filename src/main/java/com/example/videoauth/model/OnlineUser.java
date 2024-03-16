package com.example.videoauth.model;

import org.springframework.data.annotation.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@RedisHash("onlineUser")
public class OnlineUser {
	@Id
	String token;
	LocalDateTime lastSeen;
	String sharedVideoId;

	public OnlineUser(String token, LocalDateTime lastSeen, String sharedVideoId) {
		this.token = token;
		this.lastSeen = lastSeen;
		this.sharedVideoId = sharedVideoId;
	}
}
