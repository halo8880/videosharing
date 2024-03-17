package com.example.videoauth.controller;

import com.example.videoauth.payload.request.ShareVideoRequest;
import com.example.videoauth.payload.response.ShareVideoResponse;
import com.example.videoauth.service.ShareVideoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/video-sharing")
public class ShareVideoController {
	@Autowired
	private ShareVideoService shareVideoService;

	@PostMapping
	public ShareVideoResponse shareVideo(@RequestBody  ShareVideoRequest shareVideoRequest) throws JsonProcessingException {
		return shareVideoService.shareVideo(shareVideoRequest);
	}
}
