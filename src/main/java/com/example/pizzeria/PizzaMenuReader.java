package com.example.pizzeria;

import com.example.pizzeria.models.Recipe;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Component
public class PizzaMenuReader {
    private final static String filePath = Paths.get("src", "main",
            "resources", "pizza-menu.json").toString();

    public List<Recipe> getAllRecipes() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Recipe> recipes;
        recipes = objectMapper.readValue(new File(filePath), new TypeReference<>() {});
        return recipes;
    }
}
