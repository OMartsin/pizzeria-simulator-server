package com.example.pizzeria.event_listeners;

import com.example.pizzeria.dto.*;
import com.example.pizzeria.events.CookingOrderUpdateEvent;
import com.example.pizzeria.events.PausedCookUpdateEvent;
import com.example.pizzeria.events.ServiceOrderUpdateEvent;
import com.example.pizzeria.events.UpdateEvent;
import com.example.pizzeria.managers.cashregister.CashRegister;
import com.example.pizzeria.models.Order;
import com.example.pizzeria.models.PizzaCookingState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@Component
public class NetworkEventListener implements UpdateEventListener{
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Override
    @EventListener
    public void update(UpdateEvent event) {
        if (event instanceof ServiceOrderUpdateEvent) {
            handleServiceOrderUpdateEvent((ServiceOrderUpdateEvent) event);
        } else if (event instanceof PausedCookUpdateEvent) {
            handlePausedCookUpdateEvent((PausedCookUpdateEvent) event);
        } else if (event instanceof CookingOrderUpdateEvent) {
            handleCookingOrderUpdateEvent((CookingOrderUpdateEvent) event);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unsupported UpdateEvent type");
        }
    }


    private void handleServiceOrderUpdateEvent(ServiceOrderUpdateEvent event) {
        String destination = "/topic/newOrder";
        CashRegister cashRegister = event.getCashRegister();
        Order order = event.getOrder();
        DinerDto dinerDto = new DinerDto(order.getDiner().getId(), order.getDiner().getName());
        List<OrderPizzaDto> orderPizzaDtos = order.getRecipes().stream()
                .map(recipe -> new OrderPizzaDto(recipe.getId(), order.getId(),
                        recipe.getId()))
                .toList();
        ServiceOrderDto dto = new ServiceOrderDto(
                Long.valueOf(order.getId()),
                Long.valueOf(cashRegister.getId()),
                order.getOrderTime(),
                orderPizzaDtos,
                dinerDto
                );
        messagingTemplate.convertAndSend(destination, dto);
    }

    private void handlePausedCookUpdateEvent(PausedCookUpdateEvent event) {
        String destination = "/topic/pausedCookUpdate";

        // Create a PauseCookDto with the cookId
        PauseCookDto dto = new PauseCookDto(event.getCook().getCookId());
        messagingTemplate.convertAndSend(destination, dto);
    }

    private void handleCookingOrderUpdateEvent(CookingOrderUpdateEvent event) {
        String destination = "/topic/cookingOrderUpdate";
        PizzaCookingState pizzaCookingState = event.getPizzaCookingState();
        Integer toppingId = pizzaCookingState.getCurrToppingIndex();
        Integer cookId = event.getCook().getCookId();
        Integer orderId = pizzaCookingState.getOrderId();

        // Create a CookingOrderDto with relevant information
        CookingOrderDto dto = new CookingOrderDto(pizzaCookingState, toppingId, cookId, orderId);

        messagingTemplate.convertAndSend(destination, dto);
    }
}
