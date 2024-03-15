package com.example.videoauth.controller;

import com.example.videoauth.config.CustomUserDetailsService;
import com.example.videoauth.service.JwtTokenProvider;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

	private final AuthenticationManager authenticationManager;
	private final JwtTokenProvider jwtTokenProvider;
	private final CustomUserDetailsService customUserDetailsService;

	public AuthController(AuthenticationManager authenticationManager,
						  JwtTokenProvider jwtTokenProvider,
						  CustomUserDetailsService customUserDetailsService) {
		this.authenticationManager = authenticationManager;
		this.jwtTokenProvider = jwtTokenProvider;
		this.customUserDetailsService = customUserDetailsService;
	}

	@PostMapping("/api/authenticate")
	public ResponseEntity<?> authenticate(@RequestBody AuthRequest request) {
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
			);
			UserDetails userDetails = customUserDetailsService.loadUserByUsername(request.getUsername());
			String token = jwtTokenProvider.generateToken(userDetails);
			return ResponseEntity.ok(new AuthResponse(token));
		} catch (AuthenticationException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}
}

@Data
class AuthRequest {
	private String username;
	private String password;

	// Getters and setters
}

@Data
@AllArgsConstructor
class AuthResponse {
	private String token;

	// Constructors, getters, and setters
}