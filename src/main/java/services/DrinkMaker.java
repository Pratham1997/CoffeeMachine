package services;

import models.Drink;
import models.Ingredient;
import models.Machine;

import java.util.HashMap;

public class DrinkMaker implements Runnable{

    private final Drink drink;
    private final Machine machine;

    public DrinkMaker(Drink drink, Machine machine) {
        this.drink = drink;
        this.machine = machine;
    }

    @Override
    public void run() {
        synchronized (machine){
            createDrink(drink, machine.getIngredientQuantity());
        }
    }

    /**
     * Create the input drink with this machine.
     * Check if creating drink is possible and show corresponding message.
     *
     * @param drink
     * @param ingredientStoreMap
     */
    private void createDrink(Drink drink, HashMap<Ingredient, Integer> ingredientStoreMap){

        System.out.print("Outlet Number: " + (Thread.currentThread().getId()%machine.getOutlets() + 1) + "    ");
        if(!areIngredientsAvailableForDrink(drink, ingredientStoreMap) || !areIngredientsSufficientForDrink(drink, ingredientStoreMap)){
            return;
        }

        updateMachineIngredientStorage(drink, ingredientStoreMap);
        machine.getDrinkServedHistory().add(drink.getName());
        System.out.println(drink.getName() + " is prepared");
    }

    /**
     * Check if all ingredients needed for drink are available in machine
     * @param drink
     * @param ingredientStoreMap
     * @return
     */
    private boolean areIngredientsAvailableForDrink(Drink drink, HashMap<Ingredient, Integer> ingredientStoreMap){
        for(Ingredient ingredient: drink.getIngredientComposition().keySet()) {
            if (!ingredientStoreMap.containsKey(ingredient)) {
                System.out.println(drink.getName() + " cannot be prepared because item " + ingredient.getIngredientName() + " is not available");
                return false;
            }
        }
        return true;
    }

    /**
     * Check if all ingredients for drink are sufficient in machine
     * @param drink
     * @param ingredientStoreMap
     * @return
     */
    private boolean areIngredientsSufficientForDrink(Drink drink, HashMap<Ingredient, Integer> ingredientStoreMap){
        int drinkIngredientQuantity;
        for(Ingredient ingredient: drink.getIngredientComposition().keySet()) {
            drinkIngredientQuantity = drink.getIngredientComposition().get(ingredient);
            if(drinkIngredientQuantity > ingredientStoreMap.get(ingredient)) {
                System.out.println(drink.getName() + " cannot be prepared because item " + ingredient.getIngredientName() +" is not sufficient");
                return false;
            }
        }
        return true;
    }

    /**
     * Upgrade ingredient storage in machine after creating input drink
     * @param drink
     * @param ingredientStoreMap
     */
    private void updateMachineIngredientStorage(Drink drink, HashMap<Ingredient, Integer> ingredientStoreMap){
        drink.getIngredientComposition().forEach((ingredient, quantity) -> {
            int finalQuantity = ingredientStoreMap.get(ingredient) - quantity;
            ingredientStoreMap.put(ingredient, finalQuantity);
        });
    }

    /**
     * Get drink name being created in current task
     */
    public String getDrink(){
        return drink.getName();
    }
}
