package com.example.videoauth.service;

import jakarta.servlet.http.HttpServletRequest;

public interface UserOnlineService {

	void updateLastSeen(HttpServletRequest request);
}
