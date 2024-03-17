package com.example.videoauth.service;

import com.example.videoauth.model.SharedVideo;
import com.example.videoauth.model.UserSharedVideo;
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
	private MessageQueueService messageQueueService;
	@Autowired
	private SharedVideoRepository sharedVideoRepository;
	@Autowired
	private WebSocketService webSocketService;

	@Override
	public ShareVideoResponse shareVideo(ShareVideoRequest shareVideoRequest) {
		UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		SharedVideo sharedVideo = new SharedVideo(shareVideoRequest.getVideoId(), user.getUsername());
		sharedVideo.setTime(now());
		sharedVideoRepository.save(sharedVideo);
		var res = new ShareVideoResponse(
				sharedVideo.getId(),
				sharedVideo.getVideoId(),
				sharedVideo.getUsername(),
				sharedVideo.getTime());
		UserSharedVideo userSharedVideo = new UserSharedVideo(
				user.getUsername(),
				sharedVideo.getVideoId(),
				sharedVideo.getTime());
		messageQueueService.publishMessage(userSharedVideo);
		return res;
	}
}
