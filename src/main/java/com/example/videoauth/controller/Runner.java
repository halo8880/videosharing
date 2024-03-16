package com.example.videoauth.controller;

import com.example.videoauth.model.ERole;
import com.example.videoauth.model.Role;
import com.example.videoauth.model.User;
import com.example.videoauth.repository.RoleRepository;
import com.example.videoauth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class Runner implements CommandLineRunner {
	@Autowired
	RoleRepository roleRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	PasswordEncoder passwordEncoder;

	@Override
	public void run(String... args) throws Exception {
		User user = new User();
		user.setEmail("viet@gmail.com");
		user.setUsername("viet");
		user.setPassword(passwordEncoder.encode("vietpassword"));

		Role role = new Role();
		role.setName(ERole.ROLE_USER);
		roleRepository.save(role);
		user.setRoles(Set.of(role));
		userRepository.save(user);
	}
}
