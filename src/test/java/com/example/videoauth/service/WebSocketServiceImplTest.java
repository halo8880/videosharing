package com.example.videoauth.service;

import com.example.videoauth.model.redis.OnlineUser;
import com.example.videoauth.repository.OnlineUserRepository;
import com.example.videoauth.util.TimeUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class WebSocketServiceImplTest {

	@Mock
	private OnlineUserRepository onlineUserRepository;

	@Mock
	private SimpMessagingTemplate simpMessagingTemplate;
	private Long lastSeenThreshold = 60L;
	private WebSocketServiceImpl webSocketService;

	@BeforeEach
	public void setup() {
		webSocketService = new WebSocketServiceImpl(onlineUserRepository, simpMessagingTemplate, lastSeenThreshold);
	}

	@Test
	public void testNotifyUserIfOnline_UserOnline() {
		// Arrange
		String userToken = "userToken";
		String message = "Test message";
		OnlineUser onlineUser = new OnlineUser();
		onlineUser.setToken(userToken);
		onlineUser.setLastSeen(TimeUtil.now().minusMinutes(1));
		when(onlineUserRepository.findById(userToken)).thenReturn(Optional.of(onlineUser));

		// Act
		webSocketService.notifyUserIfOnline(userToken, message);

		// Assert
		verify(onlineUserRepository, times(1)).findById(userToken);
		verify(simpMessagingTemplate, times(1)).convertAndSend(eq("/topic/notify/" + userToken), eq(message));
	}

	@Test
	public void testNotifyUserIfOnline_UserOffline() {
		// Arrange
		String userToken = "userToken";
		String message = "Test message";
		when(onlineUserRepository.findById(userToken)).thenReturn(Optional.empty());

		// Act
		webSocketService.notifyUserIfOnline(userToken, message);

		// Assert
		verify(onlineUserRepository, times(1)).findById(userToken);
		verify(simpMessagingTemplate, never()).convertAndSend(anyString(), anyString());
	}


	@Test
	public void testGetOnlineUsersNotMe() {
		// Arrange
		String currentUser = "currentUser";
		OnlineUser onlineUser1 = new OnlineUser("user1", "token1", TimeUtil.now().minusSeconds(30));
		OnlineUser onlineUser2 = new OnlineUser("user2", "token2", TimeUtil.now().minusSeconds(lastSeenThreshold + 10));
		List<OnlineUser> onlineUsers = new ArrayList<>();
		onlineUsers.add(onlineUser1);
		onlineUsers.add(onlineUser2);
		when(onlineUserRepository.findAll()).thenReturn(onlineUsers);

		Authentication authentication = mock(Authentication.class);
		when(authentication.getName()).thenReturn(currentUser);

		SecurityContext securityContext = mock(SecurityContext.class);
		when(securityContext.getAuthentication()).thenReturn(authentication);

		SecurityContextHolder.setContext(securityContext);

		// Act
		Set<OnlineUser> result = webSocketService.getOnlineUsersNotMe();

		// Assert
		assertEquals(Collections.singleton(onlineUser1), result);

		// Verify interactions
		verify(onlineUserRepository, times(1)).findAll();
	}
}