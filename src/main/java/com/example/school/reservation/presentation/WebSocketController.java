package com.example.school.reservation.presentation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
public class WebSocketController {

    @Autowired
    private SimpMessagingTemplate template;

    @MessageMapping("/request/alert")
    public void requestAlert(String message) {
        // 클라이언트로부터 받은 메시지를 처리하는 로직
        log.info("Received alert request: {}", message);
        // 필요한 경우 여기에서 클라이언트로 메시지를 다시 보내는 코드를 작성
    }
}
