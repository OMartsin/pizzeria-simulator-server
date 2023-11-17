package com.example.pizzeria.event_listeners;

import com.example.pizzeria.events.UpdateEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class NetworkEventListener implements UpdateEventListener{

    @Override
    @EventListener
    public void update(UpdateEvent event) {

    }
}
