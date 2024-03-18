package com.example.videoauth.service;

import com.example.videoauth.model.User;
import com.example.videoauth.repository.UserRepository;
import com.example.videoauth.security.services.UserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserDetailsServiceImplTest {

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private UserDetailsServiceImpl userDetailsService;

	private User testUser;

	@BeforeEach
	public void setup() {
		testUser = new User();
		testUser.setUsername("testuser");
		testUser.setPassword("testpassword");

		// Stub userRepository.findByUsername() method to return the test user
		when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
	}

	@Test
	public void testLoadUserByUsername_Success() {
		// Call the method under test
		UserDetails userDetails = userDetailsService.loadUserByUsername("testuser");

		// Verify that userRepository.findByUsername() was called with the correct argument
		verify(userRepository).findByUsername("testuser");

		assertEquals(testUser.getUsername(), userDetails.getUsername());
		assertEquals(testUser.getPassword(), userDetails.getPassword());
	}

	@Test
	public void testLoadUserByUsername_UserNotFound() {
		// Stub userRepository.findByUsername() method to return an empty Optional
		when(userRepository.findByUsername("unknownuser")).thenReturn(Optional.empty());

		// Call the method under test with a non-existent username
		assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername("unknownuser"));
	}
}