package com.example.videoauth.controller;

import com.example.videoauth.payload.request.ShareVideoRequest;
import com.example.videoauth.payload.response.ShareVideoResponse;
import com.example.videoauth.service.ShareVideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/video-sharing")
public class ShareVideoController {
	@Autowired
	private ShareVideoService shareVideoService;

	@PostMapping
	public ShareVideoResponse shareVideo(ShareVideoRequest shareVideoRequest) {
		return shareVideoService.shareVideo(shareVideoRequest);
	}
}