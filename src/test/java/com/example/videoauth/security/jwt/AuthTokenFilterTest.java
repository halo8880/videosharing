package com.example.videoauth.security.jwt;

import com.example.videoauth.model.User;
import com.example.videoauth.security.services.UserDetailsImpl;
import com.example.videoauth.security.services.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AuthTokenFilterTest {

	@MockBean
	private JwtUtils jwtUtils;

	@MockBean
	private UserDetailsServiceImpl userDetailsService;

	@MockBean
	private HttpServletRequest request;

	@MockBean
	private HttpServletResponse response;

	@MockBean
	private FilterChain filterChain;

	private AuthTokenFilter authTokenFilter;

	@BeforeEach
	public void setup() {
		authTokenFilter = new AuthTokenFilter(jwtUtils, userDetailsService);
		// Mock behavior of jwtUtils and userDetailsService as needed
	}

	@Test
	public void testDoFilterInternal_ValidJwt() throws Exception {
		// Given
		String jwtToken = "valid_jwt_token";
		when(jwtUtils.parseJwt(request)).thenReturn(jwtToken);
		when(jwtUtils.validateJwtToken(jwtToken)).thenReturn(true);
		String username = "testuser";
		UserDetails userDetails = new UserDetailsImpl(1L, username, "email@email.com", "secretpassword",
				List.of(new SimpleGrantedAuthority("ROLE_USER")));
		when(jwtUtils.getUserNameFromJwtToken(jwtToken)).thenReturn(username);
		when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);

		// When
		authTokenFilter.doFilterInternal(request, response, filterChain);

		// Then
		verify(request, never()).getParameter(anyString());
		verify(filterChain).doFilter(request, response);
	}

	@Test
	public void testDoFilterInternal_InvalidJwt() throws Exception {
		// Given
		String jwtToken = "invalid_jwt_token";
		when(jwtUtils.parseJwt(request)).thenReturn(jwtToken);
		when(jwtUtils.validateJwtToken(jwtToken)).thenReturn(false);

		// When
		authTokenFilter.doFilterInternal(request, response, filterChain);

		// Then
		verify(request, never()).getParameter(anyString());
		verify(filterChain).doFilter(request, response);
	}

	@Test
	public void testDoFilterInternal_WsRequest() throws Exception {
		// Given
		String wsUri = "/ws";
		when(request.getRequestURI()).thenReturn(wsUri);
		when(request.getParameter("token")).thenReturn("ws_token");

		// When
		authTokenFilter.doFilterInternal(request, response, filterChain);

		// Then
		verify(jwtUtils, never()).parseJwt(any());
		verify(filterChain).doFilter(request, response);
	}
}