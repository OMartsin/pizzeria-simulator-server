package com.example.pizzeria.event_listeners;

import com.example.pizzeria.events.*;
import com.example.pizzeria.mappers.CookingOrderMapper;
import com.example.pizzeria.mappers.PauseCookUpdateEventMapper;
import com.example.pizzeria.mappers.ServiceOrderUpdateEventMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
@Slf4j
@RequiredArgsConstructor
public class FileEventListener implements UpdateEventListener {
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
        var dto = serviceOrderUpdateEventMapper.toServiceOrderDto(event);
        log.info(dto.toString());
    }

    private void handlePausedCookUpdateEvent(PausedCookUpdateEvent event) {
        var dto = pauseCookUpdateEventMapper.toPauseCookDto(event);
        log.info(dto.toString());
    }

    private void handlePreCookingOrderUpdateEvent(PreCookingOrderUpdateEvent event) {
        var dto = cookingOrderMapper.toCookingOrderDto(event);
        log.info(dto.toString());
    }

    private void handlePostCookingOrderUpdateEvent(PostCookingOrderUpdateEvent event) {
        var dto = cookingOrderMapper.toCookingOrderDto(event);
        log.info(dto.toString());
    }
}
