package com.example.videoauth.integration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestWebSocket {

	@Autowired
	private TestRestTemplate restTemplate;

	@LocalServerPort
	private int port;

	@Test
	public void testWebSocketConnection() throws Exception {
		WebSocketStompClient stompClient = new WebSocketStompClient(new SockJsClient(
				List.of(new WebSocketTransport(new StandardWebSocketClient()))));
		stompClient.setMessageConverter(new MappingJackson2MessageConverter());

		StompSession session = stompClient.connect("ws://localhost:" + port + "/ws", new StompSessionHandlerAdapter() {}).get();

		// Perform WebSocket interactions, such as sending and receiving messages
		session.send("/app/hello", "testWebSocketConnection");

		// Subscribe to a destination and verify received messages
		session.subscribe("/topic/greetings", new DefaultStompFrameHandler());

		// Perform assertions based on received messages or other WebSocket interactions
	}

	// Define custom StompFrameHandler to handle received messages
	class DefaultStompFrameHandler implements StompFrameHandler {
		@Override
		public Type getPayloadType(StompHeaders headers) {
			return byte[].class;
		}

		@Override
		public void handleFrame(StompHeaders headers, Object payload) {
			String message = new String((byte[]) payload);
			// Assert or process received message
		}
	}
}
