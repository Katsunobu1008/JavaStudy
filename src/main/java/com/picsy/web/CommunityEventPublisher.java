package com.picsy.web;

import com.picsy.domain.CommunityState;
import com.picsy.web.dto.CommunitySnapshotResponse;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class CommunityEventPublisher {

    private final SimpMessagingTemplate messagingTemplate;
    private final CommunityMapper mapper;

    public CommunityEventPublisher(SimpMessagingTemplate messagingTemplate, CommunityMapper mapper) {
        this.messagingTemplate = messagingTemplate;
        this.mapper = mapper;
    }

    public void publishState(CommunityState state) {
        CommunitySnapshotResponse payload = mapper.toResponse(state);
        messagingTemplate.convertAndSend("/topic/community", payload);
    }
}
