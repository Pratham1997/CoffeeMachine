package models;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Ingredient {

    private final String ingredientName;

    public Ingredient(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Ingredient)) {
            return false;
        }
        Ingredient c = (Ingredient) o;
        return c.getIngredientName().equals(this.getIngredientName());
    }

    @Override
    public int hashCode() {
        return this.ingredientName.hashCode();
    }

}
