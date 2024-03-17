package com.example.videoauth.service;

import com.example.videoauth.payload.request.ShareVideoRequest;
import com.example.videoauth.payload.response.ShareVideoResponse;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface ShareVideoService {
	ShareVideoResponse shareVideo(ShareVideoRequest shareVideoRequest) throws JsonProcessingException;
}
