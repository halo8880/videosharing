package com.example.videoauth.model.redis;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@RedisHash("UserSharedVideo")
@EqualsAndHashCode
@NoArgsConstructor
public class UserSharedVideo implements Serializable {
	private String username;
	private String videoId;
	private LocalDateTime time;

	public UserSharedVideo(String username, String videoId, LocalDateTime time) {
		this.username = username;
		this.videoId = videoId;
		this.time = time;
	}
}
