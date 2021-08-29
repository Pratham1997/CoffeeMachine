package models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Getter
@ToString
public class Machine {

    private int outlets;

    private final List<Drink> drinks;
    private final HashMap<Ingredient, Integer> ingredientQuantity;

    private final List<String> drinkServedHistory;

    public Machine(final int outlets, final List<Drink> drinks, final HashMap<Ingredient, Integer> ingredientQuantity) {
        this.outlets = outlets;
        this.drinks = drinks;
        this.ingredientQuantity = ingredientQuantity;
        this.drinkServedHistory = new ArrayList<>();
    }

    public void setOutlets(int outlets) {
        System.out.println("Number of Outlets Updated to " + outlets);
        this.outlets = outlets;
    }
}
