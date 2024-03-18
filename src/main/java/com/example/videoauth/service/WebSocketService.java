package com.example.videoauth.service;

import com.example.videoauth.model.redis.OnlineUser;
import org.springframework.scheduling.annotation.Async;

import java.util.Set;

public interface WebSocketService {

	@Async
	void notifyUserIfOnline(String userToken, String message);

	Set<OnlineUser> getOnlineUsersNotMe();
}
