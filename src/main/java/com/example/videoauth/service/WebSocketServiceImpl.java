package com.example.videoauth.service;

import com.example.videoauth.model.redis.OnlineUser;
import com.example.videoauth.repository.OnlineUserRepository;
import com.example.videoauth.util.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class WebSocketServiceImpl implements WebSocketService {
	@Autowired
	private OnlineUserRepository onlineUserRepository;
	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;
	@Value("${app.lastSeenThresholdInSeconds}")
	private Long lastSeenThreshold;

	@Override
	@Async
	public void notifyUserIfOnline(String userToken, String message) {
		onlineUserRepository.findById(userToken).ifPresent(onlineUser -> {
			if (isUserOnline(onlineUser)) {
				log.info("notifyUserIfOnline Notifying user: {}", onlineUser.getToken());
				simpMessagingTemplate.convertAndSend("/topic/notify/" + onlineUser.getToken(), message);
			}
		});
	}

	@Override
	public Set<OnlineUser> getOnlineUsersNotMe() {
		String crrUsername = SecurityContextHolder.getContext().getAuthentication().getName();
		return onlineUserRepository.findAll().
				stream()
				.filter(onlineUser ->
						!crrUsername.equalsIgnoreCase(onlineUser.getUsername()) &&
								isUserOnline(onlineUser)
				).collect(Collectors.toSet());
	}

	private boolean isUserOnline(OnlineUser onlineUser) {
		return Duration.between(onlineUser.getLastSeen(), TimeUtil.now()).getSeconds() <= lastSeenThreshold;
	}
}
