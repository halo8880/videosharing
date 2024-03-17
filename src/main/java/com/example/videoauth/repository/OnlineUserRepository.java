package com.example.videoauth.repository;

import com.example.videoauth.model.redis.OnlineUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OnlineUserRepository extends CrudRepository<OnlineUser, String> {
}
