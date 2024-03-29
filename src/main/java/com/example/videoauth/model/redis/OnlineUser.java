package com.example.videoauth.model.redis;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@RedisHash(value = "OnlineUser")
public class OnlineUser {
	@Id
	String token;
	String username;
	LocalDateTime lastSeen;
//	Set<UserSharedVideo> sharedVideoId = new HashSet<>();

	public OnlineUser(String token, String username, LocalDateTime lastSeen) {
		this.token = token;
		this.lastSeen = lastSeen;
		this.username = username;
	}

//	public OnlineUser(String token, LocalDateTime lastSeen, Set<UserSharedVideo> sharedVideoId) {
//		this.token = token;
//		this.lastSeen = lastSeen;
//		this.sharedVideoId = sharedVideoId;
//	}
}
