package com.example.videoauth.service;

import com.example.videoauth.model.UserSharedVideo;

public interface MessageQueueService {
	void publishMessage(UserSharedVideo message);

	void subscribeToNotifications();
}
