package com.example.videoauth.service;

import com.example.videoauth.model.SharedVideo;
import com.example.videoauth.payload.request.ShareVideoRequest;
import com.example.videoauth.payload.response.ShareVideoResponse;
import com.example.videoauth.repository.SharedVideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import static com.example.videoauth.util.TimeUtil.now;

@Service
public class ShareVideoServiceImpl implements ShareVideoService {
	@Autowired
	SharedVideoRepository sharedVideoRepository;

	@Override
	public ShareVideoResponse shareVideo(ShareVideoRequest shareVideoRequest) {
		UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		SharedVideo sharedVideo = new SharedVideo(user.getUsername(), shareVideoRequest.getVideoId());
		sharedVideo.setTime(now());
		sharedVideoRepository.save(sharedVideo);
		return new ShareVideoResponse(
				sharedVideo.getId(),
				sharedVideo.getVideoId(),
				sharedVideo.getUsername(),
				sharedVideo.getTime());
	}
}
