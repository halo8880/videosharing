package com.example.videoauth.service;

import com.example.videoauth.model.ERole;
import com.example.videoauth.model.Role;
import com.example.videoauth.model.SharedVideo;
import com.example.videoauth.payload.request.ShareVideoRequest;
import com.example.videoauth.payload.response.ShareVideoResponse;
import com.example.videoauth.repository.SharedVideoRepository;
import com.example.videoauth.security.services.UserDetailsImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.List;

import static com.example.videoauth.util.TimeUtil.now;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ShareVideoServiceImplTest {
	@Mock
	private SharedVideoRepository sharedVideoRepository;

	@Mock
	private MessageQueueService messageQueueService;

	@InjectMocks
	private ShareVideoServiceImpl shareVideoService;

	@Test
	public void testShareVideo_Success() {
		// Arrange
		ShareVideoRequest request = new ShareVideoRequest();
		request.setVideoId("videoId");
		Authentication authentication = mock(Authentication.class);
		Role role = new Role();
		role.setName(ERole.ROLE_USER);
		UserDetails userDetails = new UserDetailsImpl(1L, "username", "email@email.com", "password", List.of(new SimpleGrantedAuthority(role.getName().name())));
		when(authentication.getPrincipal()).thenReturn(userDetails);
		SecurityContextHolder.getContext().setAuthentication(authentication);

		when(sharedVideoRepository.existsByUsernameAndVideoId("username", "videoId")).thenReturn(false);
		when(sharedVideoRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

		// Act
		ShareVideoResponse response = shareVideoService.shareVideo(request);

		// Assert
		assertNotNull(response);
		assertEquals("videoId", response.getVideoId());
		assertEquals("username", response.getUsername());
		assertNotNull(response.getTime());

		// Verify interactions
		verify(sharedVideoRepository, times(1)).existsByUsernameAndVideoId("username", "videoId");
		verify(sharedVideoRepository, times(1)).save(any());
		verify(messageQueueService, times(1)).publishMessage(any());
	}

	@Test
	public void testShareVideo_VideoAlreadyShared() {
		// Arrange
		ShareVideoRequest request = new ShareVideoRequest();
		request.setVideoId("videoId");
		Authentication authentication = mock(Authentication.class);
		Role role = new Role();
		role.setName(ERole.ROLE_USER);
		UserDetails userDetails = new UserDetailsImpl(1L, "username", "email@email.com", "password", List.of(new SimpleGrantedAuthority(role.getName().name())));
		when(authentication.getPrincipal()).thenReturn(userDetails);
		SecurityContextHolder.getContext().setAuthentication(authentication);

		when(sharedVideoRepository.existsByUsernameAndVideoId("username", "videoId")).thenReturn(true);

		// Act and assert
		assertThrows(RuntimeException.class, () -> shareVideoService.shareVideo(request));

		// Verify interactions
		verify(sharedVideoRepository, times(1)).existsByUsernameAndVideoId("username", "videoId");
		verify(sharedVideoRepository, never()).save(any());
		verify(messageQueueService, never()).publishMessage(any());
	}

	@Test
	public void testGetSharedVideos() {
		// Arrange
		SharedVideo video1 = new SharedVideo("video1", "user1");
		video1.setId(1L);
		video1.setTime(now().minusSeconds(5));
		SharedVideo video2 = new SharedVideo("video2", "user2");
		video2.setId(2L);
		video2.setTime(now().minusSeconds(20));
		when(sharedVideoRepository.findAll()).thenReturn(Arrays.asList(video1, video2));

		// Act
		List<ShareVideoResponse> responses = shareVideoService.getSharedVideos();

		// Assert
		assertEquals(2, responses.size());
		assertEquals(1L, responses.get(0).getId());
		assertEquals("video1", responses.get(0).getVideoId());
		assertEquals("user1", responses.get(0).getUsername());
		assertEquals(video1.getTime(), responses.get(0).getTime());

		assertEquals(2L, responses.get(1).getId());
		assertEquals("video2", responses.get(1).getVideoId());
		assertEquals("user2", responses.get(1).getUsername());
		assertEquals(video2.getTime(), responses.get(1).getTime());

		// Verify interactions
		verify(sharedVideoRepository, times(1)).findAll();
	}
}
