package com.example.videoauth.repository;

import com.example.videoauth.model.ERole;
import com.example.videoauth.model.Role;
import com.example.videoauth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
	Optional<Role> findByName(ERole name);
}
