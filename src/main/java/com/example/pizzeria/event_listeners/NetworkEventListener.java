package com.example.pizzeria.event_listeners;

import com.example.pizzeria.events.*;
import com.example.pizzeria.mappers.CookingOrderMapper;
import com.example.pizzeria.mappers.PauseCookUpdateEventMapper;
import com.example.pizzeria.mappers.ServiceOrderUpdateEventMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
@RequiredArgsConstructor
public class NetworkEventListener implements UpdateEventListener{
    private final SimpMessagingTemplate messagingTemplate;
    private final ServiceOrderUpdateEventMapper serviceOrderUpdateEventMapper;
    private final PauseCookUpdateEventMapper pauseCookUpdateEventMapper;
    private final CookingOrderMapper cookingOrderMapper;

    @Override
    @EventListener
    public void update(UpdateEvent event) {
        if (event instanceof ServiceOrderUpdateEvent) {
            handleServiceOrderUpdateEvent((ServiceOrderUpdateEvent) event);
        } else if (event instanceof PausedCookUpdateEvent) {
            handlePausedCookUpdateEvent((PausedCookUpdateEvent) event);
        } else if (event instanceof PostCookingOrderUpdateEvent) {
            handlePostCookingOrderUpdateEvent((PostCookingOrderUpdateEvent) event);
        } else if (event instanceof PreCookingOrderUpdateEvent) {
            handlePreCookingOrderUpdateEvent((PreCookingOrderUpdateEvent) event);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unsupported UpdateEvent type");
        }
    }


    private void handleServiceOrderUpdateEvent(ServiceOrderUpdateEvent event) {
        String destination = "/topic/newOrder";
        var dto = serviceOrderUpdateEventMapper.toServiceOrderDto(event);

        messagingTemplate.convertAndSend(destination, dto);
    }

    private void handlePausedCookUpdateEvent(PausedCookUpdateEvent event) {
        String destination = "/topic/pausedCookUpdate";
        var dto = pauseCookUpdateEventMapper.toPauseCookDto(event);

        messagingTemplate.convertAndSend(destination, dto);
    }

    private void handlePreCookingOrderUpdateEvent(PreCookingOrderUpdateEvent event) {
        String destination = "/topic/cookingOrderUpdate";
        var dto = cookingOrderMapper.toCookingOrderDto(event);

        messagingTemplate.convertAndSend(destination, dto);
    }

    private void handlePostCookingOrderUpdateEvent(PostCookingOrderUpdateEvent event) {
        String destination = "/topic/cookingOrderUpdate";

        var dto = cookingOrderMapper.toCookingOrderDto(event);
        messagingTemplate.convertAndSend(destination, dto);
    }
}
