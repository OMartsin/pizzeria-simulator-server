package com.example.pizzeria.event_listeners;

import com.example.pizzeria.events.ServiceOrderUpdateEvent;
import com.example.pizzeria.events.UpdateEvent;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class FileEventListener implements UpdateEventListener {
    @Override
    @EventListener
    public void update(UpdateEvent event) {
        // todo: implement once the mappers are done
        log.info("I caught some event!");
    }
}
