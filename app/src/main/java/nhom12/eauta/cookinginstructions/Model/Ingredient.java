package nhom12.eauta.cookinginstructions.Model;

import java.util.List;

public class Ingredient {
    private List<String> ingredients;

    public Ingredient() {
        // Default constructor required for calls to DataSnapshot.getValue(Ingredient.class)
    }

    public Ingredient(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }
}



