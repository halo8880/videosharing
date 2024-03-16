package com.example.videoauth.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "shared_video")
@Data
@NoArgsConstructor
public class SharedVideo {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String videoId;
	private String username;
	private LocalDateTime time;

	public SharedVideo(String videoId, String username) {
		this.videoId = videoId;
		this.username = username;
	}
}
