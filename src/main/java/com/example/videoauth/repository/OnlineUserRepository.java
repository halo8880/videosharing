package com.example.videoauth.repository;

import com.example.videoauth.model.OnlineUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OnlineUserRepository extends CrudRepository<OnlineUser, String> {
}
