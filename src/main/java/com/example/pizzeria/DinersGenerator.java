package com.example.pizzeria;

import com.example.pizzeria.managers.cashregister.CashRegisterManager;
import com.example.pizzeria.config.PizzeriaConfig;
import com.example.pizzeria.models.Diner;
import com.example.pizzeria.models.Order;
import com.example.pizzeria.models.OrderedItem;
import com.example.pizzeria.models.Recipe;
import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
public class DinersGenerator {
    private PizzeriaConfig pizzeriaConfig;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private ScheduledFuture<?> taskHandle;
    private final CashRegisterManager cashRegisterManager;
    private final int maxPizzas = 5; // більше 5 в 1 руки не давати

    public void setDinerArrivalConfig(PizzeriaConfig pizzeriaConfig) {
        this.pizzeriaConfig = pizzeriaConfig;
    }

    public void start() throws IllegalStateException {
        checkArrivalConfigNotNull();

        Runnable generateDiners = () -> {
            System.out.println(pizzeriaConfig.getDinerArrivalConfig().getQuantity());
            for (int i = 0; i < pizzeriaConfig.getDinerArrivalConfig().getQuantity(); ++i) {
                Diner newDiner = generateDinner(pizzeriaConfig.getMenu());
                cashRegisterManager.acceptDinner(newDiner);
            }

        };
        taskHandle = scheduler.scheduleAtFixedRate(generateDiners,
                0,
                pizzeriaConfig.getDinerArrivalConfig().getFrequency().value,
                TimeUnit.SECONDS);

    }

    public void pause() throws IllegalStateException {
        checkArrivalConfigNotNull();
        taskHandle.cancel(false);
    }

    public void terminate() throws IllegalStateException {
        taskHandle.cancel(false);
    }

    private void checkArrivalConfigNotNull() throws IllegalStateException {
        if (pizzeriaConfig.getDinerArrivalConfig() == null) {
            throw new IllegalStateException("DinerArrivalConfig is not initialized");
        }
    }

    private Diner generateDinner(List<Recipe> menu) {

        List<Recipe> randomPizzas = getRandomPizzas(menu);

        Faker faker = new Faker();
        return new Diner(faker.name().fullName(), new Order(null,
                randomPizzas.stream().map(OrderedItem::new).collect(Collectors.toList()), null));
    }

    private List<Recipe> getRandomPizzas(List<Recipe> menu) {
        List<Recipe> tempList = new ArrayList<>(menu);
        Collections.shuffle(tempList, new Random());

        int numberOfPizzas = (int) (Math.random() * maxPizzas) + 1;

        return IntStream.range(0, numberOfPizzas)
                .mapToObj(i -> tempList.get(i % tempList.size()))
                .collect(Collectors.toList());
    }

}
