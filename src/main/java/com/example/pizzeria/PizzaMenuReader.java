package com.example.pizzeria;

import com.example.pizzeria.models.Recipe;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Component
public class PizzaMenuReader {
    public List<Recipe> getAllRecipes() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("pizza-menu.json");

        if (inputStream == null) {
            throw new IOException("Could not find pizza-menu.json resource");
        }

        List<Recipe> recipes = objectMapper.readValue(inputStream, new TypeReference<>() {});
        /*
            This is a mockup that is needed to test the modal window on the frontend,
            in the future, rewrite the time initialization outside this object
            and describe the coefficients for calculating time
        */
        recipes.forEach(recipe -> {
            int preparationTime = recipe.getToppings().size() * 2 + 6;
            recipe.setTime(preparationTime);
        });

        return recipes;
    }
}
