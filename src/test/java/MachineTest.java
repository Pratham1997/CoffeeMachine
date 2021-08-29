import models.Drink;
import models.Ingredient;
import models.Machine;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.MachineService;
import utils.MachineJsonParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MachineTest {

    Machine machine;
    MachineService machineService;

    @BeforeEach
    public void init() throws IOException, ParseException {
        machine = MachineJsonParser.getMachineObjectFromJSON("test1.json");
        machineService = new MachineService(machine);
    }

    @Test
    public void coffeeMachineTest() {
        /**
         * Check initial Ingredients in the machine
         */
        HashMap<Ingredient, Integer> ingredientIntegerHashMap =  machineService.getMachineIngredientStore();
        Assertions.assertEquals(5, ingredientIntegerHashMap.size());

        createGivenDrinks(machine.getDrinks());
        Assertions.assertEquals(2,machineService.getMachineDrinkHistory().size());

        /**
         * Check current ingredient store
         */
        ingredientIntegerHashMap =  machineService.getMachineIngredientStore();
        Assertions.assertEquals(5, ingredientIntegerHashMap.size());

        /**
         * Refill new ingredient to machine (new ingredient added)
         */
        machineService.refillIngredient(new Ingredient("choco_milk"), 500);
        ingredientIntegerHashMap =  machineService.getMachineIngredientStore();
        Assertions.assertEquals(6, ingredientIntegerHashMap.size());

        /**
         * Refill multiple ingredients in the machine
         */
        HashMap<Ingredient, Integer> refill = new HashMap<>();
        refill.put(new Ingredient("ginger_syrup"), 200);
        refill.put(new Ingredient("hot_milk"), 400);
        refill.put(new Ingredient("hot_choco"), 400);
        machineService.refillMultipleIngredients(refill);

        /**
         * Check Machine ingredients after refilling
         * Indicates the ingredients that are running low
         */
        ingredientIntegerHashMap =  machineService.getMachineIngredientStore();
        Assertions.assertEquals(7, ingredientIntegerHashMap.size());

        /**
         * Create drinks when machine OFF
         */
        machineService.createDrinks(machine.getDrinks());
        Assertions.assertEquals(2,machine.getDrinkServedHistory().size());

        /**
         * Turn ON machine and create drinks again
         * Number of drinks more than outlets (added to queue and created when outlet free)
         */
        List<Drink> drinks = new ArrayList<>();
        drinks.addAll(machine.getDrinks());
        drinks.addAll(machine.getDrinks());

        machineService.startMachine();
        createGivenDrinks(drinks);
        Assertions.assertEquals(3,machineService.getMachineDrinkHistory().size());

        /**
         * Update the number of outlets and start machine again
         */
        machine.setOutlets(6);
        machineService.startMachine();
        createGivenDrinks(drinks);
        Assertions.assertEquals(3,machineService.getMachineDrinkHistory().size());
    }


    private void createGivenDrinks(List<Drink> drinks){
        /**
         * Create each of the given drinks in the machine
         */
        machineService.createDrinks(drinks);
        machineService.turnMachineOff();
    }

}
