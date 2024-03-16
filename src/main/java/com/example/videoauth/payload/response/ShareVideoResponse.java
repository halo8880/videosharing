package com.example.videoauth.payload.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ShareVideoResponse {
	private Long id;
	private String videoId;
	private String username;
	private LocalDateTime time;

	public ShareVideoResponse(Long id, String videoId, String username, LocalDateTime time) {
		this.id = id;
		this.videoId = videoId;
		this.username = username;
		this.time = time;
	}
}
