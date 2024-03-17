package com.example.videoauth.service;

import com.example.videoauth.model.redis.OnlineUser;
import com.example.videoauth.repository.OnlineUserRepository;
import com.example.videoauth.security.jwt.JwtUtils;
import com.example.videoauth.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
public class WebSocketServiceImpl extends TextWebSocketHandler implements WebSocketService {
	@Autowired
	private JwtUtils jwtUtils;
	@Autowired
	private OnlineUserRepository onlineUserRepository;
	@Autowired
	private UserDetailsService userDetailsService;
	@Value("${app.lastSeenThresholdInSeconds}")
	private Long lastSeenThreshold;

	private Map<String, WebSocketSession> connectedUsers = new ConcurrentHashMap<>();

	@Override
	public void afterConnectionEstablished(WebSocketSession session) {
		String jwt = extractTokenFromHeader(session.getHandshakeHeaders().get("Authorization").get(0));
		if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
			String username = jwtUtils.getUserNameFromJwtToken(jwt);
			UserDetails userDetails = userDetailsService.loadUserByUsername(username);
			connectedUsers.put(jwt, session);
		}
	}

	@Override
	public void notifyOnlineUsers(String message) {

		var onlineUsers = (List<OnlineUser>) onlineUserRepository.findAll();
		Set<WebSocketSession> notifySessions = onlineUsers.stream().filter(onlineUser ->
				isUserOnline(onlineUser) && connectedUsers.containsKey(onlineUser.getToken())
		).map(onlineUser ->
				connectedUsers.get(onlineUser.getToken())).collect(Collectors.toSet());
		Executor executor = Executors.newFixedThreadPool(10);
		for (WebSocketSession session : notifySessions) {
			executor.execute(() -> {
				try {
					session.sendMessage(new TextMessage(message));
				} catch (IOException e) {
					// Handle exception
				}
			});
		}
	}

	private boolean isUserOnline(OnlineUser onlineUser) {
		return Duration.between(onlineUser.getLastSeen(), TimeUtil.now()).getSeconds() > lastSeenThreshold;
	}

	private String extractTokenFromHeader(String header) {
		if (header != null && header.startsWith("Bearer ")) {
			return header.substring(7);
		}
		return null;
	}
}
