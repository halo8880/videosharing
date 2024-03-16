package com.example.videoauth.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode
public class UserSharedVideo implements Serializable {
	private String username;
	private String videoId;
}
