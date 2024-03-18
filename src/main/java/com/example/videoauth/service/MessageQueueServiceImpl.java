package com.example.videoauth.service;

import com.example.videoauth.model.UserSharedVideo;
import com.example.videoauth.repository.OnlineUserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RPatternTopic;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MessageQueueServiceImpl implements MessageQueueService {
	//	@Autowired
	private WebSocketService webSocketService;
	//	@Autowired
	private RedissonClient redissonClient;
	private ObjectMapper objectMapper;
	private OnlineUserRepository onlineUserRepository;

	public MessageQueueServiceImpl(WebSocketService webSocketService,
								   RedissonClient redissonClient,
								   OnlineUserRepository onlineUserRepository) {
		this.webSocketService = webSocketService;
		this.redissonClient = redissonClient;
		this.onlineUserRepository = onlineUserRepository;

		objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
	}

	@Override
//	@Async
	public void publishMessage(UserSharedVideo message) {
		webSocketService.getOnlineUsersNotMe().stream().parallel().forEach(onlineUser -> {
			String topicName = notificationTopicName(onlineUser.getToken());
			RTopic topic = redissonClient.getTopic(topicName);
			if (topic.countListeners() == 0) {
				topic.addListener(UserSharedVideo.class, (channel, msg) -> {
					String jwt = channel.toString().split(":")[1];
					log.info("addListener Notifying user: {}", jwt);
					try {
						String json = objectMapper.writeValueAsString(msg);
						webSocketService.notifyUserIfOnline(jwt, json);
					} catch (JsonProcessingException e) {
						throw new RuntimeException(e);
					}
				});
			}
			topic.publish(message);

		});
	}

	@Override
//	@Async
	public void subscribeToNotifications() {
//		RPatternTopic notificationsTopic = redissonClient.getPatternTopic("notifications:*");
//		notificationsTopic.removeAllListeners();
//		notificationsTopic.addListener(UserSharedVideo.class, (pattern, channel, msg) -> {
//			String jwt = channel.toString().split(":")[1];
//			try {
//				String json = objectMapper.writeValueAsString(msg);
//				webSocketService.notifyUserIfOnline(jwt, json);
//			} catch (JsonProcessingException e) {
//				throw new RuntimeException(e);
//			}
//
//		});
	}

	private static String notificationTopicName(String originName) {
		return "notifications:" + originName;
	}
}
