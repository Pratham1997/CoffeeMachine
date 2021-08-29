package models;

import lombok.Getter;
import lombok.ToString;

import java.util.HashMap;

@Getter
@ToString
public class Drink {

    private final String name;
    private final HashMap<Ingredient, Integer> ingredientComposition;

    public Drink(String name, HashMap<Ingredient, Integer> ingredientComposition) {
        this.name = name;
        this.ingredientComposition = ingredientComposition;
    }

}
