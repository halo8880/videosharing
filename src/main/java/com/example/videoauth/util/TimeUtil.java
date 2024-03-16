package com.example.videoauth.util;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class TimeUtil {
	private TimeUtil() {
	}

	public static LocalDateTime now() {
		return LocalDateTime.now(ZoneId.of("UTC"));
	}
}
