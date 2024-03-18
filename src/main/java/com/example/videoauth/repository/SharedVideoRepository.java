package com.example.videoauth.repository;

import com.example.videoauth.model.SharedVideo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SharedVideoRepository extends JpaRepository<SharedVideo, Integer> {
	boolean existsByUsernameAndVideoId(String username, String videoId);
}
