package com.example.pizzeria;

import com.example.pizzeria.models.Recipe;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class PizzaMenuReader {
    private final List<Recipe> recipes;
    private final static String filePath = "src/main/resources/pizza-menu.json";

    public PizzaMenuReader()  {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Recipe> temp;
        try {
            temp = objectMapper.readValue(new File(filePath), new TypeReference<>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
            temp = new ArrayList<>();
        }
        recipes = temp;
    }

    public List<Recipe> getAllRecipes() {
        return recipes;
    }
}
