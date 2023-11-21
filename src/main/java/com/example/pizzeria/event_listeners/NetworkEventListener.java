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
import java.util.stream.Collectors;


@Component
public class NetworkEventListener implements UpdateEventListener{
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Override
    @EventListener
    public void update(UpdateEvent event) {
        switch (event) {
            case ServiceOrderUpdateEvent e ->  {
                handleServiceOrderUpdateEvent((ServiceOrderUpdateEvent) e);
            }
            case PausedCookUpdateEvent e -> {
                handlePausedCookUpdateEvent((PausedCookUpdateEvent) e);
            }
            case CookingOrderUpdateEvent e -> {
                handleCookingOrderUpdateEvent((CookingOrderUpdateEvent) e);
            }
            default -> {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unsupported UpdateEvent type");
            }
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
                order.getId(),
                cashRegister.getId(),
                order.getCreatedAt(),
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
        int toppingId = pizzaCookingState.getCurrToppingIndex();
        int cookId = event.getCook().getCookId();
        int orderId = pizzaCookingState.getOrderId();

        // Create a CookingOrderDto with relevant information
        CookingOrderDto dto = new CookingOrderDto(pizzaCookingState, toppingId, cookId, orderId);

        messagingTemplate.convertAndSend(destination, dto);
    }
}
