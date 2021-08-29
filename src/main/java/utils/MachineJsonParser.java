package utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.Drink;
import models.Ingredient;
import models.Machine;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.*;

public class MachineJsonParser {


    /**
     * Parse JSON file to generate the Machine Object from given data
     * @param filename
     * @return
     * @throws IOException
     * @throws ParseException
     */
    public static Machine getMachineObjectFromJSON(String filename) throws IOException, ParseException {
        InputStream inputStream = MachineJsonParser.class.getClassLoader().getResourceAsStream(filename);
        assert inputStream != null;

        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(new InputStreamReader(inputStream));
        JSONObject machineObject = (JSONObject)jsonObject.get("machine");

        JSONObject outletsObject = (JSONObject)machineObject.get("outlets");

        JSONObject machineIngredientQuantity = (JSONObject) machineObject.get("total_items_quantity");
        TypeReference<HashMap<String, Integer>> typeRef = new TypeReference<>() {};
        HashMap<String, Integer> ingredientQuantityMapping = new ObjectMapper().readValue(machineIngredientQuantity.toString(), typeRef);

        JSONObject beverageObject = (JSONObject) machineObject.get("beverages");
        TypeReference<HashMap<String, HashMap<String, Integer>>> bev = new TypeReference<>() {};
        HashMap<String, HashMap<String, Integer>> beverageMapping = new ObjectMapper().readValue(beverageObject.toString(), bev);

        int outlets = Integer.parseInt(outletsObject.get("count_n").toString());
        HashMap<Ingredient, Integer> ingredientQuantity = generateIngredientMap(ingredientQuantityMapping);
        List<Drink> drinks = new ArrayList<>();
        for (String beverageName: beverageMapping.keySet()){
            drinks.add(new Drink(beverageName, generateIngredientMap(beverageMapping.get(beverageName))));
        }

        return new Machine(outlets, drinks, ingredientQuantity);
    }


    private static HashMap<Ingredient, Integer> generateIngredientMap(HashMap<String, Integer> mapping){
        HashMap<Ingredient, Integer> ingredientMap = new HashMap<>();
        for(String ingredientName: mapping.keySet()){
            ingredientMap.put(new Ingredient(ingredientName), mapping.get(ingredientName));
        }
        return ingredientMap;
    }


}
