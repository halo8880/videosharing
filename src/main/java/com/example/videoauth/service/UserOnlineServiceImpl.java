package com.example.videoauth.service;

import com.example.videoauth.model.redis.OnlineUser;
import com.example.videoauth.repository.OnlineUserRepository;
import com.example.videoauth.security.jwt.JwtUtils;
import com.example.videoauth.util.TimeUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserOnlineServiceImpl implements UserOnlineService {
	@Autowired
	JwtUtils jwtUtils;
	@Autowired
	OnlineUserRepository onlineUserRepository;

	@Override
	public void updateLastSeen(HttpServletRequest request) {
		if (request.getRequestURI().equalsIgnoreCase("/ws")) {
			return;
		}
		var authen = SecurityContextHolder.getContext().getAuthentication();
		boolean isAuthenticated = authen!= null && authen.isAuthenticated();
		if (!isAuthenticated) {
			return;
		}
		String jwt = jwtUtils.parseJwt(request);
		if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
			LocalDateTime now = TimeUtil.now();
			String username = SecurityContextHolder.getContext().getAuthentication().getName();
			OnlineUser onlineUser = onlineUserRepository.findById(jwt)
					.orElse(new OnlineUser(jwt, username, now));
			onlineUser.setLastSeen(now);
			onlineUserRepository.save(onlineUser);
		}
	}
}
