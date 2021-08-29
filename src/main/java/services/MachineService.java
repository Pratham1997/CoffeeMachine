package services;

import models.Drink;
import models.Ingredient;
import models.Machine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class MachineService {

    private final Machine machine;
    private ThreadPoolExecutor executor;

    public MachineService(Machine machine) {
        this.machine = machine;
        startMachine();
    }

    /**
     * Turn Off this machine object (Closes the N outlets ie. threads)
     * Close the thread pool corresponding to the N outlets
     */
    public void turnMachineOff(){
        executor.shutdown();
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException ex) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
        System.out.println("\nMachine Turned OFF");
    }

    /**
     * Start this machine objects (Opens the N outlets ie. threads)
     * Create a fixed thread pool of size N
     */
    public void startMachine(){
        executor = (ThreadPoolExecutor)Executors.newFixedThreadPool(machine.getOutlets());
        executor.setRejectedExecutionHandler(new RejectedTaskHandler());
        System.out.println("\nMachine Turned ON");
    }

    /**
     * Refill multiple ingredients to machine with given quantities
     * @param ingredientQuantityHashMap
     */
    public void refillMultipleIngredients(HashMap<Ingredient, Integer> ingredientQuantityHashMap){
        ingredientQuantityHashMap.forEach(this::refillIngredient);
    }

    /**
     * Refill given ingredient to machine with given quantity
     * @param ingredient
     * @param quantity
     */
    public void refillIngredient(Ingredient ingredient, int quantity){
        System.out.println("Refilling Ingredient: " + ingredient.getIngredientName() + "   Quantity: "+ quantity +"ml");
        int newQuantity = quantity;
        if(machine.getIngredientQuantity().containsKey(ingredient)){
            newQuantity += machine.getIngredientQuantity().get(ingredient);
        }
        machine.getIngredientQuantity().put(ingredient, newQuantity);
    }

    /**
     * Prints and returns the available ingredients in the machine
     */
    public HashMap<Ingredient, Integer> getMachineIngredientStore(){
        System.out.println("\nIngredients available in the Machine:");
        List<String> insufficientIngredients = new ArrayList<>();
        machine.getIngredientQuantity().forEach((ingredient, quantity) -> {
            System.out.println("Ingredient: " + ingredient.getIngredientName() + "   Quantity: " + quantity + "ml");
            if(quantity.equals(0)){
                insufficientIngredients.add(ingredient.getIngredientName());
            }
        });
        insufficientIngredients.forEach(ingredient -> System.out.println("Please refill " + ingredient));
        System.out.println();
        return machine.getIngredientQuantity();
    }

    /**
     * Create the given drink in machine
     * Send this drink to a free thread in thread pool
     * (If all threads/outlets are busy it is kept in the queue for the thread pool and picked as soon a thread is free)
     * @param drink
     */
    public void createDrink(Drink drink){
        Runnable worker = new DrinkMaker(drink, machine);
        executor.execute(worker);
    }

    /**
     * Create this list of drinks
     * N drinks made at a time parallel from N outlets/threads. Rest kept in queue.
     * @param drinks
     */
    public void createDrinks(List<Drink> drinks){
        drinks.forEach(this::createDrink);
    }

    /**
     * Get history of successfully served drinks by machine
     * @return
     */
    public List<String> getMachineDrinkHistory(){
        System.out.println("Successfully Created Beverages: "+ machine.getDrinkServedHistory());
        return machine.getDrinkServedHistory();
    }

}
