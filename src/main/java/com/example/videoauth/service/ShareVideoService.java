package com.example.videoauth.service;

import com.example.videoauth.payload.request.ShareVideoRequest;
import com.example.videoauth.payload.response.ShareVideoResponse;

public interface ShareVideoService {
	ShareVideoResponse shareVideo(ShareVideoRequest shareVideoRequest);
}
